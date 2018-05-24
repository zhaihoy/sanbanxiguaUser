package com.hbbc.mshare.user.search;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2017/8/2.
 *
 */
@DatabaseTable(tableName = "search_history")
public class SearchHistoryBean {

    @DatabaseField(generatedId =true)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String address;
    @DatabaseField
    private long millis;//查询时,从大到小输出
    @DatabaseField
    private double lat;
    @DatabaseField
    private double lng;

    public SearchHistoryBean() {

    }

    public SearchHistoryBean(int id, String name, String address, long millis, double lat, double lng) {

        this.id = id;
        this.name = name;
        this.address = address;
        this.millis = millis;
        this.lat = lat;
        this.lng = lng;
    }



    @Override
    public String toString() {

        return "SearchHistoryBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", millis=" + millis +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }



    public int getId() {

        return id;
    }



    public void setId(int id) {

        this.id = id;
    }



    public String getName() {

        return name;
    }



    public void setName(String name) {

        this.name = name;
    }



    public String getAddress() {

        return address;
    }



    public void setAddress(String address) {

        this.address = address;
    }



    public long getMillis() {

        return millis;
    }



    public void setMillis(long millis) {

        this.millis = millis;
    }



    public double getLat() {

        return lat;
    }



    public void setLat(double lat) {

        this.lat = lat;
    }



    public double getLng() {

        return lng;
    }



    public void setLng(double lng) {

        this.lng = lng;
    }
}
