package com.hbbc.mshare.sharer;

import com.hbbc.mshare.login.BaseResultBean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/28.
 *
 */
public class GoodsTypeLabelBean extends BaseResultBean {

    private List<TypeBean> GoodsTypeList;
    private List<LabelBean> TypeLabelList;


    public List<TypeBean> getGoodsTypeList() {

        return GoodsTypeList;
    }



    public void setGoodsTypeList(List<TypeBean> goodsTypeList) {

        GoodsTypeList = goodsTypeList;
    }



    public List<LabelBean> getTypeLabelList() {

        return TypeLabelList;
    }



    public void setTypeLabelList(List<LabelBean> typeLabelList) {

        TypeLabelList = typeLabelList;
    }



    @Override
    public String toString() {

        return "GoodsTypeLabelBean{" +
                "GoodsTypeList=" + GoodsTypeList +
                ", TypeLabelList=" + TypeLabelList +
                '}';
    }
}
