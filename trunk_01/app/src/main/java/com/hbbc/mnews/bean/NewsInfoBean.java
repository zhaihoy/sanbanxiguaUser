package com.hbbc.mnews.bean;

import java.io.Serializable;

/**
 * 新闻详情实体类
 */
public class NewsInfoBean implements Serializable {


    /**
     * Result : true
     * Notice : 操作成功
     * HeadLine : 标题
     * Content : 新闻内容
     * ContentPicFileID : 新闻图片
     * PublishTime : 发布时间
     * NewsTypeName : 新闻通报
     */

    private boolean Result;

    private String Notice;

    private String Headline;

    private String Content;

    private String ContentPicFileID;

    private String PublishTime;

    private String NewsTypeName;



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



    public String getHeadline() {

        return Headline;
    }



    public void setHeadLine(String HeadLine) {

        this.Headline = HeadLine;
    }



    public String getContent() {

        return Content;
    }



    public void setContent(String Content) {

        this.Content = (Content != null) ? Content : "";
    }



    public String getContentPicFileID() {

        return ContentPicFileID;
    }



    public void setContentPicFileID(String ContentPicFileID) {

        this.ContentPicFileID = (ContentPicFileID != null) ? ContentPicFileID : "";
    }



    public String getPublishTime() {

        return PublishTime;
    }



    public void setPublishTime(String PublishTime) {

        this.PublishTime = (PublishTime != null) ? PublishTime : "";
    }



    public String getNewsTypeName() {

        return NewsTypeName;
    }



    public void setNewsTypeName(String NewsTypeName) {

        this.NewsTypeName = (NewsTypeName != null) ? NewsTypeName : "";
    }
}
