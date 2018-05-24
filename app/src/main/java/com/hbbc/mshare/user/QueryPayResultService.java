package com.hbbc.mshare.user;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.user.main.MainActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.PayResultUtil;
import com.hbbc.util.ToastUtil;

/**
 * Created by Administrator on 2017/9/22.
 * 这个服务的作用就是去后台查询最终的支付结果
 */
public class QueryPayResultService extends IntentService {

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what)
            {
                case PayResultUtil.FLAG_REAL_PAY_STATUS:
                    handleRealPayResult(msg);
                    break;
            }
            //如果支付成功了,要分支判断: 1.修改数据库,展示结果 2. 展示结果;

            ToastUtil.toast_debug("toast from service!");

            return true;
        }
    });
    private Intent intent;

    private UserDao userDao;



    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *  Used to name the worker thread, important only for debugging.
     */
    public QueryPayResultService() {

        super("QueryPayResultService");
    }



    @Override
    public void onCreate() {

        super.onCreate();
        ToastUtil.toast_debug("开启了服务");
    }



    @Override
    public void onDestroy() {

//        super.onDestroy();

        ToastUtil.toast_debug("销毁了服务");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //这个方法在工作线程中运行,所以不要在此再开子线程了
        //后到的任务在队列里面按顺序依次执行

        this.intent = intent;

        String phoneNumber = intent.getStringExtra(Constants.PhoneNumber);

        String payOrderId = intent.getStringExtra(Constants.PayOrderID);
        String[] params = {Constants.AppID,GlobalConfig.AppID,Constants.ECID, GlobalConfig.ECID, Constants.PhoneNumber, phoneNumber, Constants.PayOrderID, payOrderId};

        PayResultUtil.retrieveRealPayStatus(handler,params);

    }
    /**
     * *****处理从服务器上获取到的真实支付结果*****
     */
    private void handleRealPayResult(Message msg) {

        if(userDao == null){
            userDao = UserDao.getDaoInstance(this);
        }

        String payType = intent.getStringExtra(Constants.PayType);//区分用户支付方式:ZFB or WECHAT
        String type = intent.getStringExtra(Constants.Type);
        int payStatus = msg.arg2;
        //支付状态：1、支付成功；2、支付取消；3、支付失败；4、准备支付；
        String result = "" ;

        System.out.println("1====================payStatus="+payStatus);

        if("DEPOSIT".equals(type)){//押金的支付结果
            System.out.println("2=======================payStatus"+payStatus);
            if(payStatus==1){
                result="支付成功";
                LoginResultBean user = userDao.queryCurrentProcessingUser();
                //Caution:需求改变后：将当前正在支付押金的用户设为当前用户
                user.setCurrentUser(true);//这个为什么没有执行呢??
                user.setDespositStatus(2);//押金已付,更新押金状态
                userDao.update(user);
                openActivity(MainActivity.class);

            }else{
                System.out.println("2=======================失败");
                result="支付失败";
                ToastUtil.toast("请重新支付");
            }

        }else if("ORDERPAYMENT".equals(type)){//预定的支付结果
            System.out.println("3=======================payStatus"+payStatus);
            if(payStatus==1){
                result="订单支付成功";
                openActivity(MainActivity.class);
            }else{
                System.out.println("3=======================失败");
                result="订单支付失败";
            }

        }else if("WITHDRAW".equals(type)){//提现的支付结果

        }

        ToastUtil.toast(result);
    }



    private void openActivity(Class cls) {

        // TODO: 2017/9/26 这个打开方式不正确!要改
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//
        startActivity(intent);
    }
}
