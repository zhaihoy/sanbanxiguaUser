package com.hbbc.mshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.hbbc.R;
import com.hbbc.util.MyTopbar;

/**
 * Created by Administrator on 2017/10/16.
 *
 */
public class ProtocolActivity extends AppCompatActivity implements MyTopbar.OnTopBarClickListener {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_protocol);
        initView();

    }



    private void initView() {

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);//可交互

        Intent intent = getIntent();
        int protocol_type = intent.getIntExtra("protocol_type", -1);
        String local_url = "";
        String title = "";
        switch (protocol_type){
            case 0:
                local_url = "file:///android_asset/user_protocol.html";
                title = "用户协议";
                break;
            case 1:
                local_url = "file:///android_asset/charge_protocol.html";
                title = "充值协议";
                break;
            case 2:
                local_url = "file:///android_asset/deposit_protocol.html";
                title = "押金协议";
                break;
            case -1:
                title = "协议";
                topbar.setTitle(title);
                return;
        }
        webView.loadUrl(local_url);
        topbar.setTitle(title);

    }



    @Override
    public void setOnLeftClick() {
        finish();
    }




    @Override
    public void setOnRightClick() {

    }



    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(R.anim.global_in,R.anim.global_out);
    }
}
