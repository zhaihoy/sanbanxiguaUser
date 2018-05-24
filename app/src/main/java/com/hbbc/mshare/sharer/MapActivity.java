package com.hbbc.mshare.sharer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.AMapGestureListener;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hbbc.R;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.user.main.ResultBean;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyPopupWindow;
import com.hbbc.util.MyRelativeLayout;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.PermissionUtil;
import com.hbbc.util.ToastUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 * 集成3D地图包,效果就是比2D瓦片图好的太多了!!! Awesome'
 */
public class MapActivity extends BaseActivity implements AMap.OnMyLocationChangeListener, AMapLocationListener,
        LocationSource, AMap.OnMarkerClickListener, MyTopbar.OnTopBarClickListener, MyRelativeLayout.OnViewPositionChangedListener, MyRelativeLayout.OnPopupWindowGoneListener {

    private static final String TAG = "MapActivity";

    private static final int GET_PRODUCTS_FROM_SERVER = 101;

    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 1;

    private static final int DELETED_MARKER_SNID = 1;

    private MapView mapView;

    private AMap aMap;

    private MyPopupWindow popupWindow;

    private MyLocationStyle locationStyle;

    private AMapLocationClient mlocationClient;

    private AMapLocationListener locationListener;

    private OnLocationChangedListener mListener;

    private AMapLocationClientOption mLocationOption;

    private String currentAddress;

    private LatLng currentLatLng;

    private HttpUtil httpUtil;

    private List<ResultBean.Goods> goodsList;

    private Marker selectedMarker;//当前正在popup_window中展示的marker



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharer_map);

        httpUtil = new HttpUtil();
        initView();
        initMapView(savedInstanceState);
        initAMap();
        initLocationClient();

    }



    private void initLocationClient() {

        mlocationClient = new AMapLocationClient(getApplicationContext());
        mlocationClient.setLocationListener(this);
        AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
        locationClientOption.setLocationCacheEnable(false);
        locationClientOption.setOnceLocation(true);
        locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//优先精度高的方式
        mlocationClient.setLocationOption(locationClientOption);
        //在此发起第一次定位,首先得检查权限
        checkPermission();
    }



    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                PermissionUtil.showRationale(this, "定位权限不足");
            } else {//如果没有权限,并且用户之前选择了不再询问,此处就直接申请权限!
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
        } else {
            mlocationClient.startLocation();
        }
    }



    private void initMapView(Bundle savedInstanceState) {

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
    }



    @Override
    protected void initView() {

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        MyRelativeLayout container = (MyRelativeLayout) findViewById(R.id.container);
        container.setOnViewPositionChangedListener(this);
        container.setonPopupWindowGoneListener(this);

        popupWindow = (MyPopupWindow) findViewById(R.id.popup_window);

        ImageButton iv = (ImageButton) findViewById(R.id.btn_position);
        iv.setOnClickListener(this);

        TextView tv_edit = (TextView) findViewById(R.id.btn_edit);
        tv_edit.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.ll_zoom_button_comtainer).setElevation(100);
        }

        findViewById(R.id.ib_zoomin).setOnClickListener(this);
        findViewById(R.id.ib_zoomout).setOnClickListener(this);

    }



    /**
     * 将popup_window显示出来,并添加动画效果
     */
    private void showPopupWindowWithAnimation() {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.popup_in);
        popupWindow.setAnimation(animation);
//        animation.startNow();
        popupWindow.getAnimation().start();
        popupWindow.setVisibility(View.VISIBLE);
    }



    @Override
    protected void getMessage(Message msg) {

        ResultBean result = (ResultBean) msg.obj;
        if (result == null || result.getGoodsList() == null)
            return;
        Log.d(TAG, "getMessage: intropic===>>>" + result.toString());
        //绘制图层上图标
        goodsList = result.getGoodsList();
        Log.d(TAG, "getMessage: count===>>>" + goodsList.size());
        addAllProductsMarkers();

    }



    /**
     * 把服务器返回来的所有物品,添加到地图图层上显示,一个图标代表一个对象.
     */
    private void addAllProductsMarkers() {

        if (goodsList == null || goodsList.size() == 0)
            return;
        for (int i = 0; i < goodsList.size(); i++) {
            final ResultBean.Goods goods = goodsList.get(i);
            LatLng latLng = new LatLng(goods.getLat(), goods.getLng());
            final MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(latLng);
//            markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");
//            markerOption.draggable(true);//设置Marker可拖动

            //////////////////////////
            //下面的 .icon()自带有清除已有图标的功能了,应该不会是此处添加了默认的位置图标
            //////////////////////////

            final View inflate = LayoutInflater.from(this).inflate(R.layout.mshare_marker_view, null);
            final ImageView iv_marker = (ImageView) inflate.findViewById(R.id.iv_marker);
            Log.d(TAG, "addAllProductsMarkers: markerIcon is: " + goods.getGoodsTypePicFileID());
            Glide.with(this).load(goods.getGoodsTypePicFileID())
                    .placeholder(R.drawable.paperlogo)
                    .error(R.drawable.paperlogo)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                            iv_marker.setImageResource(R.drawable.paperlogo);
                            //            markerOption.icon(BitmapDescriptorFactory.fromBitmap(drawingCache));
                            markerOption.icon(BitmapDescriptorFactory.fromView(inflate));//这个方法更好点,可以延迟获取,相比快照来说!
                            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
//            markerOption.setFlat(true);//设置marker平贴地图效果
                            Marker marker = aMap.addMarker(markerOption);
                            marker.setObject(goods);
                            return true;
                        }



                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                            iv_marker.setImageDrawable(resource);
                            //            markerOption.icon(BitmapDescriptorFactory.fromBitmap(drawingCache));
                            markerOption.icon(BitmapDescriptorFactory.fromView(inflate));//这个方法更好点,可以延迟获取,相比快照来说!
                            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
//            markerOption.setFlat(true);//设置marker平贴地图效果
                            Marker marker = aMap.addMarker(markerOption);
                            marker.setObject(goods);
                            return true;
                        }
                    })
                    .into(iv_marker);

            //延迟问题怎么解决？或者加载失败时
//            iv_marker.setDrawingCacheEnabled(true);
//            iv_marker.buildDrawingCache();
//            Bitmap drawingCache = convertViewToBitmap(inflate);

//            markerOption.icon(BitmapDescriptorFactory.fromBitmap(drawingCache));
//            markerOption.icon(BitmapDescriptorFactory.fromView(inflate));//这个方法更好点,可以延迟获取,相比快照来说!


            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
//        markerOption.setFlat(true);//设置marker平贴地图效果
//            Marker marker = aMap.addMarker(markerOption);
//            marker.setObject(goods);

        }
    }



    //将Ｖiew 的快照给转化成一个图片,作为marker的背景
    private Bitmap convertViewToBitmap(View inflate) {

        inflate.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        inflate.layout(0, 0, inflate.getMeasuredWidth(), inflate.getMeasuredHeight());
        inflate.buildDrawingCache();
        Bitmap bitmap = inflate.getDrawingCache();
        return bitmap;

    }



    //显示地图
    private void initAMap() {

        if (aMap == null)
            aMap = mapView.getMap();

        aMap.setLocationSource(this);//核心方法,关联地图控制器与定位LocationSource;
        aMap.setOnMarkerClickListener(this);

        locationStyle = new MyLocationStyle();
//        locationStyle.showMyLocation(true);
        locationStyle.interval(2000);
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        aMap.setMyLocationStyle(locationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.getUiSettings().setCompassEnabled(true);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false;
        aMap.setOnMyLocationChangeListener(this);


        aMap.setAMapGestureListener(new AMapGestureListener() {

            @Override
            public void onDoubleTap(float v, float v1) {
                //单指双击
                Log.d(TAG, "onDoubleTap: ");
            }



            @Override
            public void onSingleTap(float v, float v1) {
//单指单击
                Log.d(TAG, "onSingleTap: ");
            }



            @Override
            public void onFling(float v, float v1) {
//单指惯性滑动float velocityX,
//            float velocityY


                Log.d(TAG, "onFling: Vx=" + v + "Vy=" + v1);
            }



            @Override
            public void onScroll(float v, float v1) {
//单指滑动
                Log.d(TAG, "onScroll: ");
            }



            @Override
            public void onLongPress(float v, float v1) {
//长按
                Log.d(TAG, "onLongPress: ");
            }



            @Override
            public void onDown(float v, float v1) {
//单指按下
                Log.d(TAG, "onDown: ");
            }



            @Override
            public void onUp(float v, float v1) {
//单指抬起
                Log.d(TAG, "onUp: ");
            }



            @Override
            public void onMapStable() {
//地图稳定下来会回到此接口
                Log.d(TAG, "onMapStable: ");
            }
        });

    }



    @Override
    public void onClick(View v) {

        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_position:
                reposition();
                break;
            case R.id.btn_edit:
                if (popupWindow.getVisibility() == View.VISIBLE && notInUsageStatus())
                    jumpToEditPage();
                break;
            case R.id.ib_zoomin:
                zoomIn(CameraUpdateFactory.zoomIn());
                break;
            case R.id.ib_zoomout:
                zoomOut(CameraUpdateFactory.zoomOut());
                break;
        }
    }



    private boolean notInUsageStatus() {

        ResultBean.Goods selectedProduct = (ResultBean.Goods) selectedMarker.getObject();
        String status = selectedProduct.getStatus();
        if (status.equals("2")) {//使用中
            ToastUtil.toast("当前物品正在使用中,不可编辑");
            return false;
        }

        return true;
    }



    /**
     * 点击缩小按钮,缩小一级
     */
    private void zoomOut(CameraUpdate cameraUpdate) {

        if (aMap == null) {
            return;
        }
        aMap.animateCamera(cameraUpdate, 500, null);

    }



    /**
     * 点击放大按钮,放大一级
     *
     * @param cameraUpdate
     */
    private void zoomIn(CameraUpdate cameraUpdate) {

        if (aMap == null) {
            return;
        }
        aMap.animateCamera(cameraUpdate, 500L, null);

    }



    /**
     * 跳入到物品编辑页
     */
    private void jumpToEditPage() {

        Intent intent = new Intent(this, NewItemActivity.class);
        ResultBean.Goods selectedProduct = (ResultBean.Goods) selectedMarker.getObject();

        Log.d(TAG, "jumpToEditPage: snid===>>>" + selectedProduct.getGoodsSNID());
        intent.putExtra(Constants.GoodsSNID, String.valueOf(selectedProduct.getGoodsSNID()));
        startActivityForResult(intent, DELETED_MARKER_SNID);
        overridePendingTransition(R.anim.global_in, 0);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == DELETED_MARKER_SNID) {
            int deletedMarkerID = data.getIntExtra("DELETED_MARKER_SNID", -1);
            if (popupWindow.getVisibility() == View.VISIBLE) {
                hidePopupWindowWithAnimation();
                //删除此marker
                if (selectedMarker != null && deletedMarkerID != -1
                        && ((ResultBean.Goods) selectedMarker.getObject()).getGoodsSNID() == deletedMarkerID) {
                    selectedMarker.remove();
                }
            }
        }
    }



    private void reposition() {

        checkPermission();

        if (mListener == null) {
            Toast.makeText(MapActivity.this, "listener = null", Toast.LENGTH_SHORT).show();
            return;
        }

        //重新定位时,清除掉之前的规划路径图层
//        clearWalkRouteOverlay();

        clearAllLayer();//清除所有图层
        if (popupWindow.getVisibility() == View.VISIBLE)
            hidePopupWindowWithAnimation();//隐藏掉popup_window

        mlocationClient = new AMapLocationClient(MapActivity.this);
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位参数
//            mLocationOption.setInterval(10 * 1000);
        mLocationOption.setOnceLocation(true);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }



    /**
     * 将popup_window隐藏,并添加动画效果
     */

    private void hidePopupWindowWithAnimation() {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.popup_out);
        popupWindow.setAnimation(animation);
//        animation.startNow();
        popupWindow.getAnimation().start();
        popupWindow.setVisibility(View.GONE);
    }



    private void clearAllLayer() {

        if (aMap != null)
            aMap.clear();
    }



    @Override
    protected void onStart() {

        super.onStart();
    }



    @Override
    protected void onResume() {

        super.onResume();
        mapView.onResume();
//        checkPermission();
    }



    @Override
    protected void onPause() {

        super.onPause();
        mapView.onPause();
    }



    @Override
    protected void onStop() {

        super.onStop();
        deactivate();
    }



    @Override
    protected void onDestroy() {

        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }



    /**
     * 定位成功,回调方法
     */
    @Override
    public void onMyLocationChange(Location location) {
        // 定位回调监听
        if (location != null) {
            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if (bundle != null) {

                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);

                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType);

                if (errorCode == 0) {//定位成功

                    currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(currentLatLng, 16f, 30, 0)));

                } else {//定位失败

                }
            } else {//定位失败

                Log.e("amap", "定位信息， bundle is null ");

            }

        } else {//定位失败
            Log.e("amap", "定位失败");
        }

    }



    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        if (mListener != null && amapLocation != null) {
            changeCurrentAddress(amapLocation);
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                currentLatLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());

//                aMap.animateCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                //CameraPosition中的bearing是什么意思?
                aMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(currentLatLng).zoom(16f).tilt(30f).bearing(0f).build()));
                ToastUtil.toast_debug("定位成功");
                //拿到定位到的坐标后,向服务器请求附近物品数据
                getProductsFromServer(currentLatLng);


            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Toast.makeText(this, errText, Toast.LENGTH_SHORT).show();
            }
        }
    }



    /**
     * 按定位后的坐标向服务器请求附近共享物品的信息
     * TODO 当然,以后要加上限制定位的频次的逻辑
     */
    private void getProductsFromServer(LatLng latLng) {

        UserDao userDao = UserDao.getDaoInstance(this);
        LoginResultBean currentUser = userDao.query();
        if (currentUser == null) {
            ToastUtil.toast("当前没有登陆用户");
            return;
        }

        String phoneNumber = currentUser.getPhoneNumber();

        //116.184419,39.928728
        httpUtil.callJson(handler, GET_PRODUCTS_FROM_SERVER, this, GlobalConfig.SHARER_SERVER_ROOT + GlobalConfig.SHARER_RETRIEVE_ALL_PRODUCTS_AROUND,
                ResultBean.class, Constants.AppID, GlobalConfig.AppID, Constants.ECID, GlobalConfig.ECID, Constants.PhoneNumber, phoneNumber);
        aMap.setOnMarkerClickListener(this);

    }



    /**
     * 记录下此次定位到的当前位置在哪里
     */
    private void changeCurrentAddress(AMapLocation aMapLocation) {

        if (aMapLocation == null)
            return;
        StringBuilder sb = new StringBuilder();
        currentAddress = sb.append(aMapLocation.getCity())
                .append(aMapLocation.getDistrict())
                .append(aMapLocation.getStreet())
                .append(aMapLocation.getStreetNum())
                .toString();

    }



    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationCacheEnable(true);//

            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
//            mLocationOption.setInterval(10 * 1000);
            mLocationOption.setOnceLocation(true);
            mlocationClient.setLocationOption(mLocationOption);

        }

    }



    @Override
    public void deactivate() {

        mlocationClient = null;
    }



    @Override
    public boolean onMarkerClick(Marker marker) {

        ToastUtil.toast_debug("marker clicked!");
        jumpPoint(marker);

        if (marker.getObject() == null) {
            return false;
        }

        if (selectedMarker == marker)
            return false;
        else
            selectedMarker = marker;

        bindDataToPopupWindow(marker);//绑定数据

        if (popupWindow.getVisibility() != View.VISIBLE)
            showPopupWindowWithAnimation();

        return false;//返回false时,系统会调用Camera,把点击的marker移动到视窗的中心上,我们也可以自己移动;
    }



    private void bindDataToPopupWindow(Marker marker) {

        ResultBean.Goods result = (ResultBean.Goods) marker.getObject();
        Glide.with(this).load(result.getGoodsIntroducePic1FileID())
                .error(R.drawable.test_with_background)
                .into((ImageView) popupWindow.findViewById(R.id.iv_thumb_nail));
        ((TextView) popupWindow.findViewById(R.id.tv_used_time)).setText(result.getUseTimes());
        ((TextView) popupWindow.findViewById(R.id.tv_total_income)).setText(result.getProfit());
        ((TextView) popupWindow.findViewById(R.id.tv_business_type)).setText(String.valueOf(result.getGoodsUsePrice()));
        ((TextView) popupWindow.findViewById(R.id.tv_SNCode)).setText(String.valueOf(result.getGoodsSNID()));

        int status = Integer.valueOf(result.getStatus());
        String status_desc;
        if (status == 1)
            status_desc = "待使用";
        else if (status == 2)
            status_desc = "使用中";
        else if (status == 3)
            status_desc = "待审核";
        else
            status_desc = "";
        ((TextView) popupWindow.findViewById(R.id.tv_status)).setText(status_desc);

    }



    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {

                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }



    @Override
    public void setOnLeftClick() {

        finish();

    }



    @Override
    public void setOnRightClick() {

    }



    //popup_window与relativelayout联动接口
    @Override
    public void onViewPositionChanged(int dy) {

        aMap.moveCamera(CameraUpdateFactory.scrollBy(0, dy));
    }



    //popup_window与relativelayout联动接口
    @Override
    public void onPopupWindowGone() {

        aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(currentLatLng, 16f, 30, 0)));
    }
}
