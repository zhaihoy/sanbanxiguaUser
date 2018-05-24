package com.hbbc.mshare.user.zhijin_section;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/29.
 */
public class ResultBeans implements Serializable{
    private boolean Result;

    private String Notice;


    public boolean getResult() {

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

}
