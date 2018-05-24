package com.hbbc.mshare.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hbbc.R;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.user.main.MainActivity;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MShareUtil;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/7/11.
 *
 */
public class AuthenticationActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    private EditText et_name;

    private EditText et_id;

    private Button btn_submit;

    private MyTopbar topbar;

    private HttpUtil httpUtil;

    private long lastSubmitTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_authentication);
        initView();
    }



    @Override
    protected void initView() {

        topbar = (MyTopbar) findViewById(R.id.top_bar);
        et_name = (EditText) findViewById(R.id.et_name);
        et_id = (EditText) findViewById(R.id.et_code);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        topbar.setOnTopBarClickListener(this);

    }

    @Override
    public void onClick(View v) {

        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_submit:
                if(System.currentTimeMillis()-lastSubmitTime>5000L){
                    authenticate();
                    lastSubmitTime=System.currentTimeMillis();
                }
                //TODO to delete this section
                //copyDB2SDCard();
                break;
        }
    }

    private void copyDB2SDCard() {
        //找到文件的路径  /data/data/包名/databases/数据库名称
        File dbFile = new File(Environment.getDataDirectory().getAbsolutePath()+"/data/"+getPackageName()+"/databases/test.db");
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            //文件复制到sd卡中
            fis = new FileInputStream(dbFile);
            fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/copy.db");
            int len = 0;
            byte[] buffer = new byte[2048];
            while(-1!=(len=fis.read(buffer))){
                fos.write(buffer, 0, len);
            }
            fos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //关闭数据流
            try{
                if(fos!=null)fos.close();
                if (fis!=null)fis.close();
            }catch(IOException e){
                e.printStackTrace();
            }

        }
    }



    /**
     * 发起认证请求
     */
    private void authenticate() {

        String name = et_name.getText().toString().trim();
        String id = et_id.getText().toString().trim();

        //只允许输入二代身份证:18位,最后一位可能是字母的,也可能是数字.但二代身份证必须是18位

        if (TextUtils.isEmpty(name) || !MShareUtil.isIDLegal(id)) {
            ToastUtil.toast("请检查输入是否合法呀");
            return;
        }
        LoginResultBean currentProcessingUser = UserDao.getDaoInstance(this).queryCurrentProcessingUser();
        int verificationState = currentProcessingUser.getCertificationStatus();
        if(verificationState==1)
            ToastUtil.toast_debug("verification_state is true.*_*");
        else
            ToastUtil.toast_debug("verification_state is false.*<>*");
        //判断是否开启押金页面,两条路:1.主页 2.押金页

        Intent intent=new Intent();
        if (currentProcessingUser.getOpenDesposit() == 1 && currentProcessingUser.getDespositStatus() == 1) {//如果开启押金页面
            intent.setClass(this,DepositActivity.class);
        } else {
            intent.setClass(this, MainActivity.class);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.global_in, 0);
        finish();


    /*    if (httpUtil == null)
            httpUtil = new HttpUtil();

        UserDao userDao = UserDao.getDaoInstance(this);
        LoginResultBean currentUser = userDao.queryCurrentProcessingUser();
        String phoneNumber = currentUser.getPhoneNumber();

        String[] params = new String[]{Constants.AppID,GlobalConfig.AppID,Constants.ECID, GlobalConfig.ECID, Constants.PhoneNumber, phoneNumber,
                Constants.RealName, name, Constants.ID, id, Constants.AppType, GlobalConfig.AppType};

        httpUtil.callJson(handler, this, GlobalConfig.MSHARE_SERVER_ROOT + GlobalConfig.MSHARE_VERIFY_NUMBER_OF_ID_CARD,
                BaseResultBean.class, params);*/

    }

/*

    @Override
    protected void getMessage(Message msg) {

        BaseResultBean result = (BaseResultBean) msg.obj;
        if (result != null && result.getResult()) {

            boolean update_result = UserDao.getDaoInstance(this).updateVerificationState();

            ToastUtil.toast_debug("验证成功" + (update_result ?"true":"false"));
            // TODO: 2017/8/8
            LoginResultBean currentProcessingUser = UserDao.getDaoInstance(this).queryCurrentProcessingUser();
            int verificationState = currentProcessingUser.getCertificationStatus();
            if(verificationState==1)
                ToastUtil.toast_debug("verification_state is true.*_*");
            else
                ToastUtil.toast_debug("verification_state is false.*<>*");
            //判断是否开启押金页面,两条路:1.主页 2.押金页

            Intent intent=new Intent();
            if (currentProcessingUser.getOpenDesposit() == 1 && currentProcessingUser.getDespositStatus() == 1) {//如果开启押金页面
                intent.setClass(this,DepositActivity.class);
            } else {
                intent.setClass(this, MainActivity.class);
            }
            startActivity(intent);
            overridePendingTransition(R.anim.global_in, 0);
            finish();

        } else {
            ToastUtil.toast("验证失败,请重新验证");
        }

    }
*/


    @Override
    public void setOnLeftClick() {

        finish();
    }



    @Override
    public void setOnRightClick() {

    }
}
