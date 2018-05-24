package com.hbbc.mshare;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/9/22.
 */
public class QueryPayResultService extends IntentService {


    private HttpUtil httpUtil;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            return false;
        }
    });



    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public QueryPayResultService(String name) {

        super(name);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        //这个方法在工作线程中调用,所以不要在此再开子线程了

        if(intent.getAction()=="")
        {
            String orderId = intent.getStringExtra("");//
            int type = intent.getIntExtra("", -1);//区分是用户端,还是车主端
            String address = null;
            if(type==1){
                address = GlobalConfig.MSHARE_SERVER_ROOT;
            }else if(type==2){
                address = GlobalConfig.SHARER_SERVER_ROOT;
            }

            try {
                URL url = new URL(address+GlobalConfig.MSHARE_QUERY_PAY_RESULT);
                HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("ser-Agent", "Fiddler");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setConnectTimeout(5000);
                conn.setDoInput(true); // 设置输入流采用字节流
                conn.setDoOutput(true);// 设置输出流采用字节流
                conn.setUseCaches(false);
                conn.connect();




            } catch (IOException e) {
                e.printStackTrace();
            }

        }










    }
}
