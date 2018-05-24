package com.hbbc.mnews.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 新闻列表实体类
 */
public class NewsListBeans implements Serializable {


    /**
     * Result : true
     * NewsList : [{"PublishTime":"2016-12-03 10:32:37.0","OrderNum":1,"Digest":"东四街道禁毒骨干来康复所参观学习","NewsContentID":101,"Headline":"东四街道禁毒骨干来康复所参观学习","PicFileID":"http://192.168.1.176:80/pgynbo/asset/distfile/f539dc5a-987e-4daf-9157-816a168804dc.jpg"}]
     * Notice : 操作成功
     * BannerNewsList : [{"PublishTime":"2016-12-06 15:18:52.0","OrderNum":2,"Digest":"天康戒毒康复所各支部组织开展主题党日活动","NewsContentID":102,"PicFileID":""},{"PublishTime":"2016-12-06 16:08:59.0","OrderNum":3,"Digest":"天康戒毒康复所各支部组织开展主题党日活动","NewsContentID":103,"PicFileID":"http://192.168.1.176:80/pgynbo/asset/distfile/f539dc5a-987e-4daf-9157-816a168804dc.jpg"}]
     */

    private boolean Result;

    private String Notice;

    /**
     * PublishTime : 2016-12-03 10:32:37.0
     * OrderNum : 1
     * Digest : 东四街道禁毒骨干来康复所参观学习
     * NewsContentID : 101
     * Headline : 东四街道禁毒骨干来康复所参观学习
     * PicFileID : http://192.168.1.176:80/pgynbo/asset/distfile/f539dc5a-987e-4daf-9157-816a168804dc.jpg
     */

    private List<NewsListBean> NewsList;

    /**
     * PublishTime : 2016-12-06 15:18:52.0
     * OrderNum : 2
     * Digest : 天康戒毒康复所各支部组织开展主题党日活动
     * NewsContentID : 102
     * PicFileID :
     */

    private List<BannerNewsListBean> BannerNewsList;



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

        this.Notice = (Notice != null) ? Notice : "";
    }



    public List<NewsListBean> getNewsList() {

        return NewsList;
    }



    public void setNewsList(List<NewsListBean> NewsList) {

        this.NewsList = (NewsList != null) ? NewsList : null;
    }



    public List<BannerNewsListBean> getBannerNewsList() {

        return BannerNewsList;
    }



    public void setBannerNewsList(List<BannerNewsListBean> BannerNewsList) {

        this.BannerNewsList = (BannerNewsList != null) ? BannerNewsList : null;
    }



    public static class NewsListBean {

        private String PublishTime;

        private int OrderNum;

        private String Digest;

        private int NewsContentID;

        private String Headline;

        private String PicFileID;



        public String getPublishTime() {

            return PublishTime;
        }



        public void setPublishTime(String PublishTime) {

            this.PublishTime = (PublishTime != null) ? PublishTime : "";
        }



        public int getOrderNum() {

            return OrderNum;
        }



        public void setOrderNum(int OrderNum) {

            this.OrderNum = (OrderNum != -1) ? OrderNum : 0;
        }



        public String getDigest() {

            return Digest;
        }



        public void setDigest(String Digest) {

            this.Digest = (Digest != null) ? Digest : "";
        }



        public int getNewsContentID() {

            return NewsContentID;
        }



        public void setNewsContentID(int NewsContentID) {

            this.NewsContentID = (NewsContentID != -1) ? NewsContentID : 0;
        }



        public String getHeadline() {

            return Headline;
        }



        public void setHeadline(String Headline) {

            this.Headline = (Headline != null) ? Headline : "";
        }



        public String getPicFileID() {

            return PicFileID;
        }



        public void setPicFileID(String PicFileID) {

            this.PicFileID = (PicFileID != null) ? PicFileID : "";
        }
    }

    public static class BannerNewsListBean {

        private String PublishTime;

        private int OrderNum;

        private String Headline;

        private int NewsContentID;

        private String PicFileID;



        public String getPublishTime() {

            return PublishTime;
        }



        public void setPublishTime(String PublishTime) {

            this.PublishTime = (PublishTime != null) ? PublishTime : "";
        }



        public int getOrderNum() {

            return OrderNum;
        }



        public void setOrderNum(int OrderNum) {

            this.OrderNum = (OrderNum != -1) ? OrderNum : 0;
        }



        public String getHeadline() {

            return Headline;
        }



        public void setHeadline(String Headline) {

            this.Headline = (Headline != null) ? Headline : "";
        }



        public int getNewsContentID() {

            return NewsContentID;
        }



        public void setNewsContentID(int NewsContentID) {

            this.NewsContentID = (NewsContentID != -1) ? NewsContentID : 0;
        }



        public String getPicFileID() {

            return PicFileID;
        }



        public void setPicFileID(String PicFileID) {

            this.PicFileID = (PicFileID != null) ? PicFileID : "";
        }
    }
}
