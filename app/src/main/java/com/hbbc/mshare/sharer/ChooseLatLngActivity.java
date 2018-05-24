package com.hbbc.mshare.sharer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.hbbc.R;
import com.hbbc.util.Constants;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.PermissionUtil;

/**
 * Created by Administrator on 2017/10/10.
 */
public class ChooseLatLngActivity extends AppCompatActivity
        implements MyTopbar.OnTopBarClickListener, AMap.OnMapClickListener, View.OnClickListener, AMapLocationListener {

    private static final String TAG = "ChooseLatLngActivity";

    private static final int REQUEST_FINE_LOCATION = 101;

    private MyTopbar topbar;

    private MapView mapView;

    private AMap aMap;

    private MyLocationStyle locationStyle;

    private TextView tv_confirm;

    private ImageButton ib_position;

    private AMapLocationClient locationClient;

    private AMapLocationClientOption locationOption;

    private LatLng mPosition;

    private MarkerOptions markOptiopns;

    private Marker product_marker;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharer_map_choose_position);
        initView(savedInstanceState);
        initMap();
        reposition();
    }



    //定位一次
    private void locateOnce() {

        locationStyle = new MyLocationStyle();
        locationStyle.showMyLocation(true);
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        aMap.setMyLocationStyle(locationStyle);
        aMap.setMyLocationEnabled(true);
    }



    @Override
    protected void onResume() {

        super.onResume();
        mapView.onResume();
    }



    @Override
    protected void onPause() {

        super.onPause();
        mapView.onPause();
    }



    @Override
    protected void onDestroy() {

        super.onDestroy();
        mapView.onDestroy();
        if (locationClient != null) locationClient.onDestroy();
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }



    private void initMap() {

        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setOnMapClickListener(this);
        aMap.getUiSettings().setCompassEnabled(true);
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.getUiSettings().setZoomControlsEnabled(false);

        if (mPosition != null) {
            addMarker(mPosition);
        }
    }



    private void initView(Bundle savedInstanceState) {

        topbar = (MyTopbar) findViewById(R.id.top_bar);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        if (mapView == null) {
            mapView = (MapView) findViewById(R.id.map_view);
        }
        mapView.onCreate(savedInstanceState);
        topbar.setOnTopBarClickListener(this);
        tv_confirm.setOnClickListener(this);

        Intent intent = getIntent();
        mPosition = intent.getParcelableExtra(Constants.CurrentPosition);


        ib_position = (ImageButton) findViewById(R.id.btn_position);
        findViewById(R.id.tv_confirm).setOnClickListener(this);
        ib_position.setOnClickListener(this);

        //两个缩放按钮+/-
        ImageButton ib_zoomin = (ImageButton) findViewById(R.id.ib_zoomin);
        ImageButton ib_zoomout = (ImageButton) findViewById(R.id.ib_zoomout);

        ib_zoomin.setOnClickListener(this);
        ib_zoomout.setOnClickListener(this);

    }



    @Override
    public void finish() {

        savePosition();
        super.finish();
        overridePendingTransition(R.anim.global_in, R.anim.global_out);
    }



    @Override
    public void onMapClick(LatLng latLng) {

        Log.d(TAG, "onMapClick: 点击了地图,返回点击点坐标:" + latLng.latitude + " , " + latLng.longitude);

        addMarker(latLng);

        mPosition = latLng;//保存选取的点
    }



    private void addMarker(LatLng latLng) {

        if(markOptiopns == null){
            markOptiopns = new MarkerOptions();
            TextView textView = new TextView(getApplicationContext());
            textView.setText("我的物品在这里");
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.BLACK);
            textView.setBackgroundResource(R.drawable.custom_info_bubble);
            markOptiopns.icon(BitmapDescriptorFactory.fromView(textView));

        }else{
            product_marker.remove();
        }
        markOptiopns.position(latLng);
        product_marker = aMap.addMarker(markOptiopns);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_position:
                reposition();
                break;
            case R.id.tv_confirm:
                confirm();
                break;
            case R.id.ib_zoomin:
                zoom(0);
                break;
            case R.id.ib_zoomout:
                zoom(1);
                break;
        }

    }



    private void zoom(int type) {

        aMap.animateCamera((type == 0) ? CameraUpdateFactory.zoomIn() : CameraUpdateFactory.zoomOut());
    }



    /**
     * 结束位置选取页面,选好了,就返回; 没有选,就关闭
     */
    private void confirm() {

        finish();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_FINE_LOCATION:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {//权限已经给了
                    reposition();
                } else {
                    PermissionUtil.showRationale(this, "定位权限不足");
                }
                break;
        }
    }



    /**
     * 进行一次定位
     */
    private void reposition() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                PermissionUtil.showRationale(this, "定位权限不足");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
            }
            return;
        }

        if (locationClient == null) {
            locationClient = new AMapLocationClient(this);
        }
        if (locationOption == null) {
            locationOption = new AMapLocationClientOption();
        }
        locationClient.setLocationListener(this);
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setOnceLocation(true);//只定位一次,不需要手动清除locationClient
        locationOption.setLocationCacheEnable(false);
        // TODO: 2017/10/11 依然要加上定位权限检查!!!
        locationClient.startLocation();
    }



    @Override
    public void setOnLeftClick() {

        finish();
    }



    //保存位置并关闭当前页面
    private void savePosition() {

        Intent result = new Intent();
        result.putExtra(Constants.LatLng, mPosition);
        setResult(RESULT_OK, result);
    }



    @Override
    public void setOnRightClick() {

    }



    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            locationClient.stopLocation();
            aMap.clear();
            aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.mshare_red_dot)).position(new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude())));
            aMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().zoom(10).target(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())).build()));
        }
    }

}
