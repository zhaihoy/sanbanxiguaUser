package com.hbbc.mshare.sharer;

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
import com.hbbc.mshare.sharer.login.LoginActivity;
import com.hbbc.mshare.user.GuideActivity;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.ToastUtil;

/**
 * Created by Administrator on 2017/7/5.
 *
 */
public class UserActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    private TextView tv_name;
    private static long prevTimemillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharer_user);

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

        ll_guide.setOnClickListener(this);
        ll_message.setOnClickListener(this);
        ll_contact.setOnClickListener(this);
        ll_about_us.setOnClickListener(this);
        iv_user.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(System.currentTimeMillis()-prevTimemillis < GlobalConfig.Button_Press_Interval){
            return;
        }
        prevTimemillis = System.currentTimeMillis();

        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.iv_user://更换头像
                if(UserDao.getDaoInstance(this).query()!=null)
                    changeUserIcon();
                else{
                    intent.setClass(this, LoginActivity.class);
                    startActivity(intent);
                }
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
            case R.id.btn_sign_out://登出,车主端如果退出登陆,直接关闭掉APP
                signOut();
                return;

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
                    .setTitle("真的要注销当前用户吗?")
                    .setNegativeButton("再想想", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dao.resetAllUsersStatus();
//                            tv_name.setText(queryCurrentUserName());
//                            ToastUtil.toast("注销成功");

                            //todo 需要把打开的所有activity全部关闭,然后打开LoginActivity
//                            Process.killProcess(Process.myPid());
                            //2.用下面的这个偏方就可解决
                            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);//此两个标记必须连用!
                            startActivity(intent);
                            System.exit(0);
//                            Process.killProcess(Process.myPid());
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
            return "点击头像登陆";
        else
            return currentUser.getPhoneNumber();
    }



}
