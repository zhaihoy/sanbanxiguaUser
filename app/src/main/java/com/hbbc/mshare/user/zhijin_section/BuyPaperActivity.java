package com.hbbc.mshare.user.zhijin_section;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.user.detail.Detail_Bean;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.SPUtils;

/**
 * Created by Administrator on 2017/12/6.
 */
public class BuyPaperActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, MyTopbar.OnTopBarClickListener {


    private static final String TAG = "BuyPaperActivity";

    private static final int REQUEST_BEAN = -1;

    private static final int BUYPAPER = 1;

    public SubmitRequestListener submitRequestListener;

    //    private com.hbbc.mshare.endStroke_bean endStroke_bean;
    private HttpUtil httpUtil;

    private TextView tvs_amount;

    private String orderMoney;

    private TextView btn_free;

    private TextView btn_forFree;

    private Detail_Bean detail_bean;

    private ForFree_Bean forFreeBean;



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }



    @Override
    public void setOnLeftClick() {

    }



    @Override
    public void setOnRightClick() {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.myue_layout_buypaper);
        detail_bean = (Detail_Bean) getIntent().getSerializableExtra("detail_bean");
        orderMoney = SPUtils.getString("orderMoney");
        initViews(savedInstanceState);
    }



    //初始化布局
    private void initViews(Bundle savedInstanceState) {

        btn_free = (TextView) findViewById(R.id.btn_Free);
        btn_forFree = (TextView) findViewById(R.id.btn_pay_submit);

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        if (topbar != null) {
            topbar.setOnTopBarClickListener(new MyTopbar.OnTopBarClickListener() {
                @Override
                public void setOnLeftClick() {

                    Intent intent = new Intent(BuyPaperActivity.this, com.hbbc.mshare.user.main.MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                @Override
                public void setOnRightClick() {
                }
            });
        }
        //点击购买纸
        btn_forFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BuyPaperActivity.this, ReBuyPaperActivity.class);
                intent.putExtra("detail_bean", detail_bean);
                System.out.println(detail_bean.getGoodsUsePrice());
                 startActivity(intent);
            }
        });
        btn_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击按钮跳转到免费领纸
                requestNet();
                setOnSubmitRequestListener(new SubmitRequestListener() {
                    @Override
                    public void onSubmitRequestSuccess() {
                        Intent intent = new Intent(BuyPaperActivity.this, OutPaperResult.class);
                        intent.putExtra("detail_bean", detail_bean);
                        System.out.println(detail_bean.getGoodsUsePrice());
                        intent.putExtra("forFreeBean", forFreeBean);
                        startActivity(intent);

                    }
                });
            }
        });
    }



    private void requestNets() {
        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }

        LoginResultBean query = UserDao.getDaoInstance(this).query();
        String phoneNumber = query.getPhoneNumber();
       /* ECID:100121,				// 商户编号
                AppID:24,			//  app编号
                AppType:2,				// 客户端类型：1、共享端；2、用户端
                PhoneNumber:18210386513,// 手机号码
                GoodsSNID:1000205,		//物品编号
*/
        String[] params = new String[]{Constants.ECID, GlobalConfig.ECID, Constants.AppID, GlobalConfig.AppID, Constants.AppType, GlobalConfig.AppType, Constants.PhoneNumber, phoneNumber,
                Constants.GoodsSNID, detail_bean.getGoodsSNID()};
        httpUtil.callJson(handler, BUYPAPER, this, GlobalConfig.SERVERROOTPATH + GlobalConfig.BUY_PAPER, ForFree_Bean.class, params);

    }



    //点击按钮访问网络，免费领取纸巾
    private void requestNet() {
        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }

        LoginResultBean query = UserDao.getDaoInstance(this).query();
        String phoneNumber = query.getPhoneNumber();
       /* ECID:100121,				// 商户编号
                AppID:24,			//  app编号
                AppType:2,				// 客户端类型：1、共享端；2、用户端
                PhoneNumber:18210386513,// 手机号码
                GoodsSNID:1000205,		//物品编号
   */
        String[] params = new String[]{Constants.ECID, GlobalConfig.ECID, Constants.AppID, GlobalConfig.AppID, Constants.AppType, GlobalConfig.AppType, Constants.PhoneNumber, phoneNumber,
                Constants.GoodsSNID, detail_bean.getGoodsSNID()};
        httpUtil.callJson(handler, REQUEST_BEAN, this, GlobalConfig.SERVERROOTPATH + GlobalConfig.BUY_PAPER_FORFREE, ForFree_Bean.class, params);
    }



    public void setOnSubmitRequestListener(SubmitRequestListener submitRequestListener) {

        this.submitRequestListener = submitRequestListener;
    }



    @Override
    protected void getMessage(Message msg) {

        switch (msg.what) {
            case REQUEST_BEAN:
                forFreeBean = (ForFree_Bean) msg.obj;
                submitRequestListener.onSubmitRequestSuccess();
                super.getMessage(msg);
        }

    }



    public interface SubmitRequestListener {

        void onSubmitRequestSuccess();
    }
}
