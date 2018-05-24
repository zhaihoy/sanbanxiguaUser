package com.hbbc.mshare;

import com.hbbc.mshare.login.BaseResultBean;

/**
 * Created by Administrator on 2017/11/1.
 */
public class AboutUsBean extends BaseResultBean{

    private String APPLogoPicFileID;
    private String WXNumber;
    private String ContactNumber;
    private String MailAddress;
    private String OfficeAddress;



    public String getAPPLogoPicFileID() {

        return APPLogoPicFileID;
    }



    public void setAPPLogoPicFileID(String APPLogoPicFileID) {

        this.APPLogoPicFileID = APPLogoPicFileID;
    }



    public String getWXNumber() {

        return WXNumber;
    }



    public void setWXNumber(String WXNumber) {

        this.WXNumber = WXNumber;
    }



    public String getContactNumber() {

        return ContactNumber;
    }



    public void setContactNumber(String contactNumber) {

        ContactNumber = contactNumber;
    }



    public String getMailAddress() {

        return MailAddress;
    }



    public void setMailAddress(String mailAddress) {

        MailAddress = mailAddress;
    }



    public String getOfficeAddress() {

        return OfficeAddress;
    }



    public void setOfficeAddress(String officeAddress) {

        OfficeAddress = officeAddress;
    }
}
