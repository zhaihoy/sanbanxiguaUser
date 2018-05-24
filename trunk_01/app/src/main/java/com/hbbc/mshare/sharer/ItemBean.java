package com.hbbc.mshare.sharer;

import com.hbbc.mshare.login.BaseResultBean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/1.
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

    private List<TypeBean> GoodsTypeList;


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
                ", GoodsTypeList=" + GoodsTypeList +
                '}';
    }
}
