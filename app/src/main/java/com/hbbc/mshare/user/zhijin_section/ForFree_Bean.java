package com.hbbc.mshare.user.zhijin_section;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/28.
 */
//用于接受来自网络上的信息的bean，这是免费取纸的数据
public class ForFree_Bean implements Serializable {
    private  boolean Result;

    private String Notice;

    private String OrderID;


    public boolean getResult() {

        return Result;
    }
    public void setResult(boolean  Result) {

        this.Result = Result;
    }

    public String getNotice(String Notice ){
        return Notice;
    }



    public void setNotice(String Notice) {

        this.Notice = Notice;
    }

    public String getOrderID(){
        return OrderID;
    }



    public void setOrderID(String OrderID) {

        this.OrderID = OrderID;
    }


}
