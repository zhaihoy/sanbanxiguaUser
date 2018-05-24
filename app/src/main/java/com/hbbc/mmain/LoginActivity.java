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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.mpush.GetuiUtil;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyCircleImageView;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    private static String PersonFlag = "1";                                   //登录身份标识

    private MyCircleImageView login_image;                                 //标题图片

    private ImageView login_back;                                           //返回

    private TextView login_title;                                           //标题

    private Button login_member, login_admin, login_validate, login_btn;    //会员登录，管理员登录，验证码,登录

    private EditText login_edit_username, login_edit_password;              //账号，密码

    private TextView login_register, login_number, login_TechnicalSupport;   //立即申请,详情电话,技术支持描述信息显示

    private int themeColor;                                                  //主题色

    private int second;                                                        //记录秒数

    //判断是否为点击
    private boolean ischeck;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mmain_login_main);

        //初始化控件
        initView();
    }



    /**
     * 初始化控件
     */
    @Override
    protected void initView() {

        login_back = (ImageView) findViewById(R.id.login_back);
        login_image = (MyCircleImageView) findViewById(R.id.login_image);
        login_title = (TextView) findViewById(R.id.login_title);
        login_member = (Button) findViewById(R.id.login_member);
        login_admin = (Button) findViewById(R.id.login_admin);
        login_validate = (Button) findViewById(R.id.login_validate);
        login_btn = (Button) findViewById(R.id.login_btn);
        login_edit_username = (EditText) findViewById(R.id.login_edit_username);
        login_edit_password = (EditText) findViewById(R.id.login_edit_password);
        login_register = (TextView) findViewById(R.id.login_register);
        login_number = (TextView) findViewById(R.id.login_number);
        login_TechnicalSupport = (TextView) findViewById(R.id.login_TechnicalSupport);

        //设置控件监听事件
        login_back.setOnClickListener(this);
        login_admin.setOnClickListener(this);
        login_member.setOnClickListener(this);
        login_register.setOnClickListener(this);
        login_number.setOnClickListener(this);


        login_btn.setOnTouchListener(this);
        login_validate.setOnTouchListener(this);
        //初始化主题
        initthemeColor();
    }



    /**
     * 消息回调机制
     */
    @Override
    protected void getMessage(Message msg) {

        switch (msg.what) {
            case 0:
                //登录
                doLogin((String) msg.obj);
                break;
            case -1:

                String data = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String notice = jsonObject.getString("Notice");
                    if (jsonObject.getBoolean("Result")) {
                        setValidateCode();
                    } else {
                        //失败时显示错误信息
                        outputShort(notice);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case 5:
                try {
                    JSONObject jsonObject = new JSONObject((String) msg.obj);
                    if (jsonObject.getBoolean("Result")) {
                        //个推ClientID记录ID(用于解绑用户身份)
                        GlobalParameter.setGwgtid(jsonObject.getInt("GWGTID") + "");
                    } else {
                        outputShort(jsonObject.getString("Notice"));
                    }
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
        themeColor = GlobalParameter.getThemeColor();

        //更改back图片中的颜色
        login_back.setColorFilter(themeColor);

        //标题颜色
        login_title.setTextColor(themeColor);

        //通过isRigester来判断是否隐藏立即申请按钮 1.显示，2.隐藏
        if (!GlobalConfig.isRegister.equals("1"))
            login_register.setVisibility(View.GONE);

        //设置立即申请背景颜色
        login_register.setTextColor(themeColor);

        //手机号码不为空时显示
        if (GlobalConfig.HotLine.equals("") || TextUtils.isEmpty(GlobalConfig.HotLine)) {
            login_number.setVisibility(View.GONE);
        } else {
            SpannableString spannable = new SpannableString("详情致电 " + GlobalConfig.HotLine);
            //设置下划线
            spannable.setSpan(new UnderlineSpan(), 5, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(themeColor), 5, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            login_number.setText(spannable);
        }

        //当有技术支持描述信息显示
        if (!GlobalConfig.TechnicalSupport.equals("") && !TextUtils.isEmpty(GlobalConfig.TechnicalSupport)) {
            login_TechnicalSupport.setText(GlobalConfig.TechnicalSupport);
        }
        //设置标题图
        login_image.setBorderColor(themeColor);
        login_image.setBorderWidth(2);
        Glide.with(this).load(GlobalConfig.ThemePicFileID).into(login_image);

        //当管理和会员都为空时，隐藏按钮
        if (GlobalConfig.MemberNickName.equals("") || GlobalConfig.ManagerNickName.equals("")) {

            if (GlobalConfig.ManagerNickName.equals("")) {
                PersonFlag = "2";
            } else {
                PersonFlag = "1";
            }
            login_admin.setVisibility(View.GONE);
            login_member.setVisibility(View.GONE);
        } else {
            login_member.setText(GlobalConfig.MemberNickName + "登录");
            login_admin.setText(GlobalConfig.ManagerNickName + "登录");
        }

        //管理员
        setBackGroundColor(login_admin, themeColor, 8.0f, 0);
        login_admin.setTextColor(Color.WHITE);


        //会员
        login_member.setTextColor(themeColor);
        GradientDrawable p = (GradientDrawable) login_member.getBackground();
        p.setColor(Color.WHITE);                                               //背景色
        p.setStroke(2, themeColor);                                            //边框颜色及宽度
        p.setCornerRadius(8.0f);                                                //圆角半径


        //登录按钮颜色
        setBackGroundColor(login_btn, themeColor, 8.0f, 0);
        login_btn.setTextColor(Color.WHITE);

        //验证码背景色
        setBackGroundColor(login_validate, themeColor, 8.0f, 0);
        login_validate.setTextColor(Color.WHITE);

    }



    /**
     * 控件监听事件
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_back:
                finish();//退出当前页面
                break;

            case R.id.login_admin:
                //管理员
                login_admin.setTextColor(Color.WHITE);
                GradientDrawable p1 = (GradientDrawable) login_admin.getBackground();
                p1.setStroke(2, themeColor);                                             //边框颜色及宽度
                p1.setColor(themeColor);                                                //背景色
                p1.setCornerRadius(8.0f);                                               //圆角半径

                //会员
                login_member.setTextColor(themeColor);
                GradientDrawable p2 = (GradientDrawable) login_member.getBackground();
                p2.setColor(Color.WHITE);                                                   //背景色
                p2.setStroke(2, themeColor);                                                //边框颜色及宽度
                p2.setCornerRadius(8.0f);                                                   //圆角半径

                PersonFlag = "1";                                                         //更换身份标识为管理员
                break;

            case R.id.login_member:
                //管理员
                login_admin.setTextColor(themeColor);
                GradientDrawable p3 = (GradientDrawable) login_admin.getBackground();
                p3.setColor(Color.WHITE);
                p3.setStroke(2, themeColor);
                //会员
                login_member.setTextColor(Color.WHITE);
                GradientDrawable p4 = (GradientDrawable) login_member.getBackground();
                p4.setStroke(2, themeColor);
                p4.setCornerRadius(8.0f);
                p4.setColor(themeColor);

                PersonFlag = "2";                                                         //更换身份标识为会员
                break;

            case R.id.login_register:
                //立即注册页面
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.login_number:
                //需更换
                setAlertDialog(GlobalConfig.HotLine);
                break;
        }
    }



    /**
     * 点击效果,监听事件，验证码
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ischeck = true;
                GradientDrawable p1 = (GradientDrawable) v.getBackground();
                p1.setAlpha(200);
                p1.setCornerRadius(8.0f);
                p1.setColor(themeColor);
                break;


            case MotionEvent.ACTION_MOVE:
                if (!(event.getX() > 0 && event.getX() < v.getMeasuredWidth() && event.getY() > 0 && event.getY() < v.getMeasuredHeight())) {
                    ischeck = false;
                    GradientDrawable p2 = (GradientDrawable) v.getBackground();
                    p2.setAlpha(255);
                    p2.setCornerRadius(8.0f);
                    p2.setColor(themeColor);
                }
                break;

            case MotionEvent.ACTION_UP:
                //判断点击登录
                if (ischeck) {
                    if (v.getId() == R.id.login_btn) {
                        doLogin();
                    } else {
                        //获取验证码
                        setValidate();
                    }

                }
                GradientDrawable p3 = (GradientDrawable) v.getBackground();
                p3.setAlpha(255);
                p3.setCornerRadius(8.0f);
                p3.setColor(themeColor);
                break;

        }

        return false;
    }



    /**
     * 调用验证码接口
     */
    private void setValidate() {

        if (isMobileNO(login_edit_username.getText().toString())) {
            new HttpUtil().callJson(handler, -1, this, GlobalConfig.MAPPMAIN_GETSMSVERIFYCODE, "AppUserID", GlobalConfig.AppUserID, "PhoneNum", login_edit_username.getText().toString());
        } else {
            outputLong("请输入正确的手机号");
        }
    }



    /**
     * 获取验证码
     */
    private void setValidateCode() {
        //换验证码背景颜色
        setBackGroundColor(login_validate, Color.GRAY, 8.0f, 0);
        //设置验证码不可点击
        login_validate.setEnabled(false);
        //默认60秒
        second = 60;
        //改变验证码秒数
        handler.post(new Runnable() {
            @Override
            public void run() {

                if (second <= 0) {
                    //换验证码背景颜色
                    setBackGroundColor(login_validate, themeColor, 8.0f, 0);
                    login_validate.setText("获取验证码");
                    login_validate.setEnabled(true);
                } else {
                    second--;
                    login_validate.setText(second + "秒后重试");
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }



    /**
     * 验证手机号
     */
    private boolean isMobileNO(String mobiles) {

        Pattern p = Pattern.compile("1\\d{10}");
        Matcher m = p.matcher(mobiles);
        return m.matches();
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
     * 设置背景色
     */
    private void setBackGroundColor(View view, int themecolor, float conRadius, int strokew) {

        GradientDrawable gradient = (GradientDrawable) view.getBackground();
        gradient.setColor(themecolor);
        gradient.setCornerRadius(conRadius);
        gradient.setStroke(strokew, themecolor);
    }



    /**
     * 登录后的业务逻辑
     */
    private void doLogin(String obj) {

        try {
            JSONObject jsonObject = new JSONObject(obj);
            boolean Result = jsonObject.getBoolean("Result");
            String Notice = jsonObject.getString("Notice");
            if (Result) {
                //登录成功的业务逻辑
                outputLong("登录成功");
                if (jsonObject.has("MemberID")) {
                    GlobalParameter.setMemberid(jsonObject.getString("MemberID"));
                }
                if (jsonObject.has("ManagerID")) {
                    GlobalParameter.setManagerid(jsonObject.getString("ManagerID"));
                }
                if (jsonObject.has("Name")) {
                    GlobalParameter.setName(jsonObject.getString("Name"));
                }
                if (jsonObject.has("HeadPicFieldID")) {
                    GlobalParameter.setHeadpicfield(jsonObject.getString("HeadPicFieldID"));
                }
                if (jsonObject.has("LoginKey")) {
                    Logger.d("LoginKey:" + jsonObject.getString("LoginKey"));
                    GlobalParameter.setLoginkey(jsonObject.getString("LoginKey"));
                }
                //设置用户身份
                GlobalParameter.setPersonflag(PersonFlag);
                //存储手机号
                GlobalParameter.setPhonenum(login_edit_username.getText().toString());
                //开启个推
                openGetui();
                //跳转到个人信息页面
                startActivity(new Intent(this, InformActivity.class));
            } else {
                //登录失败的业务逻辑
                outputLong(Notice);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**
     * 登录
     */
    private void doLogin() {
        //手机号是否为空
        boolean isPhoneNumber = login_edit_username.getText().equals("") || TextUtils.isEmpty(login_edit_username.getText().toString());
        //验证码是否为空
        boolean isPhonePassWord = login_edit_password.getText().equals("") || TextUtils.isEmpty(login_edit_password.getText().toString());
        //手机号小于11位
        boolean phonenumber = login_edit_username.getText().toString().length() < 11;
        //当手机号和验证码为空时，提示用户
        if (isPhoneNumber) {
            outputLong("请输入手机号");
        } else if (phonenumber) {
            outputLong("请输入正确的手机号");
        }

        if (!isPhoneNumber && isPhonePassWord) {
            outputLong("请输入短信验证码");
        }

        //验证登录
        if (!isPhoneNumber && !isPhonePassWord && !phonenumber) {
            new HttpUtil().callJson(handler, 0, this, GlobalConfig.MAPPMAIN_DOLOGIN, "AppUserID", GlobalConfig.AppUserID, "PhoneNum", login_edit_username.getText().toString(), "PersonFlag", PersonFlag, "TempVerifyCode", login_edit_password.getText().toString());
        }

    }



    /**
     * 开启个推
     */
    private void openGetui() {
        //开启个推
        GetuiUtil.turnonGetui();

        //绑定用户身份标识
        new HttpUtil().callJson(handler, 5, this, GlobalConfig.MPUSH_BINDUSERCLIENTID, "AppUserID", GlobalConfig.AppUserID, "ECID", GlobalConfig.ECID + "", "PushID", GlobalConfig.PushID, "UserType", GlobalParameter.getPersonflag(), "UserID", GlobalParameter.getManagerid() + GlobalParameter.getMemberid(), "UserName", GlobalParameter.getName(), "PhoneNum", GlobalParameter.getPhonenum(), "GTClientID", GetuiUtil.getClientID(), "DeviceType", "android");

    }
}
