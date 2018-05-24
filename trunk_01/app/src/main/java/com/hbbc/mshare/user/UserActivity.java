package com.hbbc.mshare.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.mshare.AboutUsActivity;
import com.hbbc.mshare.CustomerServiceActivity;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.message.MessageActivity;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.ToastUtil;

/**
 * Created by Administrator on 2017/7/5.
 *
 */
public class UserActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {


    private TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_user);

        initView();


    }



    @Override
    protected void initView() {

        MyTopbar topbar= (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);
        ImageView iv_user= (ImageView) findViewById(R.id.iv_user);
        tv_name = (TextView) findViewById(R.id.tv_name);
        findViewById(R.id.btn_sign_out).setOnClickListener(this);

        LinearLayout ll_guide= (LinearLayout) findViewById(R.id.ll_guide);
        LinearLayout ll_message= (LinearLayout) findViewById(R.id.ll_message);
        LinearLayout ll_contact= (LinearLayout) findViewById(R.id.ll_contact);
        LinearLayout ll_about_us= (LinearLayout) findViewById(R.id.ll_about_us);
        LinearLayout ll_withdraw= (LinearLayout) findViewById(R.id.ll_withdraw);

        ll_guide.setOnClickListener(this);
        ll_message.setOnClickListener(this);
        ll_contact.setOnClickListener(this);
        ll_about_us.setOnClickListener(this);
        iv_user.setOnClickListener(this);
        ll_withdraw.setOnClickListener(this);

        tv_name.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.iv_user://更换头像
                if(UserDao.getDaoInstance(this).query()!=null)
                    changeUserIcon();
                else
                    ToastUtil.toast("请先登陆,再上传头像");
                return;
//                break;
            case R.id.ll_guide:
                intent.setClass(this,GuideActivity.class);
                break;
            case R.id.ll_message:
                intent.setClass(this,MessageActivity.class);
                break;
            case R.id.ll_contact:
                intent.setClass(this,CustomerServiceActivity.class);
                break;
            case R.id.ll_about_us:
                intent.setClass(this,AboutUsActivity.class);
                break;
            case R.id.tv_name:
                if(UserDao.getDaoInstance(this).query()==null)
                    intent.setClass(this, LoginActivity.class);
                else
                    return;
                break;
            case R.id.btn_sign_out://登出
                signOut();
                return;
//                break;
            case R.id.ll_withdraw:
                intent.setClass(this,WithDrawActivity.class);
                break;

        }
        startActivity(intent);
        overridePendingTransition(R.anim.global_in,0);
    }

    /**
     * 登出操作
     */
    private void signOut() {

        final UserDao dao = UserDao.getDaoInstance(this);
        LoginResultBean currentUser = dao.query();
        if(currentUser==null){
            ToastUtil.toast("你还未登陆");
        }else {
             new AlertDialog.Builder(this)
                    .setMessage("真的要注销当前用户吗?")
                    .setNegativeButton("再想想", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dao.resetAllUsersStatus();
                            tv_name.setText(queryCurrentUserName());
                            ToastUtil.toast("注销成功");

                        }
                    })
                     .setCancelable(false)
                     .show();


        }
    }



    @Override
    protected void onResume() {

        super.onResume();
        tv_name.setText(queryCurrentUserName());
    }



    /**
     * 改变用户头像
     */
    private void changeUserIcon() {

        ToastUtil.toast( "改变头像");

    }



    @Override
    public void setOnLeftClick() {
        finish();
    }



    @Override
    public void setOnRightClick() {

    }



    /**
     * 查询当前用户的手机号绑定到tv_name
     */
    private String queryCurrentUserName(){

        LoginResultBean currentUser = UserDao.getDaoInstance(this).query();
        if(currentUser==null||TextUtils.isEmpty(currentUser.getPhoneNumber()))
            return "点击此处登陆";
        else
            return currentUser.getPhoneNumber();
    }



}
