package com.hbbc.mshare;

import com.hbbc.mshare.login.BaseResultBean;

/**
 * Created by Administrator on 2017/9/13.
 *
 */
public class SignedOrderBean extends BaseResultBean {

    private String OrderString;
    private String PayType;
    private String PayOrderID;
    private String OrderID;



    public String getOrderID() {

        return OrderID;
    }



    public void setOrderID(String orderID) {

        OrderID = orderID;
    }



    public String getPayOrderID() {

        return PayOrderID;
    }



    public void setPayOrderID(String payOrderID) {

        PayOrderID = payOrderID;
    }



    public String getPayType() {

        return PayType;
    }



    public void setPayType(String payType) {

        PayType = payType;
    }



    public String getOrderString() {

        return OrderString;
    }



    public void setOrderString(String orderString) {

        OrderString = orderString;
    }
}
