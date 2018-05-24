package com.hbbc.mshare.wallet;

import com.hbbc.mshare.login.BaseResultBean;

/**
 * Created by Administrator on 2017/11/8.
 *
 */
public class UserWalletBean extends BaseResultBean{

    public String UserName;
    public String UserBalance;
    public String DepositBalance;

    public String getUserName() {

        return UserName;
    }



    public void setUserName(String userName) {

        UserName = userName;
    }



    public String getUserBalance() {

        return UserBalance;
    }



    public void setUserBalance(String userBalance) {

        UserBalance = userBalance;
    }



    public String getDepositBalance() {

        return DepositBalance;
    }



    public void setDepositBalance(String depositBalance) {

        DepositBalance = depositBalance;
    }


}
