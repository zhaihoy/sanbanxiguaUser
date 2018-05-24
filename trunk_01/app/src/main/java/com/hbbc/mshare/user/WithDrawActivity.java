package com.hbbc.mshare.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.mshare.login.BaseResultBean;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyLinearLayout;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.ToastUtil;

/**
 * Created by Administrator on 2017/8/31.
 *
 */
public class WithDrawActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    private String[] accountType=new String[]{"支付宝","微信"};
    private HttpUtil httpUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_withdraw);
        httpUtil=new HttpUtil();

        initView();


    }



    @Override
    protected void initView() {

        MyTopbar topbar= (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        MyLinearLayout ll_withdraw = (MyLinearLayout) findViewById(R.id.ll_withdraw);
        MyLinearLayout ll_withdraw_type = (MyLinearLayout) findViewById(R.id.ll_accountType);
        MyLinearLayout ll_withdraw_account = (MyLinearLayout) findViewById(R.id.ll_accountNumber);
        ll_withdraw.setOnClickListener(this);
        ll_withdraw_type.setOnClickListener(this);
        ll_withdraw_account.setOnClickListener(this);

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

        super.onClick(v);
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



    /**
     * 提交提款申请
     */
    private void submitRequest() {
        //// TODO: 2017/9/1
        if(httpUtil==null){
            httpUtil=new HttpUtil();
        }
        String phoneNumber="15093337480";
        String[] params = new String[]{Constants.ECID,GlobalConfig.ECID,Constants.PhoneNumber,phoneNumber,
                Constants.AppType,GlobalConfig.AppType,Constants.WithdrawAmount,"",Constants.WithdrawAccountID,"",
                Constants.WithdrawType,""};
        httpUtil.callJson(handler,this, GlobalConfig.SHARER_SERVER_ROOT+ GlobalConfig.SHARER_WITHDRAW, BaseResultBean.class,params);

    }



    @Override
    protected void getMessage(Message msg) {
        BaseResultBean result= (BaseResultBean) msg.obj;
        if(result!=null&&result.getResult()){
            ToastUtil.toast("提交成功,请耐心等等");
            finish();
        }
        else
        {

        }
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
