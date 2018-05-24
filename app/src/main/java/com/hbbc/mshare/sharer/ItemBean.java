package com.hbbc.mshare.sharer;

import com.hbbc.mshare.login.BaseResultBean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/1.
 *
 */
public class ItemBean extends BaseResultBean{

    private String GoodsSNID;
    private String GoodsName;
    private List<String> PicList;
    private String GoodsIntroduceText;

    private String GoodsTypeID;
    private String GoodsUsePrice;
    private String BusinessType;
    private String GoodsDeposit;
    private long Lat;
    private long Lng;

    private List<TypeBean> GoodsTypeList;

    private String LabelContent;

    private List<LabelBean> TypeLabelList;



    public String getLabelContent() {

        return LabelContent;
    }



    public void setLabelContent(String labelContent) {

        LabelContent = labelContent;
    }



    public List<LabelBean> getTypeLabelList() {

        return TypeLabelList;
    }



    public void setTypeLabelList(List<LabelBean> typeLabelList) {

        TypeLabelList = typeLabelList;
    }



    public long getLng() {

        return Lng;
    }



    public void setLng(long lng) {

        Lng = lng;
    }



    public long getLat() {

        return Lat;
    }



    public void setLat(long lat) {

        Lat = lat;
    }



    public String getGoodsSNID() {

        return GoodsSNID;
    }



    public void setGoodsSNID(String goodsSNID) {

        GoodsSNID = goodsSNID;
    }



    public String getGoodsName() {

        return GoodsName;
    }



    public void setGoodsName(String goodsName) {

        GoodsName = goodsName;
    }



    public List<String> getPicList() {

        return PicList;
    }



    public void setPicList(List<String> picList) {

        PicList = picList;
    }



    public String getGoodsIntroduceText() {

        return GoodsIntroduceText;
    }



    public void setGoodsIntroduceText(String goodsIntroduceText) {

        GoodsIntroduceText = goodsIntroduceText;
    }



    public String getGoodsTypeID() {

        return GoodsTypeID;
    }



    public void setGoodsTypeID(String goodsTypeID) {

        GoodsTypeID = goodsTypeID;
    }



    public String getGoodsUsePrice() {

        return GoodsUsePrice;
    }



    public void setGoodsUsePrice(String goodsUsePrice) {

        GoodsUsePrice = goodsUsePrice;
    }



    public String getBusinessType() {

        return BusinessType;
    }



    public void setBusinessType(String businessType) {

        BusinessType = businessType;
    }



    public String getGoodsDeposit() {

        return GoodsDeposit;
    }



    public void setGoodsDeposit(String goodsDeposit) {

        GoodsDeposit = goodsDeposit;
    }



    public List<TypeBean> getGoodsTypeList() {

        return GoodsTypeList;
    }



    public void setGoodsTypeList(List<TypeBean> goodsTypeList) {

        GoodsTypeList = goodsTypeList;
    }



    @Override
    public String toString() {

        return "ItemBean{" +
                "GoodsSNID='" + GoodsSNID + '\'' +
                ", GoodsName='" + GoodsName + '\'' +
                ", PicList=" + PicList +
                ", GoodsIntroduceText='" + GoodsIntroduceText + '\'' +
                ", GoodsTypeID='" + GoodsTypeID + '\'' +
                ", GoodsUsePrice='" + GoodsUsePrice + '\'' +
                ", BusinessType='" + BusinessType + '\'' +
                ", GoodsDeposit='" + GoodsDeposit + '\'' +
                ", Lat=" + Lat +
                ", Lng=" + Lng +
                ", GoodsTypeList=" + GoodsTypeList +
                ", LabelContent='" + LabelContent + '\'' +
                ", TypeLabelList=" + TypeLabelList +
                '}';
    }
}
