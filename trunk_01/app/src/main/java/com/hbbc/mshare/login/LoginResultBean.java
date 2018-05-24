package com.hbbc.mshare.login;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/1.
 *
 */
@DatabaseTable(tableName = "login_result_bean")
public class LoginResultBean extends BaseResultBean implements Serializable {

    public LoginResultBean(){
    }

    public LoginResultBean(boolean result, String notice) {

        super(result, notice);
    }

    // 开启实名认证：1、开启；2、关闭；
    @DatabaseField
    protected int OpenCertification;
    // 开启押金：1、开启；2、关闭；
    @DatabaseField
    protected int OpenDesposit;
    /*// 押金数（单位：元）
    @DatabaseField
    protected int DespositCount;*/
    //自动登陆的密钥
    @DatabaseField
    protected String AutoLoginKey;
    //对应的手机号
    @DatabaseField
    protected String PhoneNumber;
    //Caution: 当前用户是否是默认自动登陆用户
    @DatabaseField
    protected boolean isCurrentUser;

    //交纳押金状态值：1、需要缴纳押金2、不需要
    @DatabaseField
    protected int DespositStatus;//标识当前用户的押金支付状态,如果需要交的话!
    //需要交纳的押金数（单位：元）
    @DatabaseField
    protected float PayDespositNum;
    @DatabaseField
    protected int CertificationStatus;//标识当前用户的认证状态



    public int getCertificationStatus() {

        return CertificationStatus;
    }



    public void setCertificationStatus(int certificationStatus) {

        CertificationStatus = certificationStatus;
    }



    public String getPhoneNumber() {

        return PhoneNumber;
    }



    public void setPhoneNumber(String phoneNumber) {

        PhoneNumber = phoneNumber;
    }



    public boolean isCurrentUser() {

        return isCurrentUser;
    }



    public void setCurrentUser(boolean currentUser) {

        isCurrentUser = currentUser;
    }



    public String getAutoLoginKey() {

        return AutoLoginKey;
    }



    public void setAutoLoginKey(String autoLoginKey) {

        AutoLoginKey = autoLoginKey;
    }



    public int getOpenCertification() {

        return OpenCertification;
    }



    public void setOpenCertification(int openCertification) {

        OpenCertification = openCertification;
    }



    public int getOpenDesposit() {

        return OpenDesposit;
    }



    public void setOpenDesposit(int openDesposit) {

        OpenDesposit = openDesposit;
    }



    public int getDespositStatus() {

        return DespositStatus;
    }



    public void setDespositStatus(int despositStatus) {

        DespositStatus = despositStatus;
    }



    public float getPayDespositNum() {

        return PayDespositNum;
    }



    public void setPayDespositNum(float payDespositNum) {

        PayDespositNum = payDespositNum;
    }



    @Override
    public String toString() {

        return "LoginResultBean{" +
                "OpenCertification=" + OpenCertification +
                ", OpenDesposit=" + OpenDesposit +
                ", AutoLoginKey='" + AutoLoginKey + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", isCurrentUser=" + isCurrentUser +
                ", DespositStatus=" + DespositStatus +
                ", PayDespositNum=" + PayDespositNum +
                ", CertificationStatus=" + CertificationStatus +
                '}';
    }
}
