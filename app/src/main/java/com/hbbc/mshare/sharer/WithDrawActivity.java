package com.hbbc.mshare.sharer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.mshare.login.BaseResultBean;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.user.UserBean;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyLinearLayout;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.ToastUtil;

/**
 * Created by Administrator on 2017/8/31.
 */
public class
WithDrawActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    private static final int FLAG_AMOUNT = 0;

    private static final int FLAG_ACCOUNT = 1;

    private static final String TAG = "WithDrawActivity";

    private String[] accountType = new String[]{"支付宝", "微信"};

    private HttpUtil httpUtil;

    private long prevTimemillis = 0;

    private int withdraw_type;

    private UserBean userBean;

    private String phoneNumber;

    private LinearLayout tixianView;

    private String tixianMoney = "0";

    private TextView tvTixianMoney;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_withdraw);
        httpUtil = new HttpUtil();
        userBean = (UserBean) getIntent().getSerializableExtra("UserBean");

        initView();


    }



    @Override
    protected void initView() {

        LoginResultBean currentUser = UserDao.getDaoInstance(this).query();
        phoneNumber = currentUser.getPhoneNumber();
        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        tixianView = (LinearLayout) findViewById(R.id.tixian);
        tvTixianMoney = (TextView) tixianView.findViewById(R.id.tv_withdraw);

        MyLinearLayout ll_withdraw = (MyLinearLayout) findViewById(R.id.ll_withdraw);
        MyLinearLayout ll_withdraw_type = (MyLinearLayout) findViewById(R.id.ll_accountType);
        MyLinearLayout ll_withdraw_account = (MyLinearLayout) findViewById(R.id.ll_accountNumber);
        TextView tvs_amounts = (TextView) findViewById(R.id.tvs_amounts);

        tvs_amounts.setText(userBean.getUserBalance());
        TextView tv_account = (TextView) findViewById(R.id.tv_account);
        tv_account.setText(phoneNumber);


        ll_withdraw.setOnClickListener(this);
        ll_withdraw_type.setOnClickListener(this);
        ll_withdraw_account.setOnClickListener(this);

        Button btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        Intent intent = getIntent();
        withdraw_type = intent.getIntExtra("withdraw_type", 1);
        topbar.setTitle(withdraw_type == 1 ? "申请提现" : "退还押金");

        TextView tv_tip01 = (TextView) findViewById(R.id.tv_tip01);
        TextView tv_tip02 = (TextView) findViewById(R.id.tv_tip02);
        tv_tip01.setText((withdraw_type == 1) ? "(当前余额)" : "(当前押金)");
        tv_tip02.setText((withdraw_type == 1) ? "退款申请" : "押金退还");

        TextView tv_withdraw_type = (TextView) findViewById(R.id.tv_withdraw_type);
        tv_withdraw_type.setText((withdraw_type == 1) ? "提现金额" : "退还押金");


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
        switch (v.getId()) {
            case R.id.ll_withdraw:
                customizeBuilder(builder, FLAG_AMOUNT);
                break;
            case R.id.ll_accountType:
                builder.setSingleChoiceItems(accountType, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ((TextView) findViewById(R.id.tv_type)).setText(accountType[which]);
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.ll_accountNumber:
                customizeBuilder(builder, FLAG_ACCOUNT);
                break;
            case R.id.btn_submit:
                Log.d("TAG", "onClick: 提交了. ");
                tixianMoney = tvTixianMoney.getText().toString().trim();
                if(tixianMoney.isEmpty() || tixianMoney.equals("0")) {
                    ToastUtil.toast("请输入正确的金额");
                    break;
                }
                if (System.currentTimeMillis() - prevTimemillis > 2000) {
                   if (Float.parseFloat(tixianMoney) <= Float.parseFloat(userBean.getUserBalance())) {
                       Log.d(TAG, "onClick: +++++Y"+userBean.getUserBalance());
                       Log.d(TAG, "onClick: +++++U"+tixianMoney);
                       submitRequest();
                       prevTimemillis = System.currentTimeMillis();
                   }
                    else {
                       ToastUtil.toast("请核对金额");
                   }
                } else {

                    ToastUtil.toast("请勿重复提交");
                }
                return;

        }
        builder.show();
    }



    /**
     * 提交提款申请
     */
    private void submitRequest() {
        //// TODO: 2017/9/1
        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }
        //检查参数
        LoginResultBean currentUser = UserDao.getDaoInstance(this).query();

        String tv_account = ((TextView) findViewById(R.id.tv_account)).getText().toString().trim();
        String tv_type = (((TextView) findViewById(R.id.tv_type)).getText().toString().trim()).equals(accountType[0]) ? "2" : "1";

        if (currentUser == null) {
            ToastUtil.toast("请先登陆");
            return;
        }

        phoneNumber = currentUser.getPhoneNumber();



        if (Float.parseFloat(tv_account) <= 0 || TextUtils.isEmpty(tv_account)) {
            ToastUtil.toast("提现信息不完善");
            finish();

        }else {

            String[] params = new String[]{Constants.AppID, GlobalConfig.AppID, Constants.ECID, GlobalConfig.ECID, Constants.PhoneNumber, phoneNumber,
                    Constants.AppType, GlobalConfig.AppType, Constants.WithdrawAmount, tixianMoney, Constants.WithdrawAccountID, tv_account,
                    Constants.WithdrawType, tv_type, Constants.OperationType, String.valueOf(withdraw_type)};

            httpUtil.callJson(handler, this, GlobalConfig.SERVERROOTPATH + GlobalConfig.SHARER_WITHDRAW, BaseResultBean.class, params);
            Log.d("TAG", "submitRequest: 提交了参数");

        }

    }



    @Override
    protected void getMessage(Message msg) {

        BaseResultBean result = (BaseResultBean) msg.obj;
        if (result != null && result.getResult()) {
            ToastUtil.toast("提交成功,请耐心等待");
            finish();
        } else {
            ToastUtil.toast("操作超时,请重试");
        }
    }



    private void customizeBuilder(AlertDialog.Builder builder, final int requestType) {

        View inflate = LayoutInflater.from(this).inflate(R.layout.sharer_input_dialog_layout, null);
        EditText et = (EditText) inflate.findViewById(R.id.et_input);
        et.setInputType((requestType == FLAG_AMOUNT) ? EditorInfo.TYPE_NUMBER_FLAG_DECIMAL | EditorInfo.TYPE_CLASS_NUMBER : EditorInfo.TYPE_CLASS_TEXT);
        builder.setView(inflate);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AlertDialog aDialog = (AlertDialog) dialog;
                EditText et_input = (EditText) aDialog.findViewById(R.id.et_input);
                String input = et_input.getText().toString().trim();
                if (!TextUtils.isEmpty(input)) {
                    ((TextView) findViewById(requestType == FLAG_AMOUNT ? R.id.tv_withdraw : R.id.tv_account)).setText(input);
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
