package com.hbbc.marti.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 文章条目实体类
 */
public class MartiListBeans implements Serializable {

    /**
     * Result : true
     * Notice : 操作成功
     * ArticleList : [{"ArticleContentID":1,"Headline":"标题","Digest":"摘要","OrderNum":1,"PicFileID":"文章图片"}]
     */

    private boolean Result;

    private String Notice;

    /**
     * ArticleContentID : 1
     * Headline : 标题
     * Digest : 摘要
     * OrderNum : 1
     * PicFileID : 文章图片
     */

    private List<ArticleListBean> ArticleList;



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



    public List<ArticleListBean> getArticleList() {

        return ArticleList;
    }



    public void setArticleList(List<ArticleListBean> articleList) {

        ArticleList = articleList;
    }



    public static class ArticleListBean {

        private int ArticleContentID;

        private String Headline;

        private String Digest;

        private int OrderNum;

        private String PicFileID;



        public int getArticleContentID() {

            return ArticleContentID;
        }



        public void setArticleContentID(int articleContentID) {

            ArticleContentID = (articleContentID > -1) ? articleContentID : articleContentID;
        }



        public String getHeadline() {

            return Headline;
        }



        public void setHeadline(String headline) {

            Headline = (headline != null) ? headline : "";
        }



        public String getDigest() {

            return Digest;
        }



        public void setDigest(String digest) {

            Digest = (digest != null) ? digest : "";
        }



        public int getOrderNum() {

            return OrderNum;
        }



        public void setOrderNum(int orderNum) {

            OrderNum = (orderNum > -1) ? orderNum : 0;
        }



        public String getPicFileID() {

            return PicFileID;
        }



        public void setPicFileID(String picFileID) {

            PicFileID = (picFileID != null) ? picFileID : "";
        }
    }
}
