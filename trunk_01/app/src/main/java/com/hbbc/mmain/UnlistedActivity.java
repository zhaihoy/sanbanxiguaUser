package com.hbbc.mmain;

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

import com.hbbc.R;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.CheckUpdateUtil;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 未登录（个人信息）界面
 */
public class UnlistedActivity extends BaseActivity implements View.OnTouchListener, MyTopbar.OnTopBarClickListener {
    private Button mshell_tologin;                                        //登录按钮

    private ImageView mshell_unlisted_new, unlisted_img;                  //退出按钮,更新new图片,未登录时头像

    private TextView global_title;                                        //标题

    private RelativeLayout unlisted_user;                                 //未登录布局（在topbar下）

    private MyTopbar unlisted_include;                                     //topbar的布局

    private int themeColor = Color.WHITE;                                  //主题色

    private LinearLayout mshell_aboutus, mshell_update;                    //关于我们，检查更新

    //判断是否为点击
    private boolean ischeck;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mmain_unlisted_main);

        //初始化控件
        initView();
    }



    @Override
    protected void onResume() {
        super.onResume();
        //检查更新接口
        new HttpUtil().callJson(handler, 0, this, GlobalConfig.MAPPMAIN_CHECKAPPUPDATE, "AppUserID", GlobalConfig.AppUserID, "AppID", GlobalConfig.AppID, "VersionType", "1", "CurVersion", GlobalConfig.newVersion);
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
        }
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
                        Logger.d(newversions[i] + "," + version[i]);//Log
                        if (Integer.valueOf(newversions[i]) > Integer.valueOf(version[i])) {
                            mshell_unlisted_new.setVisibility(View.VISIBLE);
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
     * 初始化控件
     */
    @Override
    protected void initView() {
        mshell_tologin = (Button) findViewById(R.id.mshell_tologin);
        unlisted_include = (MyTopbar) findViewById(R.id.unlisted_include);
        global_title = (TextView) findViewById(R.id.global_title);
        mshell_unlisted_new = (ImageView) findViewById(R.id.mshell_unlisted_new);
        unlisted_user = (RelativeLayout) findViewById(R.id.unlisted_user);
        mshell_aboutus = (LinearLayout) findViewById(R.id.mshell_aboutus);
        mshell_update = (LinearLayout) findViewById(R.id.mshell_update);
        unlisted_img = (ImageView) findViewById(R.id.unlisted_img);

        //初始化主题色
        initthemeColor();

        //设置控件监听事件
        unlisted_include.setOnTopBarClickListener(this);
        mshell_update.setOnClickListener(this);
        mshell_aboutus.setOnClickListener(this);

        mshell_tologin.setOnTouchListener(this);
        unlisted_img.setOnTouchListener(this);
    }



    /**
     * 初始化主题色
     */
    private void initthemeColor() {
        //主题色赋值
        themeColor= GlobalParameter.getThemeColor();

        //标题栏布局的背景色
        unlisted_include.setBackgroundColor(themeColor);
        //未登录布局的背景色
        unlisted_user.setBackgroundColor(themeColor);

        //登录按钮背景色
        GradientDrawable p = (GradientDrawable) mshell_tologin.getBackground();
        p.setStroke(2, themeColor);
        p.setCornerRadius(8.0f);
        p.setColor(themeColor);

        //标题
        unlisted_include.setTitle("个人信息");
    }



    /**
     * 监听事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.mshell_aboutus:
                //点击关于我们进入关于我们界面
                startActivity(new Intent(this, AboutUsActivity.class));
                break;

            case R.id.mshell_update:
                //检查更新
                new HttpUtil().callJson(handler, 1, this, GlobalConfig.MAPPMAIN_CHECKAPPUPDATE, "AppUserID", GlobalConfig.AppUserID, "AppID", GlobalConfig.AppID, "VersionType", "1", "CurVersion", GlobalConfig.newVersion);
                break;

        }
    }



    /**
     * 点击效果,监听事件
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Logger.d("width:" + v.getMeasuredWidth() + ",height:" + v.getMeasuredHeight() + ",X:" + event.getX() + ",Y:" + event.getY() + ",vwidth:" + v.getWidth() + ",vheight:" + v.getHeight());
        //登录
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                ischeck = true;
                switch (v.getId()) {
                    case R.id.unlisted_img:
                        //点击
                        v.setAlpha(0.7f);
                        break;
                    default:
                        GradientDrawable p1 = (GradientDrawable) v.getBackground();
                        p1.setAlpha(200);
                        p1.setCornerRadius(8.0f);
                        p1.setColor(themeColor);
                        break;
                }
                break;


            case MotionEvent.ACTION_MOVE:
                Logger.d("outside:" + v.getId());
                if (event.getX() > 0 && event.getX() < v.getMeasuredWidth() && event.getY() > 0 && event.getY() < v.getMeasuredHeight()) {
                    ischeck = true;
                } else {
                    ischeck = false;
                    switch (v.getId()) {
                        case R.id.unlisted_img:
                            v.setAlpha(1.0f);
                            break;

                        default:
                            GradientDrawable p2 = (GradientDrawable) v.getBackground();
                            p2.setAlpha(255);
                            p2.setCornerRadius(8.0f);
                            p2.setColor(themeColor);
                            break;
                    }

                }


                break;

            case MotionEvent.ACTION_UP:

                //判断点击登录
                if (ischeck) {
                    //点击登录进入登录界面
                    startActivity(new Intent(this, LoginActivity.class));
                }
                switch (v.getId()) {
                    case R.id.unlisted_img:
                        v.setAlpha(1.0f);
                        break;
                    default:
                        GradientDrawable p2 = (GradientDrawable) v.getBackground();
                        p2.setAlpha(255);
                        p2.setCornerRadius(8.0f);
                        p2.setColor(themeColor);
                        break;
                }
                break;
        }
        return true;
    }



    @Override
    public void setOnLeftClick() {
        finish();
    }



    @Override
    public void setOnRightClick() {

    }
}
