package com.hbbc.mshare.main;

import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 *
 */
public class ResultBean {

    //{"Result":true,"Notice":"操作成功",
    // "GoodsList":[{"Lng":116.184448,"GoodsDeposit":1,"distance":0.005948132950797006,"BusinessType":1,"GoodsID":1,"GoodsUsePrice":15,"Lat":39.928708,"GoodsName":"自行车"},
    // {"Lng":116.187538,"GoodsDeposit":100,"distance":0.16467828245623095,"BusinessType":1,"GoodsID":2,"GoodsUsePrice":20,"Lat":39.929077,"GoodsName":"iphone"}]}

    private String Result;
    private String Notice;
    private List<Goods> GoodsList;

    public ResultBean(String result, String notice, List<Goods> goodsList) {

        Result = result;
        Notice = notice;
        GoodsList = goodsList;
    }



    public String getResult() {

        return Result;
    }



    public void setResult(String result) {

        Result = result;
    }



    public String getNotice() {

        return Notice;
    }



    public void setNotice(String notice) {

        Notice = notice;
    }



    public List<Goods> getGoodsList() {

        return GoodsList;
    }



    public void setGoodsList(List<Goods> goodsList) {

        GoodsList = goodsList;
    }



    public class Goods {
        //{"Lng":116.187538,"GoodsDeposit":100,"distance":0.16467828245623095,
        // "BusinessType":1,"GoodsID":2,"GoodsUsePrice":20,"Lat":39.929077,"GoodsName":"iphone"}

        private double Lng;
        private double Lat;

        private float GoodsDeposit;
        private double distance;
        private int BusinessType;
        private int GoodsSNID;
        private float GoodsUsePrice;
        private String GoodsName;
        private String GoodsPic;



        public double getLng() {

            return Lng;
        }



        public void setLng(double lng) {

            Lng = lng;
        }



        public double getLat() {

            return Lat;
        }



        public void setLat(double lat) {

            Lat = lat;
        }



        public float getGoodsDeposit() {

            return GoodsDeposit;
        }



        public void setGoodsDeposit(float goodsDeposit) {

            GoodsDeposit = goodsDeposit;
        }



        public double getDistance() {

            return distance;
        }



        public void setDistance(double distance) {

            this.distance = distance;
        }



        public int getBusinessType() {

            return BusinessType;
        }



        public void setBusinessType(int businessType) {

            BusinessType = businessType;
        }



        public int getGoodsSNID() {

            return GoodsSNID;
        }



        public void setGoodsSNID(int goodsSNID) {

            GoodsSNID = goodsSNID;
        }



        public float getGoodsUsePrice() {

            return GoodsUsePrice;
        }



        public void setGoodsUsePrice(float goodsUsePrice) {

            GoodsUsePrice = goodsUsePrice;
        }



        public String getGoodsName() {

            return GoodsName;
        }



        public void setGoodsName(String goodsName) {

            GoodsName = goodsName;
        }



        public String getGoodsPic() {

            return GoodsPic;
        }



        public void setGoodsPic(String goodsPic) {

            GoodsPic = goodsPic;
        }
    }
}
