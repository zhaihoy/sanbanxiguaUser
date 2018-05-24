package com.hbbc.util;

/**
 * Created by Administrator on 2017/8/2.
 * mshare模块公共常量类
 */
public class Constants {

    public static final String ECID="ECID";
    public static final String PhoneNumber="PhoneNumber";
    public static final String TempVerifyCode="TempVerifyCode";
    public static final String AppType="AppType";

    // 开启实名认证
    public static final String OpenCertification="OpenCertification";
    // 开启押金
    public static final String OpenrDesposit="OpenDesposit";
    // 押金数
    public static final String DespositCount="DepositCount";
    //自动登陆的密钥
    public static final String AutoLoginKey="AutoLoginKey";
    //默认开启自动登陆的标志字段
    public static final String isCurrentUser="isCurrentUser";

    public static final String ID = "CertificationNumber";

    //用户的真实姓名和身份证号是不存储在客户端数据库的!
    public static final String RealName = "RealName";

    public static final String VerificationState = "VerificationState";

    public static final String CSFeedback = "CSFeedback";

    public static final String GoodsSNID = "GoodsSNID";

    public static final String Lat="Lat";
    public static final String Lng="Lng";

    public static final String INTRODUCTION = "GoodsIntroduceText";

    public static final String BUSSINESS_TYPE = "BusinessType";

    public static final String USAGE_PRICE = "GoodsUsePrice";

    public static final String GOODS_DEPOSIT = "GoodsDeposit";

    public static final String LABEL_CONTENT = "LabelContent";

    public static final String PicList = "PicList";

    public static final String WithdrawType = "WithdrawalsType";

    public static final String WithdrawAccountID = "WithdrawalsAccountID";

    public static final String WithdrawAmount = "WithdrawalsAmount";

    public static final String GOODSTYPEID = "GoodsTypeID";

    public static final String PayType = "PayType";

    public static final String OrderAmount = "OrderAmount";

    public static final String WEIXIN_PAY_RESULT = "weixin_pay_result";//接收到微信支付结果时,发广播用到

    public static final String PayOrderID = "PayOrderID";
}
