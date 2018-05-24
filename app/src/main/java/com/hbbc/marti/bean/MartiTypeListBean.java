package com.hbbc.marti.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 文章类型实体类
 */
public class MartiTypeListBean implements Serializable {

    /**
     * Result : true
     * Notice : 操作成功
     * TypeList : [{"ArticleTypeID":"文章分类ID","ArticleTypeName":"文章分类名称","OrderNum ":"排序编号"}]
     */

    private boolean Result;

    private String Notice;

    /**
     * ArticleTypeID : 文章分类ID
     * ArticleTypeName : 文章分类名称
     * OrderNum  : 排序编号
     */

    private List<TypeListBean> TypeList;



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



    public List<TypeListBean> getTypeList() {

        return TypeList;
    }



    public void setTypeList(List<TypeListBean> typeList) {

        TypeList = typeList;
    }



    public static class TypeListBean {

        private String ArticleTypeID;

        private String ArticleTypeName;

        private String OrderNum;



        public String getArticleTypeID() {

            return ArticleTypeID;
        }



        public void setArticleTypeID(String articleTypeID) {

            ArticleTypeID = articleTypeID;
        }



        public String getArticleTypeName() {

            return ArticleTypeName;
        }



        public void setArticleTypeName(String articleTypeName) {

            ArticleTypeName = articleTypeName;
        }



        public String getOrderNum() {

            return OrderNum;
        }



        public void setOrderNum(String orderNum) {

            OrderNum = orderNum;
        }
    }
}
