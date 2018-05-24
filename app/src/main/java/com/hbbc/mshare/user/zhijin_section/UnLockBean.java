package com.hbbc.mshare.user.zhijin_section;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/3.
 */
public class UnLockBean implements Serializable {
    private  boolean Result;

    private String Notice;




    public boolean getResult() {

        return Result;
    }
    public void setResult(boolean  Result) {

        this.Result = Result;
    }

    public String getNotice(){
        return Notice;
    }



    public void setNotice(String Notice) {

        this.Notice = Notice;
    }

}

