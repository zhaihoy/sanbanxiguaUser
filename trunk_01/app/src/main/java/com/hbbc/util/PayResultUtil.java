package com.hbbc.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

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

    private static final int FLAG_REAL_PAY_STATUS = 1001;

    public static void retrieveRealPayStatus(final Handler handler, final String[] params){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(GlobalConfig.MSHARE_SERVER_ROOT+GlobalConfig.MSHARE_QUERY_PAY_RESULT);
                    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setDoInput(true); // 设置输入流采用字节流
                    conn.setDoOutput(true);// 设置输出流采用字节流
                    conn.setRequestMethod("POST");
                    conn.setUseCaches(false);
                    conn.setRequestProperty("ser-Agent", "Fiddler");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.connect();// 连接既往服务端发送信息

                    String str = generateJsonString(params);
                    Log.d(TAG, "run: parmsStr="+str);
                    OutputStream os = conn.getOutputStream();
                    os.write(str.getBytes());
                    os.close();
                    Log.d(TAG, "run:>>>>>>>>>>>>>>>>>>>>>>>>>> "+conn.getResponseCode());


                    if(conn.getResponseCode()==200){
                        InputStream in = conn.getInputStream();
                        int length = conn.getContentLength();
                        byte[] b=new byte[length];
                        in.read(b);
                        String s = new String(b);
                        Log.d(TAG, "run: json>>>>>>"+s);
                        Log.d(TAG, "run: contentlength>>>"+length);
                        JSONObject obj=new JSONObject(s);
                        String payStatus = obj.getString("PayStatus");//支付状态：1、支付成功；2、支付取消；3、支付失败；4、准备支付；
                        Message msg = Message.obtain(handler, FLAG_REAL_PAY_STATUS);
                        Log.d(TAG, "run: msg.paystatus>>>>"+Integer.valueOf(payStatus));
                        msg.arg2 = Integer.valueOf(payStatus);
                        handler.sendMessage(msg);
                    }else{

                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();



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
                }else{
                    ClickKey.put(params[i], params[i + 1]);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return String.valueOf(ClickKey);
    }

}
