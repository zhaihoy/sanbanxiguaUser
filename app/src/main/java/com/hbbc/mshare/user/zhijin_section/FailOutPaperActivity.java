package com.hbbc.mshare.user.zhijin_section;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.user.detail.Detail_Bean;
import com.hbbc.mshare.user.main.MainActivity;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.SPUtils;

/**
 * Created by Administrator on 2017/12/6.
 */
public class FailOutPaperActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, MyTopbar.OnTopBarClickListener {

    private static final String TAG = "BuyPaperActivity";

    private HttpUtil httpUtil;

    private TextView btn_free_result;

    private Detail_Bean detail_bean;

    private TextView tv_amount;

    private String payMoneys;

    private ResultBeans resultBeans;

    private String payOrderID;



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }



    @Override
    public void setOnLeftClick() {
        Intent intent = new Intent(FailOutPaperActivity.this, MainActivity.class);
        startActivity(intent);
    }



    @Override
    public void setOnRightClick() {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.myue_layout_failoutpaper);
        //页面创建就退款
        payMoneys = SPUtils.getString("paymoneys");
        System.out.println("============="+payMoneys);
        payOrderID = SPUtils.getString("payOrderId");
        System.out.println("=============_+_+_oooo"+payOrderID);
        requestNet();
        initViews(savedInstanceState);
    }



    private void requestNet() {
     /*   {
            “ECID”:”100123”				// 商户编号
            ,“AppID”:26					//  app编号
            ,“AppType”:2					// 客户端类型：1、共享端；2、用户端
            ,“PhoneNumber”:”18210386513”		// 手机号码
            ,”PayMoney”:0.01,				// 充值金额
            ,”OrderID”:"...",				// 订单编号
        }
*/
        LoginResultBean query = UserDao.getDaoInstance(this).query();
        String phoneNumber = query.getPhoneNumber();
        System.out.println("=============+================="+phoneNumber);

        if (httpUtil == null)
            httpUtil = new HttpUtil();
        String[] params = new String[]{Constants.ECID, GlobalConfig.ECID, Constants.AppID, GlobalConfig.AppID, Constants.AppType, GlobalConfig.AppType, Constants.PhoneNumber, phoneNumber, Constants.payMoney, payMoneys,Constants.OrderID,payOrderID};
        httpUtil.callJson(handler, this, GlobalConfig.MSHARE_SERVER_ROOT + GlobalConfig.UZI_BACKMONER,
                ResultBeans.class, params);
    }



    @Override
    protected void getMessage(Message msg) {

        resultBeans = (ResultBeans) msg.obj;
        boolean result = resultBeans.getResult();
        System.out.println("result======="+result);


    }



    //初始化布局
    private void initViews(Bundle savedInstanceState) {
        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        if (topbar != null) {
            topbar.setOnTopBarClickListener(new MyTopbar.OnTopBarClickListener() {
                @Override
                public void setOnLeftClick() {

                    Intent intent = new Intent(FailOutPaperActivity.this, MainActivity.class);
                    startActivity(intent);
                }



                @Override
                public void setOnRightClick() {

                }
            });
        }

    }
}
