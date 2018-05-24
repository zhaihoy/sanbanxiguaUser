package com.hbbc.mshare.sharer.authentication;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/7.
 */
public class BillBean implements Serializable{
    /**
     * Result : true
     * Notice : 操作成功
     * BillSet : [{"billDetialsType":"2","billId":"R_Wx_290_20171130140146","billMoney":"0.01","billTime":"2017-11-30 14:01:46","billType":"2"}]
     */

    private boolean Result;
    private String Notice;
    private List<BillSetBean> BillSet;

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean Result) {
        this.Result = Result;
    }

    public String getNotice() {
        return Notice;
    }

    public void setNotice(String Notice) {
        this.Notice = Notice;
    }

    public List<BillSetBean> getBillSet() {
        return BillSet;
    }

    public void setBillSet(List<BillSetBean> BillSet) {
        this.BillSet = BillSet;
    }

    public static class BillSetBean  implements Serializable{
        /**
         * billDetialsType : 2
         * billId : R_Wx_290_20171130140146
         * billMoney : 0.01
         * billTime : 2017-11-30 14:01:46
         * billType : 2
         */

        private String billDetialsType;
        private String billId;
        private String billMoney;
        private String billTime;
        private String billType;

        public String getBillDetialsType() {
            return billDetialsType;
        }

        public void setBillDetialsType(String billDetialsType) {
            this.billDetialsType = billDetialsType;
        }

        public String getBillId() {
            return billId;
        }

        public void setBillId(String billId) {
            this.billId = billId;
        }

        public String getBillMoney() {
            return billMoney;
        }

        public void setBillMoney(String billMoney) {
            this.billMoney = billMoney;
        }

        public String getBillTime() {
            return billTime;
        }

        public void setBillTime(String billTime) {
            this.billTime = billTime;
        }

        public String getBillType() {
            return billType;
        }

        public void setBillType(String billType) {
            this.billType = billType;
        }
    }

}
