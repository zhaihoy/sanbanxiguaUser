package com.hbbc.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2017/9/2.
 *
 */
public class BitmapEncodeTask extends AsyncTask<List<String>,Void,String> {

    public BitmapEncodeTask(OnPostExecuteListener listener){
        this.listener=listener;
    }

    @Override
    protected String doInBackground(List<String>... params) {
        JSONArray jsonArray = new JSONArray();
        ByteArrayOutputStream baos;
        for (int i = 0; i < params[0].size(); i++) {//

            try {
                baos = new ByteArrayOutputStream();
                //todo:这个方法真的可以获取,要看看它的原理,以及控制返回的Bitmap的大小
                Bitmap bm = Glide.with(GlobalConfig.APP_Context).load(params[0].get(i)).asBitmap().into(1000, 600).get();
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


    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);
        if(listener==null){
            ToastUtil.toast("listener is null");
        }else{
            listener.onPostExecute(s);
        }

    }

    private OnPostExecuteListener listener;

    public interface OnPostExecuteListener{
        void onPostExecute(String encodedString);
    }


}
