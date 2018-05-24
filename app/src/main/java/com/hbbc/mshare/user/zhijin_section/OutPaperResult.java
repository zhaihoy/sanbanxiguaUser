package com.hbbc.mshare.user.zhijin_section;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;

import com.hbbc.R;
import com.hbbc.mshare.user.detail.Detail_Bean;
import com.hbbc.util.BaseActivity;

/**
 * Created by Administrator on 2017/12/29.
 */
public class OutPaperResult extends BaseActivity {

    private Detail_Bean detail_bean;

    private ForFree_Bean forFreeBean;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.gongyu_unlock);
        handler.sendEmptyMessageDelayed(0, 3000);
        forFreeBean = (ForFree_Bean) getIntent().getSerializableExtra("forFreeBean");
        System.out.println("===========+" + forFreeBean.getResult());
    }



    @Override
    protected void getMessage(Message msg) {

        if (forFreeBean.getResult() && forFreeBean.getOrderID().isEmpty()) {
            Intent intent = new Intent();
            intent.setClass(this, ForFreeOutPaperResult.class);
            startActivity(intent);
            finish();
        } else if (!forFreeBean.getResult() && !forFreeBean.getOrderID().isEmpty()) {
            new AlertDialog.Builder(OutPaperResult.this).setTitle("系统提示")//设置对话框标题

                    .setMessage("您今天已经免费领过纸！")//设置显示的内容

                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮

                        @Override

                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                            // TODO Auto-generated method stub

                            finish();

                        }

                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮


                @Override

                public void onClick(DialogInterface dialog, int which) {//响应事件

                    // TODO Auto-generated method stub

                    finish();

                }

            }).show();//在按键响应事件中显示此对话框
        }else if (!forFreeBean.getResult()&&forFreeBean.getOrderID().isEmpty()){
            Intent intent = new Intent();
            intent.setClass(this, FailOutActivity.class);
            startActivity(intent);
            finish();
        }
    }


}



