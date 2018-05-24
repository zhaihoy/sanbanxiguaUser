package com.hbbc.mshare.user;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/7.
 */
public class UserBean implements Serializable {
    /*	" Result": true 							// 执行成功返回true,否则返回错误类型
  	," Notice":"操作成功"						// 说明。错误时会显示详细错误信息
	,” UserName”:”赵三”						//用户姓名
	,” UserBalance”:”100”						//用户余额
	,” DepositBalance”:”200”					//用户押金*/
    private String Result;
    private String Notice;
    private String UserName;
    private String UserBalance;
    private String DepositBalance;
    public String getResult() {

        return Result;
    }

    public void setResult(String Result) {

        this.Result = Result;
    }
    public String getNotice() {

        return Notice;
    }

    public void setNotice(String Notice) {

        this.Notice = Notice;
    }
    public String getUserName() {

        return UserName;
    }

    public void setUserName(String UserName) {

        this.UserName = UserName;
    }  public String getUserBalance() {

        return UserBalance;
    }

    public void setUserBalance(String UserBalance) {

        this.UserBalance = UserBalance;
    }
    public String getDepositBalance() {

        return DepositBalance;
    }

    public void setDepositBalance(String DepositBalance) {

        this.DepositBalance = DepositBalance;
    }
}
