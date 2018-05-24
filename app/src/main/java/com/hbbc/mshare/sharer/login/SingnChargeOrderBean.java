package com.hbbc.mshare.sharer.login;

import com.hbbc.mshare.login.BaseResultBean;

/**
 * Created by Administrator on 2017/12/8.
 */
public class SingnChargeOrderBean extends BaseResultBean {
    /**
     * PayOrderID : R_Wx_376_20171208093746
     * Result : true
     * Notice : 操作成功
     * OrderString : {"sign":"037D896BB8D12FABB90DB93E0DA6E014","timestamp":"1512725866","noncestr":"1718","partnerid":"1489749712","prepayid":"wx201712080937296424ccf0ef0041858191","package":"Sign=WXPay","appid":"wx183bdd2a249aafa2"}
     */

    private String PayOrderID;

    private String OrderString;

    public String getPayOrderID() {
        return PayOrderID;
    }

    public void setPayOrderID(String PayOrderID) {
        this.PayOrderID = PayOrderID;
    }




    public String getOrderString() {
        return OrderString;
    }

    public void setOrderString(String OrderString) {
        this.OrderString = OrderString;
    }


}
