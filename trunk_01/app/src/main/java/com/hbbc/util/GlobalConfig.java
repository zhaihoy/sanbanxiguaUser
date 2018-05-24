package com.hbbc.util;

import android.content.Context;

public class GlobalConfig {

    /**
     * 是否打印Debug日志信息。为True表示打印，为False标识不打印，只打印Info和Exception信息到日志文件。
     */
    public static final boolean LogUtil_DebugFlag = true;

    /**
     * 是否存储日志信息到日志文件。为True时候存储，为False时候不存储。 注：日志文件包括2个, log.log 记录 debug, info,
     * exception 等信息。 exception.log 记录异常信息。 日志文件的位置在 LogUtil_SaveLogDir 中设置
     */
    public static final boolean LogUtil_SaveLogFile = false;


    /**线上 服务器端口的根路径*/
    //public static final String SERVERROOTPATH="http://ppgyn.handbbc.com/pgynpi/mapp";

    /**
     * 日志文件的位置，相当在 /data/data/[appid]/ 下的路径
     */
    public static final String LogUtil_SaveLogDir = "Log";

    /**
     * 日志文件的TAG标识（主要在Logcat中显示过滤用）
     */
    public static final String LogUtil_TAG = "LOG";


    // ===============APP相关配置项===============

    /**
     * 是否是Debug模式
     */
    public static final boolean ToastUtil_DebugFlag = false;
    //==========================基础通用接口规范 MAppMain============================

    /**
     * 服务器端口的根路径
     */
    private static final String SERVERROOTPATH = "http://192.168.1.86/pgynpi/mapp";

    /**
     * 用户登录接口 doLogin
     */
    public static final String MAPPMAIN_DOLOGIN = SERVERROOTPATH + ".MAppMain.doLogin.hf";

    /**
     * 用户自动登录接口 autoLogin
     */
    public static final String MAPPMAIN_AUTOLOGIN = SERVERROOTPATH + ".MAppMain.autoLogin.hf";

    /**
     * 会员注册接口 doRegister
     */
    public static final String MAPPMAIN_DOREGISTER = SERVERROOTPATH + ".MAppMain.doRegister.hf";

    /**
     * 设备注册接口deviceRegister
     */
    public static final String MAPPMAIN_DEVICEREGISTER = SERVERROOTPATH + ".MAppMain.deviceRegister.hf";

    /**
     * 获取应用基础信息
     */
    public static final String MAPPMAIN_GETAPPINFO = SERVERROOTPATH + ".MAppMain.getAppInfo.hf";

    /**
     * 修改用户个人信息 setUserInfo
     */
    public static final String MAPPMAIN_SETUSERINFO = SERVERROOTPATH + ".MAppMain.setUserInfo.hf";

    /**
     * 获取用户个人信息接口 getUserInfo
     */
    public static final String MAPPMAIN_GETUSERINFO = SERVERROOTPATH + ".MAppMain.getUserInfo.hf";

    /**
     * 获取短信验证码接口 getSMSVerifyCode
     */
    public static final String MAPPMAIN_GETSMSVERIFYCODE = SERVERROOTPATH + ".MAppMain.getSMSVerifyCode.hf";

    /**
     * 用户提交建议接口 submitUserAdvice
     */
    public static final String MAPPMAIN_SUBMITUSERADVICE = SERVERROOTPATH + ".MAppMain.submitUserAdvice.hf";

    //==========================外壳接口 MShell============================

    /**
     * 检查更新接口 checkAppUpdate
     */
    public static final String MAPPMAIN_CHECKAPPUPDATE = SERVERROOTPATH + ".MAppMain.checkAppUpdate.hf";

    /**
     * 获得外壳详情接口 getShellInfo
     */
    public static final String MSHELL_GETSHELLINFO = SERVERROOTPATH + ".MShell.getShellInfo.hf";

    /**
     * 获取轮播图接口 getCarouselList
     */
    public static final String MSHELL_GETCAROUSELLIST = SERVERROOTPATH + ".MShell.getCarouselList.hf";


    //==========================消息推送接口MPush============================

    /**
     * 获得菜单列表接口 getShellItemList
     */
    public static final String MSHELL_GETSHELLITEMLIST = SERVERROOTPATH + ".MShell.getShellItemList.hf";

    /**
     * 绑定用户身份标识 bindUserClientID
     */
    public static final String MPUSH_BINDUSERCLIENTID = SERVERROOTPATH + ".MPush.bindUserClientID.hf";

    /**
     * 解除用户身份标识 clearUserClientID
     */
    public static final String MPUSH_CLEARUSERCLIENTID = SERVERROOTPATH + ".MPush.clearUserClientID.hf";

    /**
     * 获取未读消息提示清单 getUnReadNoticeList
     */
    public static final String MPUSH_GETUNREADNOTICELIST = SERVERROOTPATH + ".MPush.getUnReadNoticeList.hf";

    /**
     *  读取消息列表 getMessageList
     */
    public static final String MPUSH_GETMESSAGELIST = SERVERROOTPATH + ".MPush.getMessageList.hf";

    /**
     * 读取消息 getMessageInfo
     */
    public static final String MPUSH_GETMESSAGEINFO = SERVERROOTPATH + ".MPush.getMessageInfo.hf";

    /**
     * 提交消息反馈状态 sendMessageStatus
     */
    public static final String MPUSH_SENDMESSAGESTATUS = SERVERROOTPATH + ".MPush.sendMessageStatus.hf";


    //=================================== 新闻接口 MNews ================================================

    /**
     * 获取推送消息模块详情接口getMPushParameter
     */
    public static final String MPUSH_GETPUSHPARAMTER = SERVERROOTPATH + ".MPush.getMPushParameter.hf";

    /**
     * 获得新闻模块详情接口getMNewsParameter
     */
    public static final String MNEWS_GETMNEWSPARAMETER = SERVERROOTPATH + ".MNews.getMNewsParameter.hf";

    /**
     * 获得新闻列表接口 MNews.getNewsList
     */
    public static final String MNEWS_GETNEWSLIST = SERVERROOTPATH + ".MNews.getNewsList.hf";

    //======================================= 文章接口Marti ================================================

    /**
     * 获得新闻分类接口 MNews.getTypeList
     */
    public static final String MNEWS_GETTYPELIST = SERVERROOTPATH + ".MNews.getTypeList.hf";

    /**
     * 获得新闻详情接口 MNews.getNewsInfo
     */
    public static final String MNEW_GETNEWSINFO = SERVERROOTPATH + ".MNews.getNewsInfo.hf";

    /**
     * 获取文章模块详情接口getMArtiParameter
     */
    public static final String MARTI_GETPARAMTER = SERVERROOTPATH + ".MArti.getMArtiParameter.hf";

    /**
     * 获得文章分类接口 getTypeList
     */
    public static final String MARTI_GETTYPELIST = SERVERROOTPATH + ".MArti.getTypeList.hf";

    //=======================================             ===========================================

    /**
     * 获得文章列表接口 getArticleList
     */
    public static final String MARTI_GETARTILIST = SERVERROOTPATH + ".MArti.getArticleList.hf";

    /**
     * 获得文章详情接口 getArticleInfo
     */
    public static final String MARTI_GETARTIINFO = SERVERROOTPATH + ".MArti.getArticleInfo.hf";

    /**
     * APP包名
     */
    public static String PackageName = "";

    /**
     * APP的上下文
     */
    public static Context APP_Context;

    /**
     * 是否登录标志
     */
    public static Boolean ISLOGIN = false;

    /**
     * 用户账号
     */
    public static String MemberUserID = null;

    /**
     * 固定值 用户端:100103<->86; 车主端:100101,100113最新测试号
     * 100102车主端    100101用户端
     *
     */
    public static String ECID = "100101";

    /**
     * 手机imei号
     */
    public static String IMEI = null;

    /**
     * 手机ua信息
     */
    public static String UA = null;

    /**
     * appuserID
     */
    public static String AppUserID = null;

    /**
     * 设备类型
     */
    public static String DeviceAndroid = "1";

    /**
     * 最新版本
     */
    public static String newVersion = null;

    /**
     * 最新版本名字
     */
    public static String newVersionName = null;

    /**
     * 应用标识
     */
    public static String AppID = null;

    /**
     * 应用名称
     */
    public static String AppName = null;

    /**
     * 会员称谓
     */
    public static String MemberNickName = null;

    /**
     * 管理员称谓
     */
    public static String ManagerNickName = null;

    /**
     * 游客称谓
     */
    public static String VisitorNickName = null;

    /**
     * 是否允许注册申请
     */
    public static String isRegister = null;

    /**
     * 注册说明
     */
    public static String RegisterExplain = null;

    /**
     * 咨询电话
     */
    public static String HotLine = null;

    /**
     * 技术支持描述信息
     */
    public static String TechnicalSupport = null;

    /**
     * 协议描述信息
     */
    public static String ProtocolDescribe = null;

    /**
     * 协议
     */
    public static String ProtocolKeyword = null;

    /**
     * 协议详情
     */
    public static String ProtocolDetail = null;

    //============================= 推送模块 ===================================

    /**
     * 　主题颜色
     */
    public static String ThemeColor = null;

    /**
     * 主题图片
     */
    public static String ThemePicFileID;

    /**
     * 模块背景图片
     */
    public static String BackgroundPicFileID;

    // ===============日志相关配置项===============

    /**
     * 个人信息
     */
    public static String InformationJson = "";

    //===============================shell模块ID=================================
    public static String Global_ShellID = "";

    /**
     * 模块名称
     */
    public static String Global_ModuleName = "";

    /**
     * 菜单类型
     */
    public static String Global_MenuType = "";

    /**
     * ECID对应推送编号
     */
    public static String PushID = "";

    /**
     * 个推模块名
     */
    public static String Push_ModuleName = "";

    /**
     * 新闻编号
     */
    public static String NewsID = "";

    /**
     * 新闻模块名
     */
    public static String News_ModuleName = "";

    /**
     * 文章编号
     */
    public static String MartiID = "";

    //////////////////////////////////////////////////////////////////////////////////////////
    //以下是mshare模块相关的内容
    //////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 0.服务器根路径
     * http://192.168.1.130:8080/sdsepi/mapp.MShare.getUserGoodsList.hf
     * http://home.handbbc.com:8680/
     * http://192.168.1.130:8080/
     * http://sdse.shidongvr.com/
     */
    public static String MSHARE_SERVER_ROOT = "http://sdse.shidongvr.com/";

    /**
     * 1.登陆注册接口
     */
    public static String MSHARE_LOGIN = "sdsepi/mapp.MShare.doUserLogin.hf";

    /**
     * 2.获取短信验证码接口
     */
    public static String MSHARE_GET_VERIFICATION_CODE = "sdsepi/mapp.MShare.getUserTempVerifyCode.hf";

    /**
     * 3.实名认证接口,验证身份证号
     */
    public static String MSHARE_VERIFY_NUMBER_OF_ID_CARD = "sdsepi/mapp.MShare.UserRealNameAuthentication.hf";

    /**
     * 4.交纳押金接口
     */
    public static String MSHARE_PAY_DEPOSIT = "sdsepi/mapp.MShare.UserPayDeposit.hf";

    /**
     * 5.获取坐标附近物品信息
     */
    public static String MSHARE_RETRIEVE_ALL_PRODUCTS_AROUND = "sdsepi/mapp.MShare.getUserGoodsList.hf";

    /**
     * 6.获取点击的某个物品详细信息
     */
    public static String MSHARE_RETRIEVE_DETAILS_OF_SELECTED_PRODUCTE = "sdsepi/mapp.MShare.getUserGoodsDetail.hf";

    /**
     * 7.物品预约接口
     */
    public static String MSHARE_RESERVE_SELECTED_PRODUCT = "sdsepi/mapp.MShare.orderBook.hf";

    /**
     * 8.更新物品支付状态接口
     */
    public static String MSHARE_PAY_ORDER_DEPOSIT_OR_TOTAL_PAYMENT = "sdsepi/mapp.MShare.updateOrderPayStatus.hf";

    /**
     * 9.用户端问题与意见接口
     */
    public static String MSHARE_CUSTOMER_SERVICE = "sdsepi/mapp.MShare.commitUserOpinion.hf";

    /**
     * 10.向服务器查询真实的支付结果接口
     */
    public static String MSHARE_QUERY_PAY_RESULT = "sdsepi/mapp.MShare.getPayResultStatus.hf";




    /**
     * 标识当前用户所在的城市
     * 在搜索页面中用到
     */
    public static String currentCity = "";

    /**
     * 客户端类型: 1.共享端(车主端) 2.用户端
     */
    public static String AppType = "2";

    /**
     * Caution: 自动登陆密钥
     * 需要用,就去查数据库,不在全局变量中显示
     */
//    public static String AutoLoginKey="";

    /**
     * 全应用共用一个SP文件---(mshare)
     */
    public static String MSHARE_SP_FOR_LOGIN_RESULT = "mshare_sp_for_login_result";

    //////////////////////////////////////////////////////////////////////////////////////////
    //以下是mshare车主端模块相关的内容
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 0.车主端模块的服务器根路径
     * http://192.168.1.130:8080/
     */
    public static String SHARER_SERVER_ROOT = "http://sdse.shidongvr.com/";

    /**
     * 1.获取物品类型信息接口
     * http://sdse.shidongvr.com/sdsepi/mapp.MShare.getGoodsTypeInfo.hf
     */
    public static String  SHARER_RETRIEVE_ALL_TYPE_INFO= "sdsepi/mapp.MShare.getGoodsTypeInfo.hf";
    /**
     * 2.提交新增物品信息接口
     * http://sdse.shidongvr.com/sdsepi/mapp.MShare.commitNewGoodsInfo.hf
     */
    public static String SUBMIT_NEW_ITEM = "sdsepi/mapp.MShare.commitNewGoodsInfo.hf";
    /**
     * 3.获取车主端物品接口
     * http://sdse.shidongvr.com/sdsepi/mapp.MShare.getSharerGoodsList.hf
     */
    public static String SHARER_RETRIEVE_ALL_PRODUCTS_AROUND = "sdsepi/mapp.MShare.getSharerGoodsList.hf";
    /**
     * 4.获取车主端物品详情接口
     *http://sdse.shidongvr.com/sdsepi/mapp.MShare.getSharerGoodsDetail.hf
     */
    public static String SHARER_RETRIEVE_SELECTED_PRODUCT_INFO = "sdsepi/mapp.MShare.getSharerGoodsDetail.hf";
    /**
     * 5.修改物品信息接口
     * http://sdse.shidongvr.com/sdsepi/mapp.MShare.modifyGoodsInfo.hf
     */
    public static String SHARER_MODIFY = "sdsepi/mapp.MShare.modifyGoodsInfo.hf";

    /**
     * 6.删除物品接口
     *http://sdse.shidongvr.com/sdsepi/mapp.MShare.deleteGoods.hf
     */
    public static String SHARER_DELETE_CURRENT_PRODUCT = "sdsepi/mapp.MShare.deleteGoods.hf";

    /**
     * 7.获取订单接口
     * http://sdse.shidongvr.com/sdsepi/mapp.MShare.getOrderList.hf
     */
    public static String SHARER_RETRIEVE_ORDER_INFO = "sdsepi/mapp.MShare.getOrderList.hf";

    /**
     * 8.获取关于我们信息接口
     * http://sdse.shidongvr.com/sdsepi/mapp.MShare.getAboutUSInfo.hf
     */
    public static String  SHARER_RETRIEVE_ABOUT_US_INFO  = "sdsepi/mapp.MShare.getAboutUSInfo.hf";

    /**
     * 9.申请提现接口
     * http://sdse.shidongvr.com/sdsepi/mapp.MShare.applyForCash.hf
     */
    public static String SHARER_WITHDRAW = "sdsepi/mapp.MShare.applyForCash.hf";

    /**
     * 10.车主端问题与意见接口
     * http://sdse.shidongvr.com/sdsepi/mapp.MShare.commitSharerOpinion.hf
     */
    public static String  SHARER_CUSTOMER_SERVICE= "sdsepi/mapp.MShare.commitSharerOpinion.hf";

    /**
     * 11.车主端登陆接口
     * http://sdse.shidongvr.com/sdsepi/mapp.MShare.doSharerLogin.hf
     */
    public static String  SHARER_LOGIN_IN = "sdsepi/mapp.MShare.doSharerLogin.hf";

    /**
     * 12.车主端获取短信验证码接口
     * http://sdse.shidongvr.com/sdsepi/mapp.MShare.getSharerTempVerifyCode.hf
     */
    public static String SHARER_RETRIEVE_VERIFICATION_CODE = "sdsepi/mapp.MShare.getSharerTempVerifyCode.hf";

    /**
     * 13.车主端实名认证接口
     * http://sdse.shidongvr.com/sdsepi/mapp.MShare.SharerRealNameAuthentication.hf
     */
    public static String  SHARER_AUTHENTICATION = "sdsepi/mapp.MShare.SharerRealNameAuthentication.hf";

    /**
     * 14.车主端获取支付押金的加签订单接口
     * http://sdse.shidongvr.com/sdsepi/mapp.MShare.SharerPayDeposit.hf
     */
    public static String  SHARER_DEPOSIT = "sdsepi/mapp.MShare.SharerPayDeposit.hf";


    public static final String[] BUSINESS_TYPE = {"扫码使用","押金使用"};

    /**
     * 屏幕的宽度(像素值)
     */
    public static int display_width;


    public static final String PriceType = "元/次";

    public static final String WEIXIN_APP_ID = "wx8315772c59afd991";
}
