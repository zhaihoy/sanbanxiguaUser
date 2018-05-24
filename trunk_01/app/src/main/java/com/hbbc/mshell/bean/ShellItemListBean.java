package com.hbbc.mshell.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dkx on 2016/10/25.
 */
public class ShellItemListBean implements Serializable{


    /**
     * Result : true
     * Notice : 操作成功
     * ShellItemList : [{"ItemName":"风险自测","ItemPicFileID":"http://asset/distfile/dfssdf.jsp","ItemURL":"module://marti/323","OrderNum":"1","SIID":1}]
     */

    private boolean Result;
    private String Notice;
    /**
     * ItemName : 风险自测
     * ItemPicFileID : http://asset/distfile/dfssdf.jsp
     * ItemURL : module://marti/323
     * OrderNum : 1
     * SIID : 1
     */

    private List<ShellItemList> ShellItemList;

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

    public List<ShellItemList> getShellItemList() {
        return ShellItemList;
    }

    public void setShellItemList(List<ShellItemList> ShellItemList) {
        this.ShellItemList = ShellItemList;
    }

    public static class ShellItemList {
        private String ItemName;
        private String ItemPicFileID;
        private String ItemURL;
        private String OrderNum;
        private int SIID;

        public String getItemName() {
            return ItemName;
        }

        public void setItemName(String ItemName) {
            this.ItemName = ItemName;
        }

        public String getItemPicFileID() {
            return ItemPicFileID;
        }

        public void setItemPicFileID(String ItemPicFileID) {
            this.ItemPicFileID = ItemPicFileID;
        }

        public String getItemURL() {
            return ItemURL;
        }

        public void setItemURL(String ItemURL) {
            this.ItemURL = ItemURL;
        }

        public String getOrderNum() {
            return OrderNum;
        }

        public void setOrderNum(String OrderNum) {
            this.OrderNum = OrderNum;
        }

        public int getSIID() {
            return SIID;
        }

        public void setSIID(int SIID) {
            this.SIID = SIID;
        }
    }
}
