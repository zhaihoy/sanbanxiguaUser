package com.hbbc.mmain;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.mpush.GetuiUtil;
import com.hbbc.mshell.MainActivity;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.CommonUtil;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.UIUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 启动页面WelcomeActivity
 */
public class WelcomeActivity extends BaseActivity {

    private ImageView imageView;           //Loading页面图片



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 设置全屏显示
        UIUtil.setFullScreenShow(this, true);

        // 装载Loading页面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mmain_welcome);
        // 设置上下文
        GlobalConfig.APP_Context = this;
        GlobalConfig.IMEI = CommonUtil.getIMEI(getApplicationContext());
        GlobalConfig.UA = CommonUtil.getUa();
        //版本号
        GlobalConfig.newVersion = CommonUtil.getVerName(this) + "." + CommonUtil.getVerCode(getApplicationContext());
        GlobalConfig.newVersionName = CommonUtil.getVerName(this);
    }



    @Override
    protected void onResume() {

        super.onResume();
        //获取应用设备
        new HttpUtil().callJson(handler, 0, this, GlobalConfig.MAPPMAIN_GETAPPINFO, "ECID", GlobalConfig.ECID + "");

        //初始化Loading页面的图片
        imageView = (ImageView) findViewById(R.id.shell_welcome_bgpicIV);
    }



    /**
     * 页面消息统一处理入口方法
     */
    @Override
    protected void getMessage(Message msg) {

        switch (msg.what) {

            case 0:
                AppInfo((String) msg.obj);
                break;

            case 1:
                String json = (String) msg.obj;
                autoLoginSuccess(json);
                //有网络时，3秒钟后，跳转到主页面
                Handler handler = new Handler();
                TurnToMainActiviey runable = new TurnToMainActiviey();
                handler.postDelayed(runable, 3000L);
                break;

            case 2:
                String jsonPic = (String) msg.obj;
                autoToPic(jsonPic);
                break;


            case 3:
                try {
                    //获取推送的数据
                    JSONObject jsonObject = new JSONObject((String) msg.obj);
                    if (jsonObject.getBoolean("Result")) {
                        GlobalConfig.PushID = jsonObject.getInt("PushID") + "";
                        GlobalConfig.Push_ModuleName = jsonObject.getString("ModuleName");
                        System.out.println("GGGGG+++++++++=====");
                        setAutoLogin();
                    } else {
                        outputShort(jsonObject.getString("Notice"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;


            case 4:
                String jsonat = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(jsonat);
                    //如果自动登录失败，清除本地数据
                    if (!jsonObject.getBoolean("Result")) {
                        closeGetui();
                        outputShort(jsonObject.getString("Notice"));
                    } else {
                        //开启个推
                        openGetui();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case 5:

                try {
                    JSONObject jsono = new JSONObject((String) msg.obj);
                    if (jsono.getBoolean("Result")) {
                        //个推ClientID记录
                        GlobalParameter.setGwgtid(jsono.getInt("GWGTID") + "");
                    } else {
                        outputShort(jsono.getString("Notice"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case 6:
                //注销个推信息
                try {
                    JSONObject jsons = new JSONObject((String) msg.obj);
                    if (jsons.getBoolean("Result")) {
                        //注销个推
                        if (GetuiUtil.isOpenGetui())
                            GetuiUtil.turnoffGetui();
                        //登录失败时，清除本地参数值
                        GlobalParameter.clearSharePreference();
                    } else {
                        outputShort(jsons.getString("Notice"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }



    /**
     * 获得应用基础信息
     */
    private void AppInfo(String obj) {

        try {
            JSONObject jsonObject = new JSONObject(obj);
            //请求响应结果
            boolean isResult = jsonObject.getBoolean("Result");
            String isNotice = jsonObject.getString("Notice");
            if (isResult) {

                //应用标识
                GlobalConfig.AppID = jsonObject.getInt("AppID") + "";
                //应用名称
                GlobalConfig.AppName = jsonObject.getString("AppName");
                //管理员称谓
                GlobalConfig.ManagerNickName = jsonObject.getString("ManagerNickName");
                //会员称谓
                GlobalConfig.MemberNickName = jsonObject.getString("MemberNickName");
                //游客称谓
                GlobalConfig.VisitorNickName = jsonObject.getString("VisitorNickName");
                //是否允许注册申请
                GlobalConfig.isRegister = String.valueOf(jsonObject.getInt("IsRegister"));
                //注册说明
                GlobalConfig.RegisterExplain = jsonObject.getString("RegisterExplain");
                //主题图片
                GlobalConfig.ThemePicFileID = jsonObject.getString("ThemePicFileID");
                //咨询电话
                String HotLine = jsonObject.getString("HotLine");
                GlobalConfig.HotLine = HotLine.substring(0, 3) + "-" + HotLine.substring(3, 7) + "-" + HotLine.substring(7);

                //技术支持描述信息
                GlobalConfig.TechnicalSupport = jsonObject.getString("TechnicalSupport");
                //协议描述信息
                GlobalConfig.ProtocolDescribe = jsonObject.getString("ProtocolDescribe");
                //协议详情
                GlobalConfig.ProtocolDetail = jsonObject.getString("ProtocolDetail");
                //协议
                GlobalConfig.ProtocolKeyword = jsonObject.getString("ProtocolKeyword");
                //主题色
                GlobalConfig.ThemeColor = jsonObject.getString("ThemeColor");

                //RGB颜色
                if (GlobalConfig.ThemeColor.contains(",")) {
                    String[] ThemeColors = GlobalConfig.ThemeColor.split(",");
                    if (!GlobalConfig.ThemeColor.equals("") && GlobalConfig.ThemeColor.contains(",")) {
                        int ThemeRed = Integer.valueOf(ThemeColors[0]);
                        int ThemeGreen = Integer.valueOf(ThemeColors[1]);
                        int ThemeBlue = Integer.valueOf(ThemeColors[2]);
                        GlobalParameter.setThemeColor(Color.rgb(ThemeRed, ThemeGreen, ThemeBlue));
                    }
                } else {
                    String ThemeColor = "#" + GlobalConfig.ThemeColor;
                    GlobalParameter.setThemeColor(Color.parseColor(ThemeColor));
                }

                Logger.d("ThemeColor:" + GlobalConfig.ThemeColor);

                //更换启动页面的Loading图片
                if (jsonObject.has("LoadingPicFileID")) {
                    String isLoadingPic = jsonObject.getString("LoadingPicFileID");
                    if (!isLoadingPic.equals("") && !TextUtils.isEmpty(isLoadingPic))
                        Glide.with(this).load(isLoadingPic).into(imageView);
                }

                //当上个请求执行完后，执行下个请求
                //执行设备注册接口
                new HttpUtil().callJson(handler, 1, this, GlobalConfig.MAPPMAIN_DEVICEREGISTER, "IMEI", GlobalConfig.IMEI, "UA", GlobalConfig.UA, "AppID", GlobalConfig.AppID, "CurVersion", GlobalConfig.newVersion);
            } else {
                outputShort(isNotice);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**
     * 获得新的Loading图片的后续逻辑
     */
    private void autoToPic(String jsonPic) {

        try {
            JSONObject jsonObject = new JSONObject(jsonPic);
            //请求响应结果
            boolean isResult = jsonObject.getBoolean("Result");
            String isNotice = jsonObject.getString("Notice");
            if (isResult) {
                //外壳编号ID
                GlobalConfig.Global_ShellID = jsonObject.getString("ShellID");
                //模块名称
                GlobalConfig.Global_ModuleName = jsonObject.getString("ModuleName");
                //菜单类型 1 、左右结构
                GlobalConfig.Global_MenuType = jsonObject.getString("MenuType");
                //模块背景图片
                GlobalConfig.BackgroundPicFileID = jsonObject.getString("BackgroundPicFileID");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**
     * 自动登录成功后的业务逻辑
     */
    private void autoLoginSuccess(String strJson) {

        try {
            JSONObject jsonObject;
            jsonObject = new JSONObject(strJson);
            if (jsonObject.getBoolean("Result")) {

                if (jsonObject.has("AppUserID")) {
                    GlobalConfig.AppUserID = jsonObject.getString("AppUserID");
                }
                if (jsonObject.has("MemeberUserID")) {
                    GlobalConfig.MemberUserID = jsonObject
                            .getString("MemeberUserID");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //获得外壳详情接口
        if (!GlobalConfig.AppUserID.equals("") && !TextUtils.isEmpty(GlobalConfig.AppUserID)) {
            new HttpUtil().callJson(handler, 2, this, GlobalConfig.MSHELL_GETSHELLINFO,
                    "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "");

            //推送接口
       new HttpUtil().callJson(handler, 3, this, GlobalConfig.MPUSH_GETPUSHPARAMTER, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "");
        }

    }



    /**
     * 自动登录
     */
    private void setAutoLogin() {

        //自动登录
        if (GlobalParameter.contains("LoginKey") && !GlobalParameter.getLoginkey().equals("")) {
            new HttpUtil().callJson(handler, 4, this, GlobalConfig.MAPPMAIN_AUTOLOGIN, "AppUserID", GlobalConfig.AppUserID, "PhoneNum", GlobalParameter.getPhonenum(), "LoginKey", GlobalParameter.getLoginkey());
        }
    }



    /**
     * 开启个推
     */
    private void openGetui() {

        Logger.d("openGetui");

        //开启个推
        GetuiUtil.turnonGetui();

        //绑定用户身份标识
        new HttpUtil().callJson(handler, 5, this, GlobalConfig.MPUSH_BINDUSERCLIENTID, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "", "PushID", GlobalConfig.PushID, "UserType", GlobalParameter.getPersonflag(), "UserID", GlobalParameter.getManagerid() + GlobalParameter.getMemberid(), "UserName", GlobalParameter.getName(), "PhoneNum", GlobalParameter.getPhonenum(), "GTClientID", GetuiUtil.getClientID(), "DeviceType", "android");
    }



    /**
     * 关闭个推
     */
    private void closeGetui() {

        new HttpUtil().callJson(handler, 6, this, GlobalConfig.MPUSH_CLEARUSERCLIENTID, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "", "PushID", GlobalConfig.PushID, "GWGTID", GlobalParameter.getGwgtid());
    }



    /**
     * 跳转到主页
     */
    private class TurnToMainActiviey implements Runnable {

        public void run() {

            Intent intent = new Intent();
            intent.setClassName(WelcomeActivity.this,
                    MainActivity.class.getName());
            startActivity(intent);
        }
    }
}
