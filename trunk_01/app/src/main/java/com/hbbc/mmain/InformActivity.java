package com.hbbc.mmain;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hbbc.R;
import com.hbbc.mmain.bean.InformCodeBean;
import com.hbbc.mpush.GetuiUtil;
import com.hbbc.mpush.MessageListActivity;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.CheckUpdateUtil;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyCircleImageView;
import com.hbbc.util.MyTopbar;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.hbbc.util.GlobalConfig.InformationJson;

/**
 * 个人信息界面
 */
public class InformActivity extends BaseActivity implements View.OnTouchListener, MyTopbar.OnTopBarClickListener {


    private ImageView inform_setting;                                               //设置，退出

    private MyCircleImageView infom_img;                                             //头像

    private ImageView inform_new, inform_msg_new;                                   //更新new

    private TextView inform_username, inform_sex, inform_adress, inform_phone;      //用户名，性别，地址,手机号

    private TextView inform_guardian_name, inform_contacts_phone;                   //联系人，联系人手机号码

    private LinearLayout inform_update, inform_aboutus, inform_msg, inform_suggest;//检查更新,MessageListActivity,用户建议

    private RelativeLayout infom_user;                                              //个人基础信息布局，topbar布局

    private MyTopbar infom_include;                                                 //标题栏

    private int themeColor = Color.WHITE;                                           //主题色

    private LinearLayout inform_guardian, inform_contacts;                       //监护人，联系方式 布局（隐藏或显示）

    private Button inform_tologin;                                                //注销

    //判断是否为点击
    private boolean ischeck = false;                                                //头像

    private boolean isCancel = false;                                               //注销



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mmain_inform_main);

        //初始化控件
        initView();
    }



    /**
     * 初始化控件
     */
    @Override
    protected void initView() {

        inform_tologin = (Button) findViewById(R.id.inform_tologin);
        inform_setting = (ImageView) findViewById(R.id.inform_setting);
        infom_img = (MyCircleImageView) findViewById(R.id.infom_img);
        inform_username = (TextView) findViewById(R.id.inform_username);
        inform_sex = (TextView) findViewById(R.id.inform_sex);
        inform_adress = (TextView) findViewById(R.id.inform_adress);
        inform_phone = (TextView) findViewById(R.id.inform_phone);
        inform_guardian_name = (TextView) findViewById(R.id.inform_guardian_name);
        inform_contacts_phone = (TextView) findViewById(R.id.inform_contacts_phone);
        inform_update = (LinearLayout) findViewById(R.id.inform_update);
        inform_aboutus = (LinearLayout) findViewById(R.id.inform_aboutus);
        infom_user = (RelativeLayout) findViewById(R.id.infom_user);
        infom_include = (MyTopbar) findViewById(R.id.infom_include);
        inform_new = (ImageView) findViewById(R.id.inform_update_new);
        inform_guardian = (LinearLayout) findViewById(R.id.inform_guardian);
        inform_contacts = (LinearLayout) findViewById(R.id.inform_contacts);
        inform_msg = (LinearLayout) findViewById(R.id.inform_msg);
        inform_suggest = (LinearLayout) findViewById(R.id.inform_suggest);
        inform_msg_new = (ImageView) findViewById(R.id.inform_msg_new);

        //管理员会隐藏布局
        if (GlobalParameter.getPersonflag().equals("1")) {
            inform_guardian.setVisibility(View.GONE);
            inform_contacts.setVisibility(View.GONE);
        }

        //主题色初始化
        initthemeColor();

        //设置监听事件
        infom_include.setOnTopBarClickListener(this);
        inform_update.setOnClickListener(this);
        inform_aboutus.setOnClickListener(this);
        inform_setting.setOnClickListener(this);
        inform_msg.setOnClickListener(this);
        inform_suggest.setOnClickListener(this);

        inform_tologin.setOnTouchListener(this);
        infom_img.setOnTouchListener(this);
    }



    /**
     * 消息回调机制
     */
    @Override
    protected void getMessage(Message msg) {

        switch (msg.what) {

            case 0:
                //检查更新
                checkUpdate((String) msg.obj, false);
                break;

            case 1:
                //检查更新,可下载
                checkUpdate((String) msg.obj, true);
                break;

            case 2:
                String data = msg.obj.toString();
                if (!GlobalConfig.InformationJson.equals(data)) {
                    GlobalConfig.InformationJson = data;
                    //更新界面用户信息
                    updateInfom(InformationJson);
                } else {
                    //从本地缓存加载数据
                    updateInfoGlobal();
                }
                break;
            case 3:
                //注销个推信息
                try {
                    JSONObject json = new JSONObject((String) msg.obj);
                    if (json.getBoolean("Result")) {
                        //注销个推
                        if (GetuiUtil.isOpenGetui())
                            GetuiUtil.turnoffGetui();

                        //清除存储信息
                        GlobalParameter.clearSharePreference();
                    } else {
                        outputShort(json.getString("Notice"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case 4:
                String data1 = msg.obj.toString();
                try {
                    JSONObject jsonObject = new JSONObject(data1);
                    if (jsonObject.has("TotalUnReadNum")) {
                        int UnReadNum = jsonObject.getInt("TotalUnReadNum");
                        if (UnReadNum > 0) {
                            inform_msg_new.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }



    /**
     * 主题色赋值
     */
    private void initthemeColor() {
        //主题色赋值
        themeColor = GlobalParameter.getThemeColor();

        //设置topbar颜色
        infom_user.setBackgroundColor(themeColor);
        //设置基本信息布局颜色
        infom_include.setBackgroundColor(themeColor);
        //标题
        infom_include.setTitle("个人信息");
    }



    /**
     * 监听事件
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.inform_aboutus:
                //关于我们
                startActivity(new Intent(this, AboutUsActivity.class));
                break;

            case R.id.inform_setting:
                //跳转修改个人信息页面
                startActivity(new Intent(this, ModifyInformActivity.class));
                break;

            case R.id.inform_update:
                //检查更新
                new HttpUtil().callJson(handler, 1, this, GlobalConfig.MAPPMAIN_CHECKAPPUPDATE, "AppUserID", GlobalConfig.AppUserID, "AppID", GlobalConfig.AppID, "VersionType", "1", "CurVersion", GlobalConfig.newVersion);
                break;

            case R.id.inform_msg:
                //MessageListActivity,跳转我的信息界面
                startActivity(new Intent(this, MessageListActivity.class));
                break;

            case R.id.inform_suggest:
                //跳转用户建议界面
                startActivity(new Intent(this, SuggestActivity.class));
                break;
        }

    }



    /**
     * 点击效果,监听事件
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                switch (v.getId()) {
                    case R.id.infom_img:
                        ischeck = true;
                        v.setAlpha(0.7f);
                        break;
                    default:
                        isCancel = true;
                        GradientDrawable p1 = (GradientDrawable) v.getBackground();
                        p1.setAlpha(200);
                        p1.setCornerRadius(8.0f);
                        break;
                }
                break;


            case MotionEvent.ACTION_MOVE:
                //当滑动出局时，执行
                if (!(event.getX() > 0 && event.getX() < v.getMeasuredWidth() && event.getY() > 0 && event.getY() < v.getMeasuredHeight())) {
                    switch (v.getId()) {
                        case R.id.infom_img:
                            ischeck = false;
                            v.setAlpha(1.0f);
                            break;
                        default:
                            isCancel = false;
                            GradientDrawable p2 = (GradientDrawable) v.getBackground();
                            p2.setAlpha(255);
                            p2.setCornerRadius(8.0f);
                            break;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                //判断点击登录
                if (ischeck) {
                    //跳转修改信息界面
                    startActivity(new Intent(this, ModifyInformActivity.class));
                    //重置默认值
                    ischeck = false;
                }
                if (isCancel) {
                    //退出
                    cancelDialog();
                    //重置默认值
                    isCancel = false;
                }
                switch (v.getId()) {

                    case R.id.infom_img:
                        v.setAlpha(1.0f);
                        break;

                    default:
                        GradientDrawable p3 = (GradientDrawable) v.getBackground();
                        p3.setAlpha(255);
                        break;
                }
                break;
        }

        return true;
    }



    /**
     * 本地缓存加载用户信息
     */
    private void updateInfoGlobal() {

        if (!GlobalParameter.getManagername().equals("")) {
            inform_username
                    .setText(GlobalParameter.getManagername());
        }
        if (!GlobalParameter.getMembername().equals("")) {
            inform_username.setText(GlobalParameter.getMembername());
        }
        if (!GlobalParameter.getPhonenum().equals("")) {
            String phoneNum = GlobalParameter.getPhonenum();
            String beforeNum = phoneNum.substring(0, 3);
            String middleNum = phoneNum.substring(3, 7);
            String laterNum = phoneNum.substring(7);
            String phone = beforeNum + "-" + middleNum + "-" + laterNum;
            inform_phone.setText(phone);
        }
        if (!GlobalParameter.getHeadpicfieldid().equals("")) {
            Glide.with(this).load(GlobalParameter.getHeadpicfieldid()).error(R.drawable.mmain_unlisted_upic).into(infom_img);
        }

        if (!GlobalParameter.getPosition().equals("")) {
            inform_adress.setText(GlobalParameter.getPosition());
        }
        if (GlobalParameter.getSex().equals("男")) {
            inform_sex.setText("男");
        } else {
            inform_sex.setText("女");
        }


        if (!GlobalParameter.getParentname().equals("")) {
            inform_guardian_name.setText(GlobalParameter.getParentname());
        }
        if (!GlobalParameter.getParentphonenum().equals("")) {
            inform_contacts_phone.setText(GlobalParameter.getParentphonenum());
        }
        if (!GlobalParameter.getProvincecode().equals("")) {
            String provinceName = getProvince(GlobalParameter.getProvincecode());
            inform_adress.setText(provinceName);
        }
        if (!GlobalParameter.getCitycode().equals("")) {
            String cityName = getCityName(GlobalParameter.getCitycode(), GlobalParameter.getProvincecode());
            inform_adress.append("-" + cityName);
        }
    }



    /**
     * 退出时对话框
     */
    public void cancelDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(getResources().getString(R.string.information_exit));
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
                //注销个推
                new HttpUtil().callJson(handler, 3, InformActivity.this, GlobalConfig.MPUSH_CLEARUSERCLIENTID, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "", "PushID", GlobalConfig.PushID, "GWGTID", GlobalParameter.getGwgtid());
                //注销时回到登录页面
                startActivity(new Intent(InformActivity.this, LoginActivity.class));
            }

        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    /**
     * 更新用户信息
     */
    private void updateInfom(String obj) {

        String province = null;                                                           //省
        String city = null;                                                               //市
        try {
            JSONObject jobj = new JSONObject(obj);
            if (jobj.getBoolean("Result")) {
                //用户名
                if (jobj.has("ManagerName")) {
                    GlobalParameter.setManagername(jobj.getString("ManagerName"));
                    inform_username.setText(jobj.getString("ManagerName"));
                }

                //用户名
                if (jobj.has("MemberName")) {
                    GlobalParameter.setMembername(jobj.getString("MemberName"));
                    inform_username.setText(jobj.getString("MemberName"));
                }

                //手机号
                if (jobj.has("PhoneNum")) {
                    String phoneNum = jobj.getString("PhoneNum");
                    String beforeNum = phoneNum.substring(0, 3);
                    String middleNum = phoneNum.substring(3, 7);
                    String laterNum = phoneNum.substring(7);
                    String phone = beforeNum + "-" + middleNum + "-" + laterNum;
                    GlobalParameter.setPhonenum(phoneNum);
                    inform_phone.setText(phone);
                }

                //头像
                if (jobj.has("HeadPicFieldID")) {
                    GlobalParameter.setHeadpicfield(jobj.getString("HeadPicFieldID"));
                    Glide.with(this).load(jobj.getString("HeadPicFieldID")).error(R.drawable.mmain_unlisted_upic).into(infom_img);
                }

                //职位
                if (jobj.has("Position")) {
                    GlobalParameter.setPosition(jobj.getString("Position"));
                    inform_adress.setText(jobj.getString("Position"));
                }

                //性别
                if (jobj.has("Sex")) {

                    if (jobj.getBoolean("Sex")) {
                        GlobalParameter.setSex("男");
                        inform_sex.setText("男");
                    } else {
                        GlobalParameter.setSex("女");
                        inform_sex.setText("女");
                    }
                }

                //监护人姓名
                if (jobj.has("ParentName")) {
                    GlobalParameter.setParentname(jobj.getString("ParentName"));
                    inform_guardian_name.setText(jobj.getString("ParentName"));
                }

                //监护人手机号
                if (jobj.has("ParentPhoneNum")) {
                    GlobalParameter.setParentphonenum(jobj.getString("ParentPhoneNum"));
                    inform_contacts_phone.setText(jobj.getString("ParentPhoneNum"));
                }

                //地址
                if (jobj.has("Address")) {
                    inform_adress.setText("");
                }

                //城市编号
                if (jobj.has("ProvinceCode")) {
                    GlobalParameter.setProvincecode(jobj.getString("ProvinceCode"));
                    String provinceName = getProvince(jobj.getString("ProvinceCode"));
                    inform_adress.setText(provinceName);
                }

                if (jobj.has("CityCode")) {
                    GlobalParameter.setCitycode(jobj.getString("CityCode"));
                    String CityName = getCityName(jobj.getString("CityCode"), jobj.getString("ProvinceCode"));
                    //省市
                    inform_adress.append("-" + CityName);
                }
            } else {
                outputLong("登录失败");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**
     * 获取城市名称
     */
    private String getCityName(String cityCode, String provinceCode) {

        String city = "";
        //转成实体类
        InformCodeBean informCodeBean = new Gson().fromJson(getResources().getString(R.string.city_select), InformCodeBean.class);
        List<InformCodeBean.DtCity> dtCities = informCodeBean.getDtCity();
        for (InformCodeBean.DtCity dtCity : dtCities) {
            if (dtCity.getProvinceCode().equals(provinceCode) && dtCity.getCityCode().equals(cityCode)) {
                //市
                city = dtCity.getCityName();
            }
        }
        return city;
    }



    /**
     * 获取省名
     */
    private String getProvince(String provinceCode) {

        String province = "";
        //转成实体类
        InformCodeBean informCodeBean = new Gson().fromJson(getResources().getString(R.string.city_select), InformCodeBean.class);
        List<InformCodeBean.DtSheng> dtSheng = informCodeBean.getDtSheng();
        for (InformCodeBean.DtSheng dtSheng1 : dtSheng) {
            if (dtSheng1.getProvinceCode().equals(provinceCode)) {
                //省
                province = dtSheng1.getProvinceName();
            }
        }

        return province;
    }



    /**
     * 检查是否需要更新，来判断是否显示图片new
     * isCheck用来判断用户是否点击了
     */
    private void checkUpdate(String data, boolean isCheck) {

        try {
            JSONObject json = new JSONObject(data);
            boolean isUpdate = false;  //是否可以更新
            if (json.getBoolean("Result")) {
                //请求成功
                if (json.has("VersionCode")) {
                    String newversion = json.getString("VersionCode"); //版本号
                    String DownloadURL = json.getString("UpdateURL"); //下载地址
                    String UpdateDesc = json.getString("UpdateDesc"); //更新的描述

                    //版本判断
                    String[] newversions = newversion.split("\\.", 3);
                    String[] version = GlobalConfig.newVersion.split("\\.", 3);
                    //判断是否可以更新
                    for (int i = 0; i < newversions.length; i++) {
                        if (Integer.valueOf(newversions[i]) > Integer.valueOf(version[i])) {
                            inform_new.setVisibility(View.VISIBLE);
                            isUpdate = true;
                            break;
                        }
                    }

                    //用户点击并且版本号可以更新
                    if (isCheck && isUpdate) {
                        new CheckUpdateUtil(this).doNewVersionUpdate(DownloadURL, this, newversion);
                    } else if (isCheck && !isUpdate) {
                        outputShort("当前为最新版本");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    /**
     * 显示界面时，调用接口
     */
    @Override
    protected void onResume() {

        super.onResume();

        Glide.with(this).onStart();
        Logger.d("OnResume");
        //管理员会隐藏布局
        if (GlobalParameter.getPersonflag().equals("1")) {
            inform_guardian.setVisibility(View.GONE);
            inform_contacts.setVisibility(View.GONE);
        }

        //检查更新接口
        new HttpUtil().callJson(handler, 0, this, GlobalConfig.MAPPMAIN_CHECKAPPUPDATE, "AppUserID", GlobalConfig.AppUserID, "AppID", GlobalConfig.AppID, "VersionType", "1", "CurVersion", GlobalConfig.newVersion);
        //获取个人用户信息接口
        new HttpUtil().callJson(handler, 2, this, GlobalConfig.MAPPMAIN_GETUSERINFO, "AppUserID", GlobalConfig.AppUserID, "UserID", GlobalParameter.getManagerid() + GlobalParameter.getMemberid(), "PersonFlag", GlobalParameter.getPersonflag());
        //我的消息接口
        new HttpUtil().callJson(handler, 4, this, GlobalConfig.MPUSH_GETUNREADNOTICELIST, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "", "PushID", GlobalConfig.PushID, "UserType", GlobalParameter.getPersonflag(), "UserID", GlobalParameter.getManagerid() + GlobalParameter.getMemberid());
    }



    @Override
    public void setOnLeftClick() {

        finish();
    }



    @Override
    public void setOnRightClick() {

    }



    @Override
    protected void onPause() {

        super.onPause();
        Glide.with(this).onStop();
    }
}
