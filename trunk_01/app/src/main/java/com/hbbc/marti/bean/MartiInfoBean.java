package com.hbbc.marti.bean;

import java.io.Serializable;

/**
 * 文章详情实体类
 */
public class MartiInfoBean implements Serializable {

    /**
     * Result : true
     * Notice : 操作成功
     * Headline : 标题
     * Content : 文章内容
     * ContentPicFileID  : 文章图片
     * PublishTime : 发布时间
     * ArticleTypeName : 文章类别
     */

    private boolean Result;

    private String Notice;

    private String Headline;

    private String Content;

    private String ContentPicFileID;

    private String PublishTime;

    private String ArticleTypeName;



    public boolean isResult() {

        return Result;
    }



    public void setResult(boolean result) {

        Result = result;
    }



    public String getNotice() {

        return Notice;
    }



    public void setNotice(String notice) {

        Notice = notice;
    }



    public String getHeadline() {

        return Headline;
    }



    public void setHeadline(String headline) {

        Headline = headline;
    }



    public String getContent() {

        return Content;
    }



    public void setContent(String content) {

        Content = content;
    }



    public String getContentPicFileID() {

        return ContentPicFileID;
    }



    public void setContentPicFileID(String contentPicFileID) {

        ContentPicFileID = contentPicFileID;
    }



    public String getPublishTime() {

        return PublishTime;
    }



    public void setPublishTime(String publishTime) {

        PublishTime = publishTime;
    }



    public String getArticleTypeName() {

        return ArticleTypeName;
    }



    public void setArticleTypeName(String articleTypeName) {

        ArticleTypeName = articleTypeName;
    }
}
