package com.hbbc.mshare.sharer.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hbbc.R;
import com.hbbc.mshare.ProtocolActivity;
import com.hbbc.mshare.login.BaseResultBean;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.sharer.MainActivity;
import com.hbbc.mshare.sharer.authentication.AuthenticationActivity;
import com.hbbc.mshare.sharer.deposit.DepositActivity;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MShareUtil;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.ToastUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2017/7/11.
 * 登陆注册还是数据库来保存
 *
 * 逻辑:
 *  如果提交成功,进行下一步判断:
 *1.是否跳转到实名认证页 . 如果跳转后,用户不认证,默认返回到主页面,如果想租赁,判断是否已认证
 *2.押金支付页是同样的道理.但是可能存在不需要实名认证的情况.甚至不需要交纳押金!即可开始租赁!!!
 *
 * #另: 在此Activity将AppId注册到微信客户端
 *
 */
public class LoginActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    private static final int GET_VERIFICATION_CODE = 0;

    private static final int SUBMIT_INFO_TO_SERVER = 1;

    private EditText et_number;

    private EditText et_code;

    private Button btn_request_code;

    private Button btn_submit;

    private Button btn_contract;

    private HttpUtil httpUtil;//用的时候再new出来

    private int START_NUMBER = 60;//倒计时从60开始

    //
    private IWXAPI api ;

    @Override
    protected void getMessage(Message msg) {

        switch (msg.what) {
            case GET_VERIFICATION_CODE:
                BaseResultBean bean = (BaseResultBean) msg.obj;
                handleVerificationResult(bean);
                break;
            case SUBMIT_INFO_TO_SERVER:
                LoginResultBean loginResultBean = (LoginResultBean) msg.obj;
                handleLoginResult(loginResultBean);
                break;
        }
    }

    /**
     * 处理登陆结果
     */
    private void handleLoginResult(LoginResultBean loginResultBean) {

        if (loginResultBean == null || !loginResultBean.getResult()) {
            ToastUtil.toast("登陆失败,请重试");
            return;
        }

        Log.d("LoginActivity", "handleLoginResult: LoginActivity---->"+loginResultBean.toString());

        //同样存储App的name
        GlobalConfig.AppName = loginResultBean.getAppName();
        Log.d("tag", "handleLoginResult: appname--->"+GlobalConfig.AppName);
        boolean result = saveTheBean(loginResultBean);//存数据库,后续或许可以试试加密数据库

        if(result){//如果存成功或更新成功了,执行查询操作,以判断下一步是否开启实名认证页,
            LoginResultBean resultBean = UserDao.getDaoInstance(this).query();
            if(resultBean!=null){
                // 开启实名认证：1、开启；代表此页面是登陆过程中的一部分,必须完成后,才能租赁
                //               2、关闭；关闭:表示此页面不需要出现!!!
                if(resultBean.getOpenCertification()==1&&resultBean.getCertificationStatus()!=1){
                    startNextActivity(AuthenticationActivity.class);//开启认证页
                }else{
                    if(resultBean.getOpenDesposit()==1&&resultBean.getDespositStatus()==1)
                        startNextActivity(DepositActivity.class);//开启押金页
                    else
                        startNextActivity(MainActivity.class);
                }

                finish();//结束当前页面,返回主页!

            }else {

                ToastUtil.toast("操作异常,请重新登陆");//// TODO: 2017/11/2 应该在此处弹出了 ！返回结果是对的，不过是新的ＫＥＹ
//                ToastUtil.toast("操作异常,请重新登陆"+"查询结果为空");

            }

        }else{
            ToastUtil.toast("操作异常,请重新登陆");
//            ToastUtil.toast("操作异常,请重新登陆"+"保存失败");
        }

    }

    /**
     * TODO 保存自动登陆密钥<autoLoginKey>,如何保存这个密钥??? 参考其他有支付的类似应用是如何保存的
     *
     */

    private boolean saveTheBean(LoginResultBean bean) {

        resetAllUserStatus();
        bean.setCurrentUser(true);//把当前要登陆的用户置为默认登陆状态,即true.
        Log.d("tag", "handleLoginResult: result==="+bean.toString());
        boolean result;
        UserDao dao = UserDao.getDaoInstance(this);
        if (dao.contain(bean)) {//// TODO: 2017/8/4
            int id = dao.queryId(bean);
            bean.setId(id);
            result = dao.update(bean)!=0;

            Log.d("tag", "saveTheBean: 更新结果为"+result);
        } else {
            result= dao.insert(bean)!=-1;
            Log.d("tag", "saveTheBean: 插入结果为"+result);
        }

        return result;
    }


    /**
     * 存储/更新前,务必将所有用户的状态重新置为非默认用户(false)
     */
    private void resetAllUserStatus() {
        UserDao.getDaoInstance(this).resetAllUsersStatus();
    }


    /**
     * 处理发送验证码请求的结果: 发送失败,还是发送成功
     */
    private void handleVerificationResult(BaseResultBean bean) {

        String info;
        if (bean == null || !bean.getResult()) {
            info = "发送失败";
        } else {
            info = "发送成功,请查收";
        }
        ToastUtil.toast(info);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_login);
        registerAppIdToWeChat();
        checkDefaultUser();
        initView();



    }



    /**
     * 各微信客户端注册本应用的APPID
     */
    private void registerAppIdToWeChat() {
        api = WXAPIFactory.createWXAPI(this,GlobalConfig.WEIXIN_USER_APP_ID,false);
        api.registerApp(GlobalConfig.WEIXIN_USER_APP_ID);
    }



    private void checkDefaultUser() {
        UserDao userDao = UserDao.getDaoInstance(this);
        LoginResultBean currentUser = userDao.query();

        if(currentUser!=null&&currentUser.getPhoneNumber()!=null){

            if(currentUser.getOpenCertification()==1){
                if(currentUser.getCertificationStatus()!=1){
                    startNextActivity(AuthenticationActivity.class);//开启实名认证页
                }
            }

            if(currentUser.getOpenDesposit()==1){
                if(currentUser.getDespositStatus()!=2){
                    startNextActivity(DepositActivity.class);
                }
            }
            startNextActivity(MainActivity.class);
        }
    }



    @Override
    protected void initView() {

        MyTopbar topBar = (MyTopbar) findViewById(R.id.top_bar);
        topBar.setOnTopBarClickListener(this);

        et_number = (EditText) findViewById(R.id.et_phone_number);
        et_code = (EditText) findViewById(R.id.et_verify_code);
        btn_request_code = (Button) findViewById(R.id.btn_request_code);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_contract = (Button) findViewById(R.id.btn_contract);

        btn_contract.setOnClickListener(this);
        btn_request_code.setOnClickListener(this);
        btn_submit.setOnClickListener(this);


    }



    @Override
    public void setOnLeftClick() {

        finish();
    }



    @Override
    public void setOnRightClick() {

    }



    @Override
    public void onClick(View v) {

        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_contract:
                //查看协议信息
                Intent intent = new Intent(this, ProtocolActivity.class);
                intent.putExtra("protocol_type",0);
                startActivity(intent);
                overridePendingTransition(R.anim.global_in,R.anim.global_out);
                break;
            case R.id.btn_request_code:
                //请求获取验证码
                getVerificationCode();
                break;
            case R.id.btn_submit:
                //提交信息
                submit();
                break;

        }
    }



    private void startNextActivity(Class cls) {

        Intent intent = new Intent(this, cls);
        startActivity(intent);
        overridePendingTransition(R.anim.global_in, 0);
        finish();

    }



    /**
     * 向服务器提交手机号与验证码
     */
    private void submit() {

        String phone_number = et_number.getText().toString().trim();
        String verification_code = et_code.getText().toString().trim();
        if (!MShareUtil.isMobileNO(phone_number) || !MShareUtil.isVerificationLegal(verification_code)) {
            ToastUtil.toast("手机号或验证码格式不合法");
            return;
        }
        //todo :Caution 不要去查询旧的AutologinKey ,否则登陆不了.
        // 要去数据库里面查询,此手机号对应的用户是否之前已经登陆过:1.登陆过,查询出旧的AutoLoginKey; 2.未登陆过,AutoLoginKey="";
        LoginResultBean resultBean = UserDao.getDaoInstance(this).queryWithPhoneNumber(phone_number);
//        if(resultBean!=null && TextUtils.equals(resultBean.getPhoneNumber(),phone_number))
//            GlobalConfig.AutoLoginKey=resultBean.getAutoLoginKey();
//        else
//            GlobalConfig.AutoLoginKey="";

        if (httpUtil == null)
            httpUtil = new HttpUtil();
        String[] params = new String[]{Constants.AppID,GlobalConfig.AppID,Constants.ECID, GlobalConfig.ECID,
                Constants.PhoneNumber, phone_number, Constants.TempVerifyCode, verification_code,
                Constants.AutoLoginKey, "",
                Constants.AppType, GlobalConfig.AppType};
        httpUtil.callJson(handler, SUBMIT_INFO_TO_SERVER, this, GlobalConfig.SERVERROOTPATH + GlobalConfig.SHARER_LOGIN_IN,
                LoginResultBean.class, params);

    }


    /**
     * 60s内此发验证码请求Button不能再次点击
     */
    private void disableButton() {

        btn_request_code.setEnabled(false);

        btn_request_code.setText("60");
        new Thread() {
            @Override
            public void run() {

                try {
                    for (int i = 0; i < 60; i++) {
                        Thread.sleep(1000);
                        START_NUMBER--;
                        if (START_NUMBER == 0) {
                            resetNormalStatus();
                            return;
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                btn_request_code.setText("" + START_NUMBER);
                            }
                        });
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    resetNormalStatus();
                }
            }
        }.start();


    }



    /**
     * 即使出现异常,不能执行倒计时的情况下,也要恢复到未获取时的状态
     * 1.start_number=60
     * 2.button is enabled
     * 3.the text of button is "获取验证码"
     */
    private void resetNormalStatus() {

        handler.post(new Runnable() {
            @Override
            public void run() {

                btn_request_code.setEnabled(true);
                btn_request_code.setText("获取验证码");
                START_NUMBER = 60;
            }
        });
    }



    /**
     * 请求发送验证码
     */
    private void getVerificationCode() {

        String phone_number = et_number.getText().toString().trim();
        //判断下是不是手机号
        if (!MShareUtil.isMobileNO(phone_number)) {
            Toast.makeText(LoginActivity.this.getApplicationContext(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        //倒计时60s内disable
        disableButton();

        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }

        String[] params = new String[]{Constants.AppID,GlobalConfig.AppID,Constants.ECID, GlobalConfig.ECID + "", Constants.PhoneNumber, phone_number, Constants.AppType, GlobalConfig.AppType};
        //发起获取验证码请求
        httpUtil.callJson(handler, GET_VERIFICATION_CODE, this,
                GlobalConfig.MAPPMAIN_GETSMSVERIFYCODE, BaseResultBean.class, params);
    }

}
