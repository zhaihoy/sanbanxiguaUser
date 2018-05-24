package com.hbbc.mshare.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.services.core.LatLonPoint;

/**
 * Created by Administrator on 2017/8/2.
 * todo 此bean是专门在SearchActivity & MainActivity之间传递的
 * todo 其实是可以优化之前的逻辑,而省掉此class的.以后有时间再优化
 */
public class SearchBean implements Parcelable{
    private LatLonPoint latLonPoint;



    public SearchBean(LatLonPoint latLonPoint) {

        this.latLonPoint = latLonPoint;
    }



    protected SearchBean(Parcel in) {

        latLonPoint = in.readParcelable(LatLonPoint.class.getClassLoader());
    }



    public static final Creator<SearchBean> CREATOR = new Creator<SearchBean>() {
        @Override
        public SearchBean createFromParcel(Parcel in) {

            return new SearchBean(in);
        }



        @Override
        public SearchBean[] newArray(int size) {

            return new SearchBean[size];
        }
    };



    public LatLonPoint getLatLonPoint() {

        return latLonPoint;
    }



    public void setLatLonPoint(LatLonPoint latLonPoint) {

        this.latLonPoint = latLonPoint;
    }



    @Override
    public int describeContents() {

        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(latLonPoint, flags);
    }
}
