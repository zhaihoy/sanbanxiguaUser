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



    public static class OrderInfoBean {

        private String OrderID;
        private String OrderPayOrderID;
        private int GoodsSNID;
        private String SharerName;//下单人
        private String PhoneNumber;//下单人电话
        private String OrderBeginTime;//订单开始时间
        private int Status;//支付状态：1、待支付；2、支付成功；3、支付失败；4、支付取消；
        private String GoodsPic;
        private String OrderTime;
        private String UserName;//车主端使用
        private float GoodsUsePrice;//车主端使用



        public String getUserName() {

            return UserName;
        }



        public void setUserName(String userName) {

            UserName = userName;
        }



        public float getGoodsUsePrice() {

            return GoodsUsePrice;
        }



        public void setGoodsUsePrice(float goodsUsePrice) {

            GoodsUsePrice = goodsUsePrice;
        }



        public String getOrderPayOrderID() {

            return OrderPayOrderID;
        }



        public void setOrderPayOrderID(String orderPayOrderID) {

            OrderPayOrderID = orderPayOrderID;
        }



        public String getOrderTime() {

            return OrderTime;
        }



        public void setOrderTime(String orderTime) {

            OrderTime = orderTime;
        }



        public int getGoodsSNID() {

            return GoodsSNID;
        }



        public int getStatus() {

            return Status;
        }



        public void setGoodsSNID(int goodsSNID) {

            GoodsSNID = goodsSNID;
        }



        public String getSharerName() {

            return SharerName;
        }



        public void setSharerName(String sharerName) {

            SharerName = sharerName;
        }



        public void setStatus(int status) {

            Status = status;
        }



        public String getGoodsPic() {

            return GoodsPic;
        }



        public void setGoodsPic(String goodsPic) {

            GoodsPic = goodsPic;
        }



        public String getOrderID() {

            return OrderID;
        }



        public void setOrderID(String orderID) {

            OrderID = orderID;
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

    }
}
