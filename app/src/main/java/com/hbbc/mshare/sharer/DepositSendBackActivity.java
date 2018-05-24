package com.hbbc.mshare.sharer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.util.MyLinearLayout;
import com.hbbc.util.MyTopbar;

/**
 * Created by Administrator on 2017/10/11.
 *
 */
public class DepositSendBackActivity extends AppCompatActivity implements MyTopbar.OnTopBarClickListener, View.OnClickListener {

    private String[] accountType=new String[]{"支付宝","微信"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_withdraw);
        initView();
    }



    private void initView() {
        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setTitle("退还押金");
        topbar.setOnTopBarClickListener(this);

        TextView tv_tip01 = (TextView) findViewById(R.id.tv_tip01);
        TextView tv_tip02 = (TextView) findViewById(R.id.tv_tip02);
        tv_tip01.setText("(当前押金)");
        tv_tip02.setText("退款申请");

        MyLinearLayout ll_withdraw = (MyLinearLayout) findViewById(R.id.ll_withdraw);
        MyLinearLayout ll_withdraw_type = (MyLinearLayout) findViewById(R.id.ll_accountType);
        MyLinearLayout ll_withdraw_account = (MyLinearLayout) findViewById(R.id.ll_accountNumber);
        ll_withdraw.setOnClickListener(this);
        ll_withdraw_type.setOnClickListener(this);
        ll_withdraw_account.setOnClickListener(this);

    }



    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(R.anim.global_in,R.anim.global_out);
    }



    @Override
    public void setOnLeftClick() {
        finish();
    }



    @Override
    public void setOnRightClick() {

    }



    @Override
    public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setCancelable(true);
        switch (v.getId()){
            case R.id.ll_withdraw:
                customizeBuilder(builder,0);
                break;
            case R.id.ll_accountType:
                builder.setSingleChoiceItems(accountType, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((TextView)findViewById(R.id.tv_type)).setText(accountType[which]);
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.ll_accountNumber:
                customizeBuilder(builder,1);
                break;
            case R.id.btn_submit:
                submitRequest();
                break;

        }
        builder.show();
    }


    //提交申请
    private void submitRequest() {


    }



    private void customizeBuilder(AlertDialog.Builder builder, final int requestType) {

        View inflate = LayoutInflater.from(this).inflate(R.layout.sharer_input_dialog_layout, null);
        builder.setView(inflate);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog aDialog= (AlertDialog) dialog;
                EditText et_input = (EditText) aDialog.findViewById(R.id.et_input);
                String input = et_input.getText().toString().trim();
                if(!TextUtils.isEmpty(input)){
                    ((TextView)findViewById(requestType==0?R.id.tv_withdraw:R.id.tv_account)).setText(input);
                }

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
}
