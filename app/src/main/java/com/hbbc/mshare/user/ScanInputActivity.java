package com.hbbc.mshare.user;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.hbbc.R;
import com.hbbc.mshare.user.detail.Detail_Bean;
import com.hbbc.mshare.user.gongyu_section.UnLockActivity;
import com.hbbc.mshare.user.main.ResultBean;
import com.hbbc.mshare.user.zhijin_section.BuyPaperActivity;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.ToastUtil;
import com.hbbc.util.refreshview.BeanUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/7.
 */
public class ScanInputActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {

    private static final String TAG = "ScanInputActivity";

    private ImageButton ib_torch;

    private boolean torch_status;

    private Camera camera;

    private Button btn_submit;

    private Button btn_back;

    private HttpUtil httpUtil;

    private EditText et_input;

    private ArrayList<ResultBean.Goods> goodsList;

    //记录:上次点击按钮的时间
    private long lastSubmitTime;

    private String text="1000201";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        overridePendingTransition(R.anim.global_in,0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_scan_input);
        goodsList = (ArrayList<ResultBean.Goods>) getIntent().getSerializableExtra(Constants.GoodsList);
        text = getIntent().getStringExtra("text");
        initView();
        btn_submit.performClick();
    }



    @Override
    protected void initView() {

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        ib_torch = (ImageButton) findViewById(R.id.ib_torch);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        et_input = (EditText) findViewById(R.id.et_input);
        et_input.setText(text);

        ib_torch.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

    }



    @Override
    protected void onPause() {

        super.onPause();
        //如果手电筒还在打开状态,就在此关闭掉
        if (camera != null && camera.getParameters().getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)
                && torch_status) {
            closeTorch();
        }
    }



    @Override
    protected void onStop() {
//如果手电筒还在打开状态,就在此关闭掉
        if (camera != null
                && torch_status) {
            closeTorch();
        }
        super.onStop();

    }



    @Override
    public void onClick(View v) {

        super.onClick(v);
        switch (v.getId()) {
            case R.id.ib_torch://打开或关闭手电筒
                if (torch_status) {//close torch
                    closeTorch();
                } else {//open torch
                    openTorch();
                }
//                torch_status=!torch_status;
                break;
           case R.id.btn_submit://向服务器提交输入的编码|关闭自身|跳入详情页面
                //防抖动
                if(System.currentTimeMillis()-lastSubmitTime>2000L) {
                    submit();
                    lastSubmitTime = System.currentTimeMillis();
                }
                break;
        }
    }



    /**
     * 根据输入的SN码向服务器请求物品详情
     */
    private void submit() {


        String  SN_Code = et_input.getText().toString().trim();

        if (!isSNCodeLegal(SN_Code)) {
            ToastUtil.toast("非法的物品编码,请重新输入");
            return;
        }

        if (isInUsageStatus(SN_Code)) {
            ToastUtil.toast("当前物品正在使用中,请重新选择");
            return;
        }
        if (httpUtil == null)
            httpUtil = new HttpUtil();
        String[] params = new String[]{Constants.ECID, GlobalConfig.ECID, Constants.AppID, GlobalConfig.AppID, Constants.AppType, GlobalConfig.AppType, Constants.PhoneNumber, GlobalParameter.getPhonenum(), Constants.GoodsSNID, SN_Code};
        httpUtil.callJson(handler, this, GlobalConfig.SERVERROOTPATH + GlobalConfig.MSHARE_RETRIEVE_DETAILS_OF_SELECTED_PRODUCTE,
                Detail_Bean.class, params);
    }



    private boolean isInUsageStatus(String SN_Code) {

        if (goodsList == null || goodsList.size() == 0) {
            ToastUtil.toast("当前物品不存在,请重新选择");
            finish();
            return true;

        }
        return false;
    }



    @Override
    protected void getMessage(Message msg) {

        Detail_Bean detail_bean = (Detail_Bean) msg.obj;
        BeanUtils.putString("detail_bean", detail_bean);
        Log.d("tag", "getMessage: detail_bean====" + detail_bean.toString());
        if (detail_bean == null || !detail_bean.isResult()) {//返回结果为空||返回结果码错误
            ToastUtil.toast("请确认号码是否正确");
            return;
        }
        int appId = Integer.valueOf(GlobalConfig.AppID);
        Class target = null;
        switch (appId) {
            case 20:
                target = UnLockActivity.class;
                break;
            case 21:

                break;
            case 22:

                break;
            case 23:

                break;
            case 24:

                break;
            case 25:

                break;
            default:

                break;
        }
        Intent intent = new Intent(this,BuyPaperActivity.class);
        intent.putExtra("detail_bean", detail_bean);
        startActivity(intent);
        finish();//关闭当前页面
        overridePendingTransition(R.anim.global_in, R.anim.global_out_left);

    }



    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(0, R.anim.global_out_left);
    }



    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(0, R.anim.global_out);
    }



    private boolean isSNCodeLegal(String SN_Code) {

        return true;
    }



    private void openTorch() {

        camera = (camera == null) ? Camera.open() : camera;
        Camera.Parameters params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        //先注掉,看7.0的系统如何配置以实现手电筒功能的
//        try {
//            camera.setPreviewTexture(new SurfaceTexture(0));
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d("Torch", "openTorch: >>>>>>>>>>>");
//        }
        camera.startPreview();
        torch_status = true;//打开状态
    }



    private void closeTorch() {

        if (camera == null)
            return;
        Camera.Parameters params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
//        try {
//            camera.setPreviewTexture(new SurfaceTexture(0));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        camera.stopPreview();
        torch_status = false;//关闭状态
    }



    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (camera != null)//释放资源
            camera.release();
    }



    @Override
    public void setOnLeftClick() {

        finish();
        overridePendingTransition(0, R.anim.global_out);
    }



    @Override
    public void setOnRightClick() {

    }
}
