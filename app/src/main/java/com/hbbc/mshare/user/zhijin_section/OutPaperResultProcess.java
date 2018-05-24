package com.hbbc.mshare.user.zhijin_section;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.hbbc.R;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.SPUtils;

/**
 * Created by Administrator on 2017/12/29.
 */
public class OutPaperResultProcess extends BaseActivity {

    private HttpUtil httpUtil;

    private ForFree_Bean forFreeBean;

    private UnLockBean unLockBean;

    private String payOrderId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gongyu_unlock_process);
        handler.sendEmptyMessageDelayed(0, 10000);
        requestNet();

    }



    private void requestNet() {


        //// TODO: 2017/12/
        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }

        LoginResultBean query = UserDao.getDaoInstance(OutPaperResultProcess.this).query();
        String phoneNumber = query.getPhoneNumber();
        //这里拿到订单的编号
        payOrderId = SPUtils.getString("payOrderId");
        System.out.println("================??====" + payOrderId);
        String[] params = new String[]{Constants.ECID, GlobalConfig.ECID, Constants.PhoneNumber, phoneNumber, Constants.AppType, GlobalConfig.AppType, Constants.AppID, GlobalConfig.AppID,
                Constants.PayOrderID, payOrderId};
        httpUtil.callJson(handler, this, GlobalConfig.SERVERROOTPATH + GlobalConfig.OPEANLOCKBYORDER, UnLockBean.class, params);
    }



    @Override
    protected void getMessage(Message msg) {

        try {
            unLockBean = (UnLockBean) msg.obj;
            System.out.println("======="+unLockBean.getResult());
            if (unLockBean.getResult()) {
                //就跳转到出纸成功的界面
                Intent intent = new Intent();
                intent.setClass(this, ForFreeOutPaperResult.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent();
                intent.setClass(this, FailOutPaperActivity.class);
                startActivity(intent);
                finish();
            }
            super.getMessage(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //操作成功的话也就是支付结果完全跟服务器返回的结果是一致性的

        /*    Intent intent = new Intent();
            intent.setClass(this, ForFreeOutPaperResult.class);
            startActivity(intent);
            finish()*/
        ;


    }
}



