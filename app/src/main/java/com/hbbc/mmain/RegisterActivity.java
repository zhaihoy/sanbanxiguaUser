package com.hbbc.mmain;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 注册界面
 */
public class RegisterActivity extends BaseActivity implements View.OnTouchListener, MyTopbar.OnTopBarClickListener {

    //标题，提示,联系电话,注册说明,技术支持
    private TextView register_text, register_number, RegisterExplain, register_TechnicalSupport;

    private EditText register_et_username, register_et_password;                   //账号，密码

    private LinearLayout mshell_register_issure;                                  //提示布局

    private MyTopbar register_include;

    private Button register_btn, register_btn_sure, register_btn_cancel;            //立即注册,是,否按钮

    private int themeColor;                                                         //主题色

    private String UnderstandFlag = "1";                                            //默认为了解

    //判断是否为点击
    private boolean ischeck;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mmain_register_main);

        //初始化控件
        initView();

    }



    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        register_text = (TextView) findViewById(R.id.register_text);
        register_et_username = (EditText) findViewById(R.id.register_et_username);
        register_et_password = (EditText) findViewById(R.id.register_et_password);
        mshell_register_issure = (LinearLayout) findViewById(R.id.mshell_register_issure);
        register_btn = (Button) findViewById(R.id.register_btn);
        register_btn_sure = (Button) findViewById(R.id.register_btn_sure);
        register_btn_cancel = (Button) findViewById(R.id.register_btn_cancel);
        register_number = (TextView) findViewById(R.id.register_number);
        RegisterExplain = (TextView) findViewById(R.id.RegisterExplain);
        register_include = (MyTopbar) findViewById(R.id.register_include);
        register_TechnicalSupport = (TextView) findViewById(R.id.register_TechnicalSupport);

        //初始化主题
        initthemeColor();

        //设置监听事件
        register_btn_sure.setOnClickListener(this);
        register_btn_cancel.setOnClickListener(this);
        register_include.setOnTopBarClickListener(this);
        register_text.setOnClickListener(this);
        register_number.setOnClickListener(this);

        //立即注册
        register_btn.setOnTouchListener(this);
    }



    /**
     * 消息回调机制
     */
    @Override
    protected void getMessage(Message msg) {
        switch (msg.what) {
            case 0:
                String msgdata = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(msgdata);
                    outputShort(jsonObject.getString("Notice"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }



    /**
     * 初始化主题
     */
    private void initthemeColor() {

        //主题色赋值
        themeColor= GlobalParameter.getThemeColor();

        //设置top颜色
        register_include.setBackgroundColor(themeColor);

        register_include.setLeftColorFilter(Color.WHITE);              //back键颜色

        register_include.setTitleColor(Color.WHITE);                //标题字体颜色
        register_include.setTitle("注册申请");

        //注册按钮背景色
        GradientDrawable p1 = (GradientDrawable) register_btn.getBackground();
        p1.setStroke(2, themeColor);
        p1.setCornerRadius(8.0f);
        p1.setColor(themeColor);

        //是否
        GradientDrawable p = (GradientDrawable) register_et_username.getBackground();
        p.setCornerRadius(8.0f);
        p.setStroke(2, themeColor);
        GradientDrawable p2 = (GradientDrawable) register_et_password.getBackground();
        p2.setCornerRadius(8.0f);
        p2.setStroke(2, themeColor);

        if (!GlobalConfig.TechnicalSupport.equals("") && !TextUtils.isEmpty(GlobalConfig.TechnicalSupport)) {
            register_TechnicalSupport.setText(GlobalConfig.TechnicalSupport);
        }

        if (GlobalConfig.ProtocolDescribe.equals("") || TextUtils.isEmpty(GlobalConfig.ProtocolDescribe)) {
            mshell_register_issure.setVisibility(View.INVISIBLE);
        } else {
            GradientDrawable p3 = (GradientDrawable) register_btn_sure.getBackground();
            p3.setColor(themeColor);
            p3.setCornerRadius(8.0f);
            register_btn_sure.setTextColor(Color.WHITE);

            GradientDrawable p4 = (GradientDrawable) register_btn_cancel.getBackground();
            p4.setCornerRadius(8.0f);
            p4.setStroke(2, themeColor);
            p4.setColor(Color.WHITE);
            register_btn_cancel.setTextColor(themeColor);

            register_text.setText(GlobalConfig.ProtocolDescribe);

            //协议加下划线
            if (!GlobalConfig.ProtocolKeyword.equals("") && !TextUtils.isEmpty(GlobalConfig.ProtocolKeyword)) {
                int start = GlobalConfig.ProtocolDescribe.indexOf(GlobalConfig.ProtocolKeyword);
                int end = start + GlobalConfig.ProtocolKeyword.length();
                SpannableString spannableString = new SpannableString(GlobalConfig.ProtocolDescribe);
                spannableString.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(themeColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                register_text.setText(spannableString);
            }
        }

        //手机号码不为空时显示
        if (GlobalConfig.HotLine.equals("") || TextUtils.isEmpty(GlobalConfig.HotLine)) {
            register_number.setVisibility(View.GONE);
        } else {
            SpannableString spannable = new SpannableString("详情致电 " + GlobalConfig.HotLine);
            //设置下划线
            spannable.setSpan(new UnderlineSpan(), 5, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(themeColor), 5, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            register_number.setText(spannable);
        }

        //注册说明
        if (!GlobalConfig.RegisterExplain.equals("") && TextUtils.isEmpty(GlobalConfig.RegisterExplain)) {
            RegisterExplain.setText("*" + GlobalConfig.RegisterExplain);
        }
    }



    /**
     * 监听事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.register_btn_cancel:
                GradientDrawable p1 = (GradientDrawable) register_btn_sure.getBackground();
                p1.setCornerRadius(8.0f);
                p1.setStroke(2, themeColor);
                p1.setColor(Color.WHITE);
                register_btn_sure.setTextColor(themeColor);

                GradientDrawable p2 = (GradientDrawable) register_btn_cancel.getBackground();
                p2.setColor(themeColor);
                p2.setCornerRadius(8.0f);
                register_btn_cancel.setTextColor(Color.WHITE);
                UnderstandFlag = "0";
                break;

            case R.id.register_btn_sure:
                GradientDrawable p3 = (GradientDrawable) register_btn_sure.getBackground();
                p3.setColor(themeColor);
                p3.setCornerRadius(8.0f);
                register_btn_sure.setTextColor(Color.WHITE);

                GradientDrawable p4 = (GradientDrawable) register_btn_cancel.getBackground();
                p4.setCornerRadius(8.0f);
                p4.setStroke(2, themeColor);
                p4.setColor(Color.WHITE);
                register_btn_cancel.setTextColor(themeColor);
                UnderstandFlag = "1";
                break;

            case R.id.register_text:
                //协议跳转
                break;

            case R.id.register_number:
                setAlertDialog(GlobalConfig.HotLine);
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
                ischeck = true;
                GradientDrawable p1 = (GradientDrawable) register_btn.getBackground();
                p1.setAlpha(200);
                p1.setCornerRadius(8.0f);
                p1.setColor(themeColor);
                break;


            case MotionEvent.ACTION_MOVE:
                if (!(event.getX() > 0 && event.getX() < v.getMeasuredWidth() && event.getY() > 0 && event.getY() < v.getMeasuredHeight())) {
                    ischeck = false;
                    GradientDrawable p2 = (GradientDrawable) register_btn.getBackground();
                    p2.setAlpha(255);
                    p2.setCornerRadius(8.0f);
                    p2.setColor(themeColor);
                }
                break;

            case MotionEvent.ACTION_UP:
                //判断点击登录
                if (ischeck) {
                    doRegister();
                }
                GradientDrawable p3 = (GradientDrawable) register_btn.getBackground();
                p3.setAlpha(255);
                p3.setCornerRadius(8.0f);
                p3.setColor(themeColor);
                break;
        }
        return true;
    }



    /**
     * 点击详情致电，弹出对话框
     */
    private void setAlertDialog(final String phone) {
        final AlertDialog alert;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.mmain_login_number, null);
        //初始化Dialog中的控件
        TextView mTvNumber = (TextView) view.findViewById(R.id.number);
        TextView mTvCancel = (TextView) view.findViewById(R.id.numberCancel);
        mTvNumber.setText("拨打电话：  " + phone);
        alertDialog.setView(view);
        alert = alertDialog.create();
        alert.show();
        //设置监听
        mTvNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到打电话界面
                Uri uri = Uri.parse("tel:" + phone);
                startActivity(new Intent(Intent.ACTION_DIAL, uri));
                alert.cancel();
            }
        });
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }



    /**
     * 判断点击登录
     */
    private void doRegister() {
        //判断是否为空
        boolean isUsername = register_et_username.getText().toString().equals("") || TextUtils.isEmpty(register_et_username.getText().toString());
        boolean isPassword = register_et_password.getText().toString().equals("") || TextUtils.isEmpty(register_et_password.getText().toString());
        if (isUsername) {
            outputShort("请输入姓名");
        }

        if (isPassword && !isUsername) {
            outputShort("请输入手机号");
        } else if (!isPassword && register_et_password.getText().toString().length() < 11) {
            outputShort("请输入正确的手机号");
        }

        //调用注册接口,当用户输入不为空时调用接口
        if (!isPassword && !isUsername) {
            new HttpUtil().callJson(handler, 0, this, GlobalConfig.MAPPMAIN_DOREGISTER, "AppUserID", GlobalConfig.AppUserID, "PhoneNum", register_et_password.getText().toString(), "UserName", register_et_username.getText().toString(), "UnderstandFlag", UnderstandFlag);
        }
    }



    @Override
    public void setOnLeftClick() {
        finish();
    }



    @Override
    public void setOnRightClick() {

    }
}
