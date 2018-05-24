package com.hbbc.mmain;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hbbc.R;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户建议页面
 */

public class SuggestActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener, View.OnTouchListener {

    private MyTopbar suggest_layout;                //topbar主题色

    private EditText suggest_et;                    //用户建议输入框

    private Button submit_suggest;                  //提交建议

    private int themeColor = Color.WHITE;           //主题色

    private boolean ischeck;                        //是否点击



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mmain_inform_suggest);

        initView();
    }



    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        //用户建议标题
        suggest_et = (EditText) findViewById(R.id.suggest_et);
        submit_suggest = (Button) findViewById(R.id.submit_suggest);
        suggest_layout = (MyTopbar) findViewById(R.id.suggest_layout);

        initTheme();

        //设置监听事件
        submit_suggest.setOnTouchListener(this);
        suggest_layout.setOnTopBarClickListener(this);
    }



    /**
     * 初始化主题色
     */
    private void initTheme() {

        //主题色赋值
        themeColor= GlobalParameter.getThemeColor();

        //提交建议Button主题色
        GradientDrawable p = (GradientDrawable) submit_suggest.getBackground();
        p.setColor(themeColor);
        p.setCornerRadius(8.0f);
        p.setStroke(2, themeColor);

        //用户建议
        suggest_layout.setTitle(getResources().getString(R.string.unlisted_suggest));

        //topbar主题色
        suggest_layout.setBackgroundColor(themeColor);

    }



    /**
     * 消息回调机制
     */
    @Override
    protected void getMessage(Message msg) {
        String data = msg.obj.toString();
        try {
            JSONObject jsonObject = new JSONObject(data);
            String notice = jsonObject.getString("Notice");
            if (jsonObject.getBoolean("Result")){
                outputShort("您的建议提交成功！");
            }else{
                //提交失败
                outputShort(notice);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    /**
     * 模拟点击事件
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //设置点击透明度
                ischeck = true;
                GradientDrawable p = (GradientDrawable) submit_suggest.getBackground();
                p.setColor(themeColor);
                p.setStroke(2, themeColor);
                p.setCornerRadius(8.0f);
                p.setAlpha(200);
                break;

            case MotionEvent.ACTION_MOVE:
                if (!(event.getX() > 0 && event.getX() < v.getMeasuredWidth() && event.getY() > 0 && event.getY() < v.getMeasuredHeight())) {
                    ischeck = false;
                    GradientDrawable p1 = (GradientDrawable) submit_suggest.getBackground();
                    p1.setColor(themeColor);
                    p1.setCornerRadius(8.0f);
                    p1.setStroke(2, themeColor);
                    p1.setAlpha(255);
                }


                break;

            case MotionEvent.ACTION_UP:
                if (ischeck) {
                    //点击后跳转
                    if (suggest_et.getText().toString().equals("")) {
                        outputShort("请先输入建议内容！");
                    } else {
                        //提交建议
                        new HttpUtil().callJson(handler, this, GlobalConfig.MAPPMAIN_SUBMITUSERADVICE, "AppUserID", GlobalConfig.AppUserID, "AdviceContent", suggest_et.getText().toString());
                        finish();
                    }
                }
                GradientDrawable p1 = (GradientDrawable) submit_suggest.getBackground();
                p1.setColor(themeColor);
                p1.setStroke(2, themeColor);
                p1.setCornerRadius(8.0f);
                p1.setAlpha(255);
                break;

        }

        return false;
    }



    @Override
    public void setOnLeftClick() {
        finish();
    }



    @Override
    public void setOnRightClick() {

    }
}
