package com.hbbc.mnews.bean;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * 新闻分类接口实体类
 */
public class NewsTypeListBean implements Serializable {


    /**
     * Result : true
     * Notice : 操作成功
     * ListType : [{"NewsTypeID":"新闻分类ID","NewsTypeName":"分类名称","OrderNum":"排序编号"}]
     */

    private boolean Result;

    private String Notice;

    /**
     * NewsTypeID : 新闻分类ID
     * NewsTypeName : 分类名称
     * OrderNum : 排序编号
     */

    private List<ListTypeBean> ListType;



    public boolean isResult() {

        return Result;
    }



    public void setResult(boolean Result) {

        this.Result = Result;
    }


    @Nullable
    public String getNotice() {

        return Notice;
    }



    public void setNotice(String Notice) {

        this.Notice = Notice;
    }


    @Nullable
    public List<ListTypeBean> getListType() {

        return ListType;
    }



    public void setListType(List<ListTypeBean> ListType) {

        this.ListType = ListType;
    }



    public static class ListTypeBean {

        private String NewsTypeID;

        private String NewsTypeName;

        private String OrderNum;


        @Nullable
        public String getNewsTypeID() {

            return NewsTypeID;
        }


        public void setNewsTypeID(String NewsTypeID) {

            this.NewsTypeID = NewsTypeID;
        }


        @Nullable
        public String getNewsTypeName() {

            return NewsTypeName;
        }



        public void setNewsTypeName(String NewsTypeName) {

            this.NewsTypeName = NewsTypeName;
        }


        @Nullable
        public String getOrderNum() {

            return OrderNum;
        }



        public void setOrderNum(String OrderNum) {

            this.OrderNum = OrderNum;
        }
    }
}
