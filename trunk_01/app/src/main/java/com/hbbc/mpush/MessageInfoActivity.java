package com.hbbc.mpush;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 读取消息详情页面
 */
public class MessageInfoActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {
    private String mtmid;

    private MyTopbar mpush_msginfo_topbar;  //标题栏Topbar

    private TextView mpush_msginfo_title, mpush_msginfo_time, mpush_msginfo_content;//详情标题，时间,内容

    private ImageView mpush_msginfo_img;                    //图片

    private int themeColor = Color.WHITE;                   //主题色



    @Override
    protected void getMessage(Message msg) {

        switch (msg.what){
            case 0:
                try {
                    String msgdata= (String) msg.obj;
                    JSONObject jsono=new JSONObject(msgdata);
                    String notice=jsono.getString("Notice");
                    if (jsono.getBoolean("Result")){
                        Logger.d("请求成功");
                    }else{
                        outputShort(notice);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case 1:
                try {
                    String msgdata= (String) msg.obj;
                    Logger.d("msg:"+msgdata);
                    JSONObject jsono=new JSONObject(msgdata);
                    String notice=jsono.getString("Notice");
                    if (jsono.getBoolean("Result")){
                        //标题
                        if (jsono.has("MsgTitle")){
                            mpush_msginfo_title.setText(jsono.getString("MsgTitle"));
                        }
                        //图片
                        if (jsono.has("MsgPicFileID")){
                            Glide.with(this).load(jsono.getString("MsgPicFileID")).into(mpush_msginfo_img);
                        }
                        //内容
                        if (jsono.has("MsgContent")){
                            mpush_msginfo_content.setText("      "+jsono.getString("MsgContent"));
                        }
                        //时间
                        if (jsono.has("SendTime")){
                            String time=jsono.getString("SendTime").substring(0,16);
                            mpush_msginfo_time.setText("      "+time);
                        }

                    }else{
                        outputShort(notice);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mpush_msginfo_main);
        mtmid = getIntent().getStringExtra("MTMID");

        initView();
        //提交反馈消息
        new HttpUtil().callJson(handler,0,this,GlobalConfig.MPUSH_SENDMESSAGESTATUS,"AppUserID",GlobalConfig.AppUserID,"ECID",GlobalConfig.ECID+"","MTMID",mtmid);
    }



    @Override
    protected void initView() {
        mpush_msginfo_topbar = (MyTopbar) findViewById(R.id.mpush_msginfo_topbar);
        mpush_msginfo_title = (TextView) findViewById(R.id.mpush_msginfo_title);
        mpush_msginfo_time = (TextView) findViewById(R.id.mpush_msginfo_time);
        mpush_msginfo_content = (TextView) findViewById(R.id.mpush_msginfo_content);
        mpush_msginfo_img = (ImageView) findViewById(R.id.mpush_msginfo_img);

        initTheme();

        //详情接口
        new HttpUtil().callJson(handler,1,this,GlobalConfig.MPUSH_GETMESSAGEINFO,"AppUserID",GlobalConfig.AppUserID,"ECID",GlobalConfig.ECID+"","MTMID",mtmid);
        mpush_msginfo_topbar.setOnTopBarClickListener(this);
    }



    /**
     * 主题色
     */
    private void initTheme() {
        //主题色赋值
        themeColor= GlobalParameter.getThemeColor();

        mpush_msginfo_topbar.setBackgroundColor(themeColor);

        mpush_msginfo_topbar.setTitle("消息详情");
    }



    @Override
    public void setOnLeftClick() {
        finish();
    }



    @Override
    public void setOnRightClick() {

    }
}
