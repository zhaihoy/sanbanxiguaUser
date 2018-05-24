package com.hbbc.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hbbc.mshare.SignedOrderBean;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/9/25.
 *
 */
public class PayResultUtil {

    private static final String TAG = "PayResultUtil";

    public static final int FLAG_REAL_PAY_STATUS = 1001;



    public static void retrieveRealPayStatus(final Handler handler, final String[] params) {

        try {
            URL url = new URL(GlobalConfig.MSHARE_SERVER_ROOT + GlobalConfig.MSHARE_QUERY_PAY_RESULT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoInput(true); // 设置输入流采用字节流
            conn.setDoOutput(true);// 设置输出流采用字节流
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setRequestProperty("ser-Agent", "Fiddler");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();// 连接既往服务端发送信息

            String str = generateJsonString(params);
            Log.d(TAG, "run: parmsStr=" + str);
            OutputStream os = conn.getOutputStream();
            os.write(str.getBytes());
            os.close();
            Log.d(TAG, "run:>>>>>>>>>>>>>>>>>>>>>>>>>> " + conn.getResponseCode());


            if (conn.getResponseCode() == 200) {
                Log.e(TAG, "retrieveRealPayStatus: 服务器正确返回数据");

                InputStream in = conn.getInputStream();
                int length = conn.getContentLength();
                byte[] b = new byte[length];
                in.read(b);
                String s = new String(b);
                Log.d(TAG, "run: json>>>>>>" + s);
                Log.d(TAG, "run: contentlength>>>" + length);
                JSONObject obj = new JSONObject(s);
                String payStatus = obj.getString("PayStatus");//支付状态：1、支付成功；2、支付取消；3、支付失败；4、准备支付；
                Message msg = Message.obtain(handler, FLAG_REAL_PAY_STATUS);
                Log.d(TAG, "run: msg.paystatus>>>>" + Integer.valueOf(payStatus));
                msg.arg2 = Integer.valueOf(payStatus);
                handler.sendMessage(msg);

            } else {
                Log.e(TAG, "retrieveRealPayStatus: 服务器没有正确返回数据");

            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "retrieveRealPayStatus: exception!");
        }


    }



    /**
     * 产生json格式的请求字符串
     */
    private static String generateJsonString(String[] params) {

        //把穿入的strUrl封装成JSON串
        JSONObject ClickKey = new JSONObject();
        try {
            for (int i = 0, N = params.length; i < N; i += 2) {
                if (i + 1 > N) {
                    ClickKey.put(params[i], null);
                } else {
                    ClickKey.put(params[i], params[i + 1]);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return String.valueOf(ClickKey);
    }


    /**
     * 向服务器拉取真实的支付结果,并可能需要更新数据库信息
     * 指定出要查询哪个服务
     * type      用来标明是进行订金/订单/提现中的哪种
     */
    public static void openQueryService(Context context, SignedOrderBean orderBean, String type, Class service) {

        //?是否是出问题在这了? 就在这里: 用户端支付时,应该查询登陆用户的信息,而不是当前正在注册用户的信息!
        LoginResultBean user = null;

        if(type.equals("ORDERPAYMENT")||type.equals("WITHDRAW")){
            user = UserDao.getDaoInstance(context).query();
        }else if(type.equals("DEPOSIT")){
            user = Integer.valueOf(GlobalConfig.AppType) == 2 ? UserDao.getDaoInstance(context).queryCurrentProcessingUser():UserDao.getDaoInstance(context).query();
        }
        if (user == null) {
            Log.d(TAG, "openQueryService: 未进行真实结果查询!!!问题在此");
            return;
        }

        Log.d(TAG, "openQueryService: 进行真实结果查询!!!问题不在此");


        String phoneNumber = user.getPhoneNumber();

        Intent intent = new Intent(context, service);
        intent.putExtra(Constants.PhoneNumber, phoneNumber);
        intent.putExtra(Constants.PayOrderID, orderBean.getPayOrderID());
        intent.putExtra(Constants.PayType, orderBean.getPayType());//指明用支付还是微信
        intent.putExtra(Constants.Type, type);//指明是押金支付还是业务支付还是提现支付

        context.startService(intent);//开启服务,在服务中来获取真实的后台支付结果
    }

    public static void notifyPaymentCancelation(Handler handler,Context ctx,String orderId){

//        OkHttpUtils
//                .post()
//                .addParams(Constants.AppID,GlobalConfig.AppID)
//                .url(GlobalConfig.MSHARE_SERVER_ROOT+GlobalConfig.SHARER_CANCEL_ORDER_PAYMENT)
//                .addParams(Constants.ECID,GlobalConfig.ECID)
//                .addParams(Constants.PayOrderID,orderId)
//                .addParams("Status","2")//2、支付取消
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int i) {
//
//                    }
//                    @Override
//                    public void onResponse(String s, int i) {
//
//                    }
//                });//不处理同步结果

        String[] params = {Constants.AppID,GlobalConfig.AppID,Constants.ECID,GlobalConfig.ECID,Constants.PayOrderID,orderId,"Status","2"};
        String url = GlobalConfig.MSHARE_SERVER_ROOT + GlobalConfig.SHARER_CANCEL_ORDER_PAYMENT;
        new HttpUtil().callJson(handler,ctx,url,params);


    }

}
