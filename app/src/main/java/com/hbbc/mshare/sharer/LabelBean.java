package com.hbbc.mshare.sharer;

/**
 * Created by Administrator on 2017/8/28.
 *
 */
public class LabelBean {
    //"GoodsTypeID":19,"LabelOption":"6,8,10","GoodsTypeLabelID":28,"LabelName":"张数"

    private String GoodsTypeLabelID;
    private String LabelName;
    private String GoodsTypeID;
    private String LabelOption;



    public String getGoodsTypeLabelID() {

        return GoodsTypeLabelID;
    }



    public void setGoodsTypeLabelID(String goodsTypeLabelID) {

        GoodsTypeLabelID = goodsTypeLabelID;
    }



    public String getLabelName() {

        return LabelName;
    }



    public void setLabelName(String labelName) {

        LabelName = labelName;
    }



    public String getGoodsTypeID() {

        return GoodsTypeID;
    }



    public void setGoodsTypeID(String goodsTypeID) {

        GoodsTypeID = goodsTypeID;
    }



    public String getLabelOption() {

        return LabelOption;
    }



    public void setLabelOption(String labelOption) {

        LabelOption = labelOption;
    }



    @Override
    public String toString() {

        return "LabelBean{" +
                "GoodsTypeLabelID='" + GoodsTypeLabelID + '\'' +
                ", LabelName='" + LabelName + '\'' +
                ", GoodsTypeID='" + GoodsTypeID + '\'' +
                ", LabelOption='" + LabelOption + '\'' +
                '}';
    }
}
