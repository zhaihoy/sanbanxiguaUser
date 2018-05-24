package com.hbbc.mshare.sharer;

/**
 * Created by Administrator on 2017/8/28.
 */
public class TypeBean {

    private boolean isSelected;
    private String GoodsTypeID;
    private String GoodsTypeName;

    public String getGoodsTypeID() {

        return GoodsTypeID;
    }



    public void setGoodsTypeID(String goodsTypeID) {

        GoodsTypeID = goodsTypeID;
    }



    public String getGoodsTypeName() {

        return GoodsTypeName;
    }



    public void setGoodsTypeName(String goodsTypeName) {

        GoodsTypeName = goodsTypeName;
    }



    @Override
    public String toString() {

        return "TypeBean{" +
                "GoodsTypeID='" + GoodsTypeID + '\'' +
                ", GoodsTypeName='" + GoodsTypeName + '\'' +
                '}';
    }
}
