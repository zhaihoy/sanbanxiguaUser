package com.hbbc.mshare.login;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/1.
 *
 */
public class BaseResultBean implements Serializable{

    public BaseResultBean(){
    }

    @DatabaseField(generatedId = true)
    protected int id;

    @DatabaseField
    protected boolean Result;

    @DatabaseField
    protected String Notice;

    public BaseResultBean(boolean result, String notice) {

        Result = result;
        Notice = notice;
    }



    public int getId() {

        return id;
    }



    public void setId(int id) {

        this.id = id;
    }



    public boolean isResult() {

        return Result;
    }



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



    @Override
    public String toString() {

        return "BaseResultBean{" +
                "id=" + id +
                ", Result=" + Result +
                ", Notice='" + Notice + '\'' +
                '}';
    }
}
