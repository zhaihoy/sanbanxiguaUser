package com.hbbc.mpush.bean;

import java.util.List;

/**
 * 未读消息提示数据
 */
public class ReadNoticeListBean {


    /**
     * Result : true
     * Notice : 操作成功
     * TotalUnReadNum : 0
     * UnReadNoticeList : [{"MTID":"1001","PicFileID":"http://q.jpg","TypeName":"系统通知","UnReadNum":"0"},{"MTID":"1001","PicFileID":"http://q.jpg","TypeName":"系统通知","UnReadNum":"0"}]
     */

    private boolean Result;

    private String Notice;

    private int TotalUnReadNum;

    /**
     * MTID : 1001
     * PicFileID : http://q.jpg
     * TypeName : 系统通知
     * UnReadNum : 0
     */

    private List<UnReadNoticeList> UnReadNoticeList;



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



    public int getTotalUnReadNum() {
        return TotalUnReadNum;
    }



    public void setTotalUnReadNum(int TotalUnReadNum) {
        this.TotalUnReadNum = TotalUnReadNum;
    }



    public List<UnReadNoticeList> getUnReadNoticeList() {
        return UnReadNoticeList;
    }



    public void setUnReadNoticeList(List<UnReadNoticeList> UnReadNoticeList) {
        this.UnReadNoticeList = UnReadNoticeList;
    }



    public static class UnReadNoticeList {
        private String MTID;

        private String PicFileID;

        private String TypeName;

        private String UnReadNum;



        public String getMTID() {
            return MTID;
        }



        public void setMTID(String MTID) {
            this.MTID = MTID;
        }



        public String getPicFileID() {
            return PicFileID;
        }



        public void setPicFileID(String PicFileID) {
            this.PicFileID = PicFileID;
        }



        public String getTypeName() {
            return TypeName;
        }



        public void setTypeName(String TypeName) {
            this.TypeName = TypeName;
        }



        public String getUnReadNum() {
            return UnReadNum;
        }



        public void setUnReadNum(String UnReadNum) {
            this.UnReadNum = UnReadNum;
        }
    }
}
