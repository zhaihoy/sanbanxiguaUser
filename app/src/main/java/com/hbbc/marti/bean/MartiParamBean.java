package com.hbbc.marti.bean;

import java.io.Serializable;

/**
 * 文章模块基本信息
 */
public class MartiParamBean implements Serializable {

    /**
     * Result : true
     * Notice : 操作成功
     * ArticleID : 1001
     * ModuleName : 新闻模块
     */

    private boolean Result;

    private String Notice;

    private int ArticleID;

    private String ModuleName;



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



    public int getArticleID() {

        return ArticleID;
    }



    public void setArticleID(int articleID) {

        ArticleID = (articleID > -1) ? articleID : 0;
    }



    public String getModuleName() {

        return ModuleName;
    }



    public void setModuleName(String moduleName) {

        ModuleName = (moduleName != null) ? moduleName : "";
    }
}
