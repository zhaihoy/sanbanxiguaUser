package com.hbbc.util;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hbbc.R;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络请求工具类,eg:new HttpUtil().callJson();
 */
public class HttpUtil {

    private boolean callJson_isComplete;        // 下载是否完成；

    private int callJson_time = 2;              // 用于计时,默认2秒；

    private boolean callJson_isOpen = true;    // 用于控制是否开启dialog,默认开启；

    private int callJson_what = -1;             //用于控制handler返回消息的区分，默认-1；



    /**
     * Result字段返回值
     */
    private boolean isResultTrue(String text) {

        try {
            JSONObject jsonObject = new JSONObject(text);
            if (jsonObject.has("Result") && jsonObject.getBoolean("Result")) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            return false;
        }
    }



    /**
     * 服务器异常
     */
    private String getNotice(String text) {
        //改成网络异常
        String data = "网络异常,请稍后再试";
        try {
            JSONObject jsonObject = new JSONObject(text);
            if (jsonObject.has("Notice"))
                data = jsonObject.getString("Notice");
        } catch (JSONException e) {
            LogUtil.exception(e);
        }
        return data;
    }



    /**
     * 调用时要在callJson前面调用，否则无用.在callJson里面会把值置会默认值，如果有需要，下次还要调用
     */
    public void setDialogIsShow(boolean open, int times) {

        callJson_isOpen = open;
        callJson_time = times;
    }



    /**
     * 显示对话框,设置是否在callJson之前调用
     */
    public void setDialogIsShow(boolean open) {

        setDialogIsShow(open, callJson_time);
    }



    /**
     * 显示对话框,设置时长，默认为2秒
     */
    public void setDialogIsShow(int times) {

        setDialogIsShow(callJson_isOpen, times);
    }



    /**
     * @param handler 进行子线程与UI线程进行传递消息
     * @param context 上下文
     * @param url     网络地址
     * @param object  Class文件
     * @param strUrl  String类型的数组上传时所用 前一个为字段名，紧跟着为数据，依次类推
     */
    public void callJson(final Handler handler, final Context context,
                         final String url, final Class<?> object, final String... strUrl) {

        //判断是否有网络
        //不能在Applicaiton入口的地方弹这个dialog: 把这个检测功能像摩拜一样,单独提到Splash页面处
        if (!isNetworkAvailable(context)) {
            setWifiAlertDialog(context);
            return;
        }
        callJson_isComplete = false;

        // 用于计时作用
        new Thread(new Runnable() {

            @Override
            public void run() {

                int i = 0;
                while (i < callJson_time) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!callJson_isComplete && callJson_isOpen) {
                    handler.post(new Runnable() {
                        public void run() {

                        }
                    });
                }
                callJson_isOpen = true;
                callJson_time = 2;
            }
        }).start();
        // 用于下载数据
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    //把穿入的strUrl封装成JSON串
                    JSONObject ClickKey = new JSONObject();
                    for (int i = 0, N = strUrl.length; i < N; i += 2) {
                        if (i + 1 > N) {
                            ClickKey.put(strUrl[i], null);
                        } else {
                            ClickKey.put(strUrl[i], strUrl[i + 1]);
                        }
                    }

                    //向服务器端获取得到的数据
                    final String json = getJsonData(context, url, ClickKey);

                    Log.d("json:", json);
                    if (json.startsWith("Error")) {
                        showText(handler, context, json);

                        return;
                    }

                    callJson_isComplete = true;

                    Message msg = new Message();
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            //loadingDialog.dismiss();
                        }
                    });

                    msg.what = callJson_what;
                    if (object == null) {
                        msg.obj = json;

                    } else {
                        Object obj = new Gson().fromJson(json, object);
                        msg.obj = obj;
                    }
                    LogUtil.debug(msg.obj.toString());
                    Logger.i(msg.obj.toString());
                    //将result和callJson_what恢复成默认值
                    callJson_what = -1;
                    handler.sendMessage(msg);//通过handler发送消息，携带json数据（如果存在object则返回object类型，否则返回值为String类型）
                } catch (Exception e) {
                    LogUtil.exception(e);
                }
            }
        }).start();

    }



    /**
     * 返回数据时,显示Toast
     */
    private void showText(Handler handler, final Context context, final String text) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }



    /**
     * 从服务器端请求数据，无object形式
     * 请求成功返回String
     */
    public void callJson(Handler handler, Context context, String url,
                         String... strUrl) {

        callJson_what = -1;
        callJson(handler, context, url, null, strUrl);
    }



    /**
     * 多次请求用what分辨返回String
     */
    public void callJson(Handler handler, int mWhat, Context context,
                         String url, String... strUrl) {

        callJson_what = mWhat;
        callJson(handler, context, url, null, strUrl);
    }



    /**
     * 多次请求用what分辨 返回类对象（object）
     */
    public void callJson(Handler handler, int mWhat, Context context,
                         String url, Class<?> obj, String... strUrl) {
        callJson_what = mWhat;
        callJson(handler, context, url, obj, strUrl);
    }



    /**
     * 检查联网情况（手机是否正常联网）。 eg：isNetworkAvailable(context);
     */
    public boolean isNetworkAvailable(Context context) {

        ConnectivityManager mgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }



    /**
     * 从服务器请求JSON数据，放回String类型jsonback数据
     *
     * @param context
     * @param strUrl  网络访问地址
     * @param obj     向服务器端发送的请求数据
     * @return jsonback为服务器端返回的数据
     */
    public String getJsonData(Context context, String strUrl,
                              JSONObject obj) {

        String jsonback = "";
        if (!isNetworkAvailable(context)) {
            LogUtil.debug("out-------->positon==0");
            jsonback = "Error:网络异常,请稍后再试";
            ToastUtil.toast_debug("没有数据");
            ((BaseActivity) context).outputShort(jsonback);
        } else {
            try {
                URL url = new URL(strUrl);
                // 开始封装JSON数据
                String content = String.valueOf(obj);// 把JSON数据转换成String类型，使用输出流向服务器写
                // 传输数据
                HttpURLConnection urlConn = (HttpURLConnection) url
                        .openConnection();
                urlConn.setConnectTimeout(5000);
                urlConn.setDoInput(true); // 设置输入流采用字节流
                urlConn.setDoOutput(true);// 设置输出流采用字节流
                urlConn.setRequestMethod("POST");
                urlConn.setUseCaches(false);
                urlConn.setRequestProperty("ser-Agent", "Fiddler");
                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.connect();// 连接既往服务端发送信息

                OutputStream os = urlConn.getOutputStream();
                os.write(content.getBytes());
                os.close();
                int code = urlConn.getResponseCode();//返回码

                if (code == 200) {
                    InputStream is = urlConn.getInputStream();
                    int contentLength = urlConn.getContentLength();
                    LogUtil.debug("out------------->contentLength" + contentLength);
                    String TAG = "detail";
                    Log.e(TAG, "getJsonData: contentlength=" + contentLength);
                    // 以上是没有问题的!
                    String json = readString(is);
                    Log.e("detail", "getJsonData: json=" + json);

                    jsonback = json;
                    LogUtil.exception("json数据：" + json);

                    // 设置请求头
                } else {
                    //改成网络异常
                    jsonback = "Error:网络异常,请稍后再试";
                    ToastUtil.toast_debug("返回不是200");
                }

            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.exception(e);
                jsonback = "Error:网络异常,请稍后再试";
                ToastUtil.toast_debug("出现异常");
            }
        }
        return jsonback;

    }



    /**
     * 输入流的字节转化byte数组
     */
    public byte[] readBytes(InputStream is) {

        try {
            byte[] buffer = new byte[1024];//原来写的是1024
            int len;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 将字节转换为字符
     */
    public String readString(InputStream is) {

        byte[] bytes = readBytes(is);
        Log.d("detail", "readString: bytes=====" + bytes);
        String s = new String(bytes);
        return s;
    }



    /**
     * 无网络时跳转到设置网络页面
     */
    public void setWifiAlertDialog(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("" + context.getResources().getString(R.string.app_name_sharer_gongyu));
        builder.setMessage("网络不可用，请设置网络！");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = null;
                /**
                 * 判断手机系统的版本！如果API大于10 就是3.0+
                 * 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
                 */
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName(
                            "com.android.settings",
                            "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create();
        builder.show();
    }
}
