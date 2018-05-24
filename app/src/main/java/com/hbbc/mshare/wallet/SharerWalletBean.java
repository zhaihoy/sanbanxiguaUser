package com.hbbc.mshare.wallet;

import com.hbbc.mshare.login.BaseResultBean;

/**
 * Created by Administrator on 2017/11/8.
 */
public class SharerWalletBean extends BaseResultBean {

    public String SharerName;
    public String SharerBalance;
    public String DepositBalance;



    public String getSharerName() {

        return SharerName;
    }



    public void setSharerName(String sharerName) {

        SharerName = sharerName;
    }



    public String getSharerBalance() {

        return SharerBalance;
    }



    public void setSharerBalance(String sharerBalance) {

        SharerBalance = sharerBalance;
    }



    public String getDepositBalance() {

        return DepositBalance;
    }



    public void setDepositBalance(String depositBalance) {

        DepositBalance = depositBalance;
    }
}
