package com.hbbc.mshare;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/5.
 */
public class endStroke_bean implements Serializable{

    /*
    ECID:100122,					// 商户编号
    AppID:22,					//  app编号
    AppType:2,					// 客户端类型：1、共享端；2、用户端
    PhoneNumber:18210386513,	// 手机号码
    GoodsSNID:1000201,			// 物品编码
    CyclingTime:10			//骑行时间
    "OrderMoney":0.01,"Status":1,"Result":true,"GoodsSNID":"1000202","Notice":"操作成功"

   */
    private String GoodsSNID;

    private String CyclingTime;

    private int Status;

    private float OrderMoney;

    private  boolean Result;

    private String Notice;



    public float getOrderMoney() {

        return OrderMoney;
    }

    public void setOrderMoney(float  OrderMoney) {

       this. OrderMoney = OrderMoney;
    }


    public int getStatus() {

        return Status;
    }
    public void setStatus(int  Status) {

        this.Status = Status;
    }


    public boolean getResult() {

        return Result;
    }
    public void setResult(boolean  Result) {

        this.Result = Result;
    }


    public String getGoodsSNID() {

        return GoodsSNID;
    }



    public void setGoodsSNID(String GoodsSNID) {

        this.GoodsSNID = GoodsSNID;
    }

    public String getCyclingTime() {

        return CyclingTime;
    }



    public void CyclingTime(String CyclingTime) {

        this.CyclingTime = CyclingTime;
    }

    /*GoodsSNID":"1000202","Notice":"操作成功"*/
    public String getNotice(String Notice ){
        return Notice;
    }



    public void setNotice(String Notice) {

        this.Notice = Notice;
    }



    @Override
    public String toString() {
/*
        "OrderMoney":0.01,"Status":1,"Result":true,"GoodsSNID":"1000202","Notice":"操作成功"
*/
        return "enStroke{"+"OrderMoney"+OrderMoney+","+"Status"+Status+","+"Result"+Result+","+"GoodsSNID"+GoodsSNID+","+"Notice"+Notice+"}";
    }
}
