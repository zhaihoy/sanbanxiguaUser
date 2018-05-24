package com.hbbc.util;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbbc.R;

import java.lang.ref.WeakReference;

/**
 * 自定义Activity基类，eg:public class MainActivity extends BaseActivity{}
 */
public class BaseActivity extends AppCompatActivity implements OnClickListener {

    /**
     * handler用来与子线程发送消息，不容易被回收（内存泄漏）
     */
    @SuppressLint("HandlerLeak")
    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            getMessage(msg);
        }
    };
    private TextView shell_titlebar_tv_Title; //标题
    private ImageView global_back;            //退出

    /**
     * 设置去除标题栏
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtil.setFullScreenShow(this, false);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        overridePendingTransition(R.anim.global_in, R.anim.global_out);
    }


    /**
     * 子类重写此方法接消息
     */
    protected void getMessage(android.os.Message msg) {

    }

    /**
     * 初始化控件
     */
    protected void initView() {

        shell_titlebar_tv_Title = (TextView) findViewById(R.id.global_title);
        global_back = (ImageView) findViewById(R.id.global_back);
        global_back.setOnClickListener(this);
    }

    /**
     * 封装Toast_long
     */
    public void outputLong(String str) {

        Toast.makeText(BaseActivity.this, str, Toast.LENGTH_LONG).show();
    }

    /**
     * 封装Toast_short
     */
    public void outputShort(String str) {
        Toast.makeText(BaseActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置标题头部文字
     */
    public boolean tbSetBarTitleText(int resId) {
        String title = getString(resId);
        return tbSetBarTitleText(title);
    }

    /**
     * 设置标题头部文字
     */
    public boolean tbSetBarTitleText(String titleText) {

        TextView tv = (TextView) findViewById(R.id.global_title);
        if (null != tv) {
            tv.setText(titleText);
            return true;
        }
        return false;
    }

    /**
     * 设置退出图片的颜色
     */
    protected void setGlobal_backColor(int color) {
        global_back.setColorFilter(color);
    }

    /**
     * 点击的监听事件
     */
    @Override
    public void onClick(View v) {
        //退出
        if (v.getId() == R.id.global_back) {
            finish();
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        overridePendingTransition(0, R.anim.global_out);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    /**
     * 退出当前页面
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.global_out);

    }

//    /**
//     * handler用来与子线程发送消息,容易被GC
//     */
//    public HandlerHolder handler = new HandlerHolder(new OnReceiverMessageListener() {
//        @Override
//        public void handlerMessage(Message message) {
//
//            getMessage(message);
//        }
//    });

    public interface OnReceiverMessageListener {
        void handlerMessage(Message message);
    }

    /**
     * 控制Handler，容易被GC回收
     */
    public static class HandlerHolder extends Handler {
        WeakReference<OnReceiverMessageListener> messageListenerWeakReference;

        public HandlerHolder(OnReceiverMessageListener messageListener) {
            messageListenerWeakReference = new WeakReference<>(messageListener);
        }

        @Override
        public void handleMessage(Message msg) {

            if (messageListenerWeakReference != null && messageListenerWeakReference.get() != null) {
                messageListenerWeakReference.get().handlerMessage(msg);
            }
        }
    }
}
