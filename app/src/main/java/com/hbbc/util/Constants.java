package com.hbbc.util;

/**
 * Created by Administrator on 2017/8/2.
 * mshare模块公共常量类
 */
public class Constants {
    public static final String OpenID="OpenID";
    public static final String ECID="ECID";
    public static final String AppID="AppID";
    public static final String PhoneNumber="PhoneNumber";
    public static final String TempVerifyCode="TempVerifyCode";
    public static final String AppType="AppType";
    public static final String CyclingTime="CyclingTime";
    public static final String OrderMoney="OrderMoney";


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

    public static final String  CurrentTime="CurrentTime";

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

    public static final String Type = "type";//指明是押金支付还是业务支付,以便在服务中进行区分,是否需要修改数据库中已登陆用户信息!

    public static final String LatLng = "LatLng";

    public static final String GoodsName = "GoodsName";

    public static final String CurrentPosition = "CurrentPosition";

    public static final String OperationType = "OperationType";

    public static final String isProcessing = "isProcessing";

    public static final String OrderPayOrderID = "OrderPayOrderID";

    public static final String GoodsList = "GoodsList";

    public static String RechargeAmount="RechargeAmount";

    public static String payMoney="PayMoney";

    public static String OrderType="OrderType";

    public static String OrderID="OrderID";
}
