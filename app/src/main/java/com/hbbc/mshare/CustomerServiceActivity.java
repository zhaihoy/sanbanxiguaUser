package com.hbbc.mshare;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hbbc.R;
import com.hbbc.mshare.login.BaseResultBean;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.ToastUtil;

/**
 * Created by Administrator on 2017/7/19.
 *
 */
public class CustomerServiceActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {


    private EditText et_message;
    private HttpUtil httpUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_contact_service);
        initView();
    }



    @Override
    protected void initView() {

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        et_message = (EditText) findViewById(R.id.et_input);
        Button btn_submit = (Button) findViewById(R.id.btn_submit);

        topbar.setOnTopBarClickListener(this);
        btn_submit.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {

        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_submit://判断并提交给服务器信息
                submitSuggestion();
                break;
        }

    }



    /**
     * 联网提交信息
     */
    private void submitSuggestion() {

        String suggestion = et_message.getText().toString().trim();
        LoginResultBean currentUser = UserDao.getDaoInstance(this).query();
        if(currentUser==null) {
            ToastUtil.toast("请先登陆");
            return;
        }
        if(TextUtils.isEmpty(suggestion)||suggestion.length()<10){
            ToastUtil.toast("请至少输入十个以上文字");
            return;
        }

        if(httpUtil==null)
            httpUtil=new HttpUtil();
        String phoneNumber = currentUser.getPhoneNumber();
        String[] params = new String[]{Constants.AppID,GlobalConfig.AppID,Constants.ECID, GlobalConfig.ECID,
                Constants.PhoneNumber,phoneNumber, Constants.CSFeedback,suggestion,Constants.AppType,GlobalConfig.AppType};

        String url = (GlobalConfig.AppType.equals("1"))?(GlobalConfig.SERVERROOTPATH+GlobalConfig.SHARER_CUSTOMER_SERVICE):(GlobalConfig.SERVERROOTPATH+GlobalConfig.MSHARE_CUSTOMER_SERVICE);

        httpUtil.callJson(handler,this,url, BaseResultBean.class,params);

    }



    @Override
    protected void getMessage(Message msg) {
        BaseResultBean result= (BaseResultBean) msg.obj;
        if(result==null||!result.getResult()){
            ToastUtil.toast("提交失败,请稍后重试");
            return;
        }
        ToastUtil.toast("提交成功,我们会尽快处理*_*");
        finish();

    }



    @Override
    public void setOnLeftClick() {
        finish();
    }



    @Override
    public void setOnRightClick() {

    }
}
