package com.hbbc.mshare.sharer;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.IOUtil;
import com.hbbc.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2017/9/27.
 *
 */
public class UploadImgService extends IntentService {

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    ToastUtil.toast("上传失败");
                    break;
                case 1:
                    ToastUtil.toast("上传成功");
                    break;
            }


            return false;
        }
    });

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * Used to name the worker thread, important only for debugging.
     */
    public UploadImgService() {

        super("UploadImgService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        //上传图片

        String goodsSNID = intent.getStringExtra(Constants.GoodsSNID);
        Log.d("upload", "onHandleIntent: snid-->"+goodsSNID);

        String goodsName = intent.getStringExtra(Constants.GoodsName);
        String intro = intent.getStringExtra(Constants.INTRODUCTION);
        String businessType = intent.getStringExtra(Constants.BUSSINESS_TYPE);
        String price = intent.getStringExtra(Constants.USAGE_PRICE);
        String goods_deposit = intent.getStringExtra(Constants.GOODS_DEPOSIT);
        String typeId = intent.getStringExtra(Constants.GOODSTYPEID);
        Log.d("upload", "onHandleIntent: deposit-->"+goods_deposit);

        LatLng selectedPosition = intent.getParcelableExtra("LatLng");
        String labelContent = intent.getStringExtra(Constants.LABEL_CONTENT);
        ArrayList<String> imgPaths = intent.getStringArrayListExtra(Constants.PicList);

        LoginResultBean user = UserDao.getDaoInstance(this).query();
        if (user == null) return;
        String phoneNumber = user.getPhoneNumber();
        String picList = assembleJsonArrayOfPics(imgPaths);

        String[] params = new String[]{
                Constants.AppID,GlobalConfig.AppID,
                Constants.GoodsName,goodsName,
                Constants.ECID, GlobalConfig.ECID,
                Constants.PhoneNumber, phoneNumber,
                Constants.GoodsSNID, goodsSNID,
                Constants.PicList, picList,
                Constants.INTRODUCTION, intro,
                Constants.BUSSINESS_TYPE, businessType,
                Constants.USAGE_PRICE, price,
                Constants.GOODS_DEPOSIT, goods_deposit,
                Constants.GOODSTYPEID, typeId,
                Constants.LABEL_CONTENT, labelContent,
                Constants.Lat,String.valueOf(selectedPosition.latitude),
                Constants.Lng,String.valueOf(selectedPosition.longitude)};

        callJson(goodsSNID, params);
    }



    /**
     * 开启网络访问
     */
    private void callJson(String goodsSNID, String[] params) {

        try {
            String request = assembleJsonString(params);
            Log.d("Upload", "callJson: request-->"+request);
            URL url = new URL(GlobalConfig.SHARER_SERVER_ROOT +
                    (goodsSNID.equals("") ? GlobalConfig.SUBMIT_NEW_ITEM : GlobalConfig.SHARER_MODIFY));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoInput(true); // 设置输入流采用字节流
            conn.setDoOutput(true);// 设置输出流采用字节流
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setRequestProperty("ser-Agent", "Fiddler");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();// 连接既往服务端发送信息
            if(request==null)
                return;
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(request.getBytes());
            outputStream.close();

            if(conn.getResponseCode()==200){
                InputStream in = conn.getInputStream();
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                byte[] buffer = new byte[100];
                int len = 0;
                while ((len = in.read(buffer))!=-1){
                    baos.write(buffer,0,len);
                }
                String response = new String(baos.toByteArray());
                baos.close();
                in.close();
                JSONObject jsonObject = new JSONObject(response);
                boolean result = jsonObject.getBoolean("Result");
                handler.sendEmptyMessage(result?1:0);
            }else{
                handler.sendEmptyMessage(0);
            }


        } catch (IOException | JSONException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(0);
        }
    }



    private String assembleJsonString(String[] params){

        try {
            //把穿入的params封装成JSON串
            JSONObject ClickKey = new JSONObject();
            for (int i = 0, N = params.length; i < N; i += 2) {
                if (i + 1 > N) {
                    ClickKey.put(params[i], null);
                } else {
                    ClickKey.put(params[i], params[i + 1]);
                }
            }
            return ClickKey.toString();
        } catch (JSONException e) {
            //null
            Log.e("UploadImgService", "assembleJsonString: ");
        }

        return null;
    }



    /**
     * base64对图片转码,得到json格式数组
     */
    private String assembleJsonArrayOfPics(ArrayList<String> imgPaths) {

        JSONArray jsonArray = new JSONArray();
        ByteArrayOutputStream baos;
        for (int i = 0; i < imgPaths.size(); i++) {//

            try {
                baos = new ByteArrayOutputStream();
                //todo:这个方法真的可以获取,要看看它的原理,以及控制返回的Bitmap的大小
                Bitmap bm = Glide.with(GlobalConfig.APP_Context).load(imgPaths.get(i)).asBitmap().into(1000, 600).get();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                String encodeToString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

                jsonArray.put(encodeToString);
                IOUtil.closeStream(baos);

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }
        return jsonArray.toString();
    }
}
