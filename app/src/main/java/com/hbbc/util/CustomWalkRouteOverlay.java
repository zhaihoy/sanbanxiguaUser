package com.hbbc.util;

import android.content.Context;
import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.WalkPath;
import com.hbbc.R;
import com.hbbc.mshare.overlay.WalkRouteOverlay;

/**
 * Created by Administrator on 2017/7/31.
 *
 */
public class CustomWalkRouteOverlay extends WalkRouteOverlay {

    private Context context;
    public CustomWalkRouteOverlay(Context context, AMap aMap, WalkPath walkPath, LatLonPoint latLonPoint, LatLonPoint latLonPoint1) {

        super(context, aMap, walkPath, latLonPoint, latLonPoint1);
        this.context=context;
    }



    @Override
    protected BitmapDescriptor getEndBitmapDescriptor() {
        //重新定义路径终点坐标,如果返回null,那么默认会显示一个蓝色的小气泡,非常丑陋,此处我们重复添加一个图标即可!
        return BitmapDescriptorFactory.fromResource(R.drawable.paperlogo);
    }



    @Override
    protected BitmapDescriptor getStartBitmapDescriptor() {

        return BitmapDescriptorFactory.fromResource(R.drawable.mshare_search_center);//重新定义路径起点坐标
    }



    @Override
    protected int getWalkColor() {

        return Color.parseColor("#3ca4ed");
    }



    /**
     * 定义路径的宽度像素值
     * @return
     */
//    @Override
//    protected float getBuslineWidth() {
//
//        return 3*context.getResources().getDisplayMetrics().density;//默认是18f
//    }


    @Override
    protected float getRouteWidth() {

        return 3*context.getResources().getDisplayMetrics().density;//默认是18f
//        return super.getRouteWidth();
    }
    @Override
    protected BitmapDescriptor getWalkBitmapDescriptor() {
//        super.getWalkBitmapDescriptor();
        return null;
    }



//    @Override
//    protected BitmapDescriptor getBitDes(Bitmap bitmap, String s) {
//
//        return super.getBitDes(bitmap,s);
//    }



    @Override
    public void setNodeIconVisibility(boolean b) {

        super.setNodeIconVisibility(false);//默认不显示转角图标
    }


}
