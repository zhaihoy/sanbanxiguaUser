package com.hbbc.mshare.user;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.hbbc.R;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.mshare.user.detail.Detail_Bean;
import com.hbbc.mshare.user.main.ResultBean;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.LogUtil;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.ToastUtil;
import com.hbbc.zxing.camera.CameraManager;
import com.hbbc.zxing.decoding.CaptureActivityHandler;
import com.hbbc.zxing.decoding.InactivityTimer;
import com.hbbc.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class ScanActivity extends BaseActivity implements Callback, OnClickListener, MyTopbar.OnTopBarClickListener {

    private static final float BEEP_VOLUME = 0.10f;

    private static final long VIBRATE_DURATION = 200L;

    private static final int REQUESTS_BEAN = 1;

    private static final int REQUESTWATER_BEAN = 7;

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {

            mediaPlayer.seekTo(0);
        }
    };



    private HttpUtil httpUtil;

    private CaptureActivityHandler handler;

    private ViewfinderView viewfinderView;

    private boolean hasSurface;

    private Vector<BarcodeFormat> decodeFormats;

    private String characterSet;

    private InactivityTimer inactivityTimer;

    private MediaPlayer mediaPlayer;

    private boolean playBeep;

    private boolean vibrate;

    private TextView tv_input;

    private TextView tv_torch;

    //indicate current torch status
    private boolean torch_status;

    private int Camera_Request_Code = 101;

    private ArrayList<ResultBean.Goods> goodsList;// TODO: 2017/11/6 二维码这里也需要判断:如果是使用中或待审核状态的物品,不能跳转/查看/预定

    private String text;

    private String detail_bean;

    private Detail_Bean detail_bean1;

    private UserBean userBean;



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        overridePendingTransition(0, R.anim.global_out);
        requestFullScreen();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mshare_scan_capture);

        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        initView();
        goodsList = (ArrayList<ResultBean.Goods>) getIntent().getSerializableExtra(Constants.GoodsList);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        if (Build.VERSION.SDK_INT > 23)
            //单独为闪光灯,请求Camera权限
            requestCameraPermission();
    }



    private void requestCameraPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Camera_Request_Code);
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    protected void initView() {

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);


        tv_input = (TextView) findViewById(R.id.tv_input);
        tv_torch = (TextView) findViewById(R.id.tv_torch);
        tv_input.setOnClickListener(this);
        tv_torch.setOnClickListener(this);

    }



    private void requestFullScreen() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }



    @Override
    protected void onResume() {

        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
        overridePendingTransition(R.anim.global_in, R.anim.global_out);
    }



    @Override
    protected void onPause() {

        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }



    @Override
    protected void onDestroy() {

        inactivityTimer.shutdown();
        super.onDestroy();
        overridePendingTransition(R.anim.global_in, R.anim.global_out);
    }



    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(0, R.anim.global_out);
    }



    /**
     * 在此方法中来处理扫描结果
     *
     * @param result  Result
     * @param barcode Bitmap
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void handleDecode(Result result, Bitmap barcode) {

        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();//播放声音与振动
        String resultString = result.getText();

        if (resultString.equals("")) {
            ToastUtil.toast("获取扫码结果失败!无效条码");
        } else {
            // TODO: 2017/7/7 添加处理逻辑,如果结果不为空,直接跳转到此物品的物品的详情页面
            String text = result.getText();
            goodsList = (ArrayList<ResultBean.Goods>) getIntent().getSerializableExtra(Constants.GoodsList);

            //没问题,在此发起网络请求
            LogUtil.debug("handleDecode: sn=============" + text);
            Log.d("ScanActivity", "handleDecode: text====>>>" + text);
            ToastUtil.toast("扫描成功,结果为:" + text);
            Intent intent = new Intent(this, ScanInputActivity.class);
            intent.putExtra(Constants.GoodsList, goodsList);
            intent.putExtra("text", text);
            startActivity(intent);
            finish();
            ////TODO: 2017/8/11 要考虑:1.前一个扫到的码不符合规则后,2.请求服务器返回结果失败时,需要重新开启扫描的情况!
            ////
//            Intent intent=new Intent(this, DetailActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putParcelable("detail_bean",null);
//            intent.putExtra("bundle",bundle);
//            startActivity(intent);
        }
//        finish();
    }


    private void initCamera(SurfaceHolder surfaceHolder) {

        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        hasSurface = false;

    }



    public ViewfinderView getViewfinderView() {

        return viewfinderView;
    }



    public Handler getHandler() {

        return handler;
    }



    public void drawViewfinder() {

        viewfinderView.drawViewfinder();

    }



    private void initBeepSound() {

        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }



    private void playBeepSoundAndVibrate() {

        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {//打开手动输入界面,并关闭当前扫码界面
            case R.id.tv_input:
                Intent intent = new Intent(this, ScanInputActivity.class);
                //并且向页面传输一个用于余额支付的金钱的数量
                intent.putExtra(Constants.GoodsList, goodsList);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.global_in, R.anim.global_out_left);
                break;
            case R.id.tv_torch://打开或关闭手电筒功能
                toggleTorch();
                break;
        }
    }



    private void requestNet() {

        //// TODO: 2017/12/
        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }

        LoginResultBean query = UserDao.getDaoInstance(ScanActivity.this).query();
        String phoneNumber = query.getPhoneNumber();

        String[] params = new String[]{Constants.ECID, GlobalConfig.ECID, Constants.PhoneNumber, phoneNumber,  Constants.AppType, GlobalConfig.AppType,Constants.AppID, GlobalConfig.AppID,
        };
        httpUtil.callJson(handler, REQUESTWATER_BEAN, this, GlobalConfig.SERVERROOTPATH + GlobalConfig.USERWALLET, UserBean.class, params);

    }
    public SubmitRequestListener submitRequestListener;
    public interface SubmitRequestListener {
        void onSubmitRequestSuccess();
    }
    public void setOnSubmitRequestListener(SubmitRequestListener submitRequestListener) {
        this.submitRequestListener = submitRequestListener;
    }



    @Override
    protected void getMessage(Message msg) {

        switch (msg.what) {
            case REQUESTWATER_BEAN:
                userBean = (UserBean) msg.obj;
                submitRequestListener.onSubmitRequestSuccess();
                super.getMessage(msg);
        }
    }


    private void toggleTorch() {

        if (torch_status) {//close torch
            closeTorch();
        } else {//open torch
            openTorch();
        }
        torch_status = !torch_status;
    }



    private void closeTorch() {

        CameraManager.get().deActivateTorch();
    }



    private void openTorch() {

        CameraManager.get().activateTorch();
    }



    @Override
    public void setOnLeftClick() {

        finish();
        overridePendingTransition(0, R.anim.global_out);
    }



    @Override
    public void setOnRightClick() {

    }



    @Override
    public void finish() {

        super.finish();
    }



}