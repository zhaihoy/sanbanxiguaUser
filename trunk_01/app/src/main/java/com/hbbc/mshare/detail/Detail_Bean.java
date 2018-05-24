package com.hbbc.mshare.detail;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 * 请求数据
 {
 “ECID”: “100001”					//商户编号
 “GoodsSNID”:”123”					//物品编号
 }
 *
 */
public class Detail_Bean implements Serializable {

    private boolean Result;
    private String Notice;
    private String GoodsSNID;
    private String GoodsName;
    private List<String> PicList;
    private String GoodsIntroduceText;
    private String GoodsUsePrice;
    private String GoodsDeposit;
    private String BusinessType;
    private String LabelContent;


    public List<String> getPicList() {

        return PicList;
    }



    public void setPicList(List<String> picList) {

        PicList = picList;
    }



    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean result) {

        Result = result;
    }

    public String getNotice() {

        return Notice;
    }

    public void setNotice(String notice) {

        Notice = notice;
    }

    public String getGoodsSNID() {

        return GoodsSNID;
    }

    public void setGoodsSNID(String GoodsSNID) {

        GoodsSNID = GoodsSNID;
    }

    public String getGoodsName() {

        return GoodsName;
    }

    public void setGoodsName(String goodsName) {

        GoodsName = goodsName;
    }


    public String getGoodsIntroduceText() {

        return GoodsIntroduceText;
    }



    public void setGoodsIntroduceText(String goodsIntroduceText) {

        GoodsIntroduceText = goodsIntroduceText;
    }



    public String getGoodsUsePrice() {

        return GoodsUsePrice;
    }



    public void setGoodsUsePrice(String goodsUsePrice) {

        GoodsUsePrice = goodsUsePrice;
    }



    public String getGoodsDeposit() {

        return GoodsDeposit;
    }



    public void setGoodsDeposit(String goodsDeposit) {

        GoodsDeposit = goodsDeposit;
    }



    public String getBusinessType() {

        return BusinessType;
    }



    public void setBusinessType(String businessType) {

        BusinessType = businessType;
    }



    public String getLabelContent() {

        return LabelContent;
    }



    public void setLabelContent(String labelContent) {

        LabelContent = labelContent;
    }



    @Override
    public String toString() {

        return "Detail_Bean{" +
                "Result=" + Result +
                ", Notice='" + Notice + '\'' +
                ", GoodsSNID='" + GoodsSNID + '\'' +
                ", GoodsName='" + GoodsName + '\'' +
                ", PicList=" + PicList +
                ", GoodsIntroduceText='" + GoodsIntroduceText + '\'' +
                ", GoodsUsePrice='" + GoodsUsePrice + '\'' +
                ", GoodsDeposit='" + GoodsDeposit + '\'' +
                ", BusinessType='" + BusinessType + '\'' +
                ", LabelContent='" + LabelContent + '\'' +
                '}';
    }
}
