package com.hbbc.mpush.bean;

import java.util.List;

/**
 * 未读消息列表
 */
public class MessageListBean {


    /**
     * Result : true
     * Notice : 操作成功
     * UnReadMessageList : [{"MIID":"1213","PicFileID":"http://e.jpg","ItemName":"今日新闻","MTMID":"11212","MsgTitle":"标题","MsgUrl":"http://Module/qdjc/1101","SendTime":"2016-11-02 12:12:32","Status":9}]
     * ReadMessageList : [{"MIID":"1213","PicFileID":"http://e.jpg","ItemName":"今日新闻","MTMID":"11212","MsgTitle":"标题","MsgUrl":"http://Module/qdjc/1101","SendTime":"2016-11-02 12:12:32","Status":9}]
     */

    private String Result;

    private String Notice;

    /**
     * MIID : 1213
     * PicFileID : http://e.jpg
     * ItemName : 今日新闻
     * MTMID : 11212
     * MsgTitle : 标题
     * MsgUrl : http://Module/qdjc/1101
     * SendTime : 2016-11-02 12:12:32
     * Status : 9
     */

    private List<UnReadMessageList> UnReadMessageList;

    /**
     * MIID : 1213
     * PicFileID : http://e.jpg
     * ItemName : 今日新闻
     * MTMID : 11212
     * MsgTitle : 标题
     * MsgUrl : http://Module/qdjc/1101
     * SendTime : 2016-11-02 12:12:32
     * Status : 9
     */

    private List<ReadMessageList> ReadMessageList;



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



    public List<UnReadMessageList> getUnReadMessageList() {
        return UnReadMessageList;
    }



    public void setUnReadMessageList(List<UnReadMessageList> UnReadMessageList) {
        this.UnReadMessageList = UnReadMessageList;
    }



    public List<ReadMessageList> getReadMessageList() {
        return ReadMessageList;
    }



    public void setReadMessageList(List<ReadMessageList> ReadMessageList) {
        this.ReadMessageList = ReadMessageList;
    }



    public static class UnReadMessageList {
        private String MIID;

        private String PicFileID;

        private String ItemName;

        private String MTMID;

        private String MsgTitle;

        private String MsgUrl;

        private String SendTime;

        private int Status;



        public String getMIID() {
            return MIID;
        }



        public void setMIID(String MIID) {
            this.MIID = MIID;
        }



        public String getPicFileID() {
            return PicFileID;
        }



        public void setPicFileID(String PicFileID) {
            this.PicFileID = PicFileID;
        }



        public String getItemName() {
            return ItemName;
        }



        public void setItemName(String ItemName) {
            this.ItemName = ItemName;
        }



        public String getMTMID() {
            return MTMID;
        }



        public void setMTMID(String MTMID) {
            this.MTMID = MTMID;
        }



        public String getMsgTitle() {
            return MsgTitle;
        }



        public void setMsgTitle(String MsgTitle) {
            this.MsgTitle = MsgTitle;
        }



        public String getMsgUrl() {
            return MsgUrl;
        }



        public void setMsgUrl(String MsgUrl) {
            this.MsgUrl = MsgUrl;
        }



        public String getSendTime() {
            return SendTime;
        }



        public void setSendTime(String SendTime) {
            this.SendTime = SendTime;
        }



        public int getStatus() {
            return Status;
        }



        public void setStatus(int Status) {
            this.Status = Status;
        }
    }

    public static class ReadMessageList {
        private String MIID;

        private String PicFileID;

        private String ItemName;

        private String MTMID;

        private String MsgTitle;

        private String MsgUrl;

        private String SendTime;

        private int Status;



        public String getMIID() {
            return MIID;
        }



        public void setMIID(String MIID) {
            this.MIID = MIID;
        }



        public String getPicFileID() {
            return PicFileID;
        }



        public void setPicFileID(String PicFileID) {
            this.PicFileID = PicFileID;
        }



        public String getItemName() {
            return ItemName;
        }



        public void setItemName(String ItemName) {
            this.ItemName = ItemName;
        }



        public String getMTMID() {
            return MTMID;
        }



        public void setMTMID(String MTMID) {
            this.MTMID = MTMID;
        }



        public String getMsgTitle() {
            return MsgTitle;
        }



        public void setMsgTitle(String MsgTitle) {
            this.MsgTitle = MsgTitle;
        }



        public String getMsgUrl() {
            return MsgUrl;
        }



        public void setMsgUrl(String MsgUrl) {
            this.MsgUrl = MsgUrl;
        }



        public String getSendTime() {
            return SendTime;
        }



        public void setSendTime(String SendTime) {
            this.SendTime = SendTime;
        }



        public int getStatus() {
            return Status;
        }



        public void setStatus(int Status) {
            this.Status = Status;
        }
    }
}
