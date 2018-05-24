package com.hbbc.mshare.sharer;

import com.hbbc.mshare.login.BaseResultBean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/25.
 *
 */
public class OrderResultBean extends BaseResultBean {

    private List<OrderInfoBean> OrderList;



    public List<OrderInfoBean> getOrderList() {

        return OrderList;
    }



    public void setOrderList(List<OrderInfoBean> orderList) {

        OrderList = orderList;
    }



    public class OrderInfoBean {

        private String OrderID;
        private String GoodsSNID;
        private String UserName;//下单人
        private String PhoneNumber;//下单人电话
        private String OrderBeginTime;//订单开始时间
        private String Status;//支付状态：1、待支付；2、支付成功；3、支付失败；4、支付取消；



        public String getOrderID() {

            return OrderID;
        }



        public void setOrderID(String orderID) {

            OrderID = orderID;
        }



        public String getGoodsSNID() {

            return GoodsSNID;
        }



        public void setGoodsSNID(String goodsSNID) {

            GoodsSNID = goodsSNID;
        }



        public String getUserName() {

            return UserName;
        }



        public void setUserName(String userName) {

            UserName = userName;
        }



        public String getPhoneNumber() {

            return PhoneNumber;
        }



        public void setPhoneNumber(String phoneNumber) {

            PhoneNumber = phoneNumber;
        }



        public String getOrderBeginTime() {

            return OrderBeginTime;
        }



        public void setOrderBeginTime(String orderBeginTime) {

            OrderBeginTime = orderBeginTime;
        }



        public String getStatus() {

            return Status;
        }



        public void setStatus(String status) {

            Status = status;
        }
    }
}
