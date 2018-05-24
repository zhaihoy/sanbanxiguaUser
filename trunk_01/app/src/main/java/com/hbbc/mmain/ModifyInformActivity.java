package com.hbbc.mmain;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.hbbc.R;
import com.hbbc.mmain.bean.InformCodeBean;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.DataUtil;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.MyCircleImageView;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 修改个人信息页面
 */
public class ModifyInformActivity extends BaseActivity {

    private static final int PHOTOGRAPH = 1;                            //调用相机请求码

    private static final int ALBUM = 2;                                 //调用相册请求码

    private static final int ALBUMCROP = 3;                             //相册照片裁剪

    //=========================数据变量===========================================
    private static String name;                                         //文件名

    private static String path;                                         //文件路径

    private static String base_pic = null;                                //图片Base64

    private static String province = null;                                 //省

    private static String city = null;                                     //市

    //======================控件变量=============================================
    private RelativeLayout modify_infom_include;                       //标题布局

    private ImageView modify_inform_back, modify_man, modify_woman;     //退出,男，女

    private TextView modify_inform_title;                               //标题

    private TextView modify_inform_finish;                              //完成

    private EditText modify_address, modify_cityname, modify_username;  //详细住址，住址，用户名

    private MyCircleImageView modify_inform_pic;                       //头像

    private TextView modify_Detailed, modify_position;                   //电话和详细地址,职位和地区

    private int themeColor = Color.WHITE;                               //主题色

    private int Sex = 1;                                                    //性别标识，默认男

    private String provinceCode;                                          //省份码

    private String cityCode;                                              //城市码



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mmain_modify_main);

        initView();

        //个人信息接口
        new HttpUtil().callJson(handler, 0, this, GlobalConfig.MAPPMAIN_GETUSERINFO, "AppUserID", GlobalConfig.AppUserID, "UserID", GlobalParameter.getManagerid() + GlobalParameter.getMemberid(), "PersonFlag", GlobalParameter.getPersonflag());

    }



    /**
     * 初始化控件
     */
    @Override
    protected void initView() {

        modify_inform_pic = (MyCircleImageView) findViewById(R.id.modify_inform_pic);
        modify_infom_include = (RelativeLayout) findViewById(R.id.modify_infom_include);
        modify_inform_back = (ImageView) findViewById(R.id.modify_inform_back);
        modify_inform_finish = (TextView) findViewById(R.id.modify_inform_finish);
        modify_inform_title = (TextView) findViewById(R.id.modify_inform_title);
        modify_address = (EditText) findViewById(R.id.modify_address);
        modify_cityname = (EditText) findViewById(R.id.modify_cityname);
        modify_username = (EditText) findViewById(R.id.modify_username);
        modify_inform_pic = (MyCircleImageView) findViewById(R.id.modify_inform_pic);
        modify_man = (ImageView) findViewById(R.id.modify_man);
        modify_woman = (ImageView) findViewById(R.id.modify_woman);
        modify_Detailed = (TextView) findViewById(R.id.modify_Detailed);
        modify_position = (TextView) findViewById(R.id.modify_position);

        if (GlobalParameter.getPersonflag().equals("1")) {
            //管理员
            modify_position.setText("职位");
            modify_cityname.setClickable(false);
            modify_Detailed.setText("电话");
            modify_Detailed.setFocusable(false);
            modify_address.setFocusable(false);
        } else if (GlobalParameter.getPersonflag().equals("2")) {
            //会员
            modify_position.setText("地区");
            modify_cityname.setClickable(true);
            modify_Detailed.setText("详细地址");
            modify_cityname.setOnClickListener(this);
            modify_address.setFocusable(true);
        }

        //主题色赋值
        initthemeColor();

        //设置监听事件
        modify_inform_pic.setOnClickListener(this);
        modify_man.setOnClickListener(this);
        modify_woman.setOnClickListener(this);
        modify_inform_back.setOnClickListener(this);
        modify_inform_finish.setOnClickListener(this);
    }



    /**
     * 消息回调机制
     */
    @Override
    protected void getMessage(Message msg) {
        switch (msg.what) {
            case 0:
                //个人信息接口
                updateInfom((String) msg.obj);
                break;
            case 1:
                String data = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    if (jsonObject.getBoolean("Result")) {
                        finish();//退出到个人信息页面
                    }
                    outputShort(jsonObject.getString("Notice"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }

    }



    /**
     * 更新用户信息
     */
    private void updateInfom(String obj) {

        try {
            JSONObject jobj = new JSONObject(obj);
            if (jobj.getBoolean("Result")) {
                if (GlobalParameter.getPersonflag().equals("1")) {

                    //职位
                    if (jobj.has("Position")) {
                        modify_cityname.setText(jobj.getString("Position"));
                    }

                    //用户名
                    if (jobj.has("ManagerName")) {
                        modify_username.setText(jobj.getString("ManagerName"));
                    }

                    //电话
                    if (jobj.has("PhoneNum")) {
                        modify_address.setText(jobj.getString("PhoneNum"));
                    }

                } else if (GlobalParameter.getPersonflag().equals("2")) {

                    //会员姓名
                    if (jobj.has("MemberName")) {
                        modify_username.setText(jobj.getString("MemberName"));
                    }

                    //地址
                    if (jobj.has("Address")) {
                        modify_address.setText(jobj.getString("Address"));
                    }

                    //城市编号
                    if (jobj.has("ProvinceCode")) {
                        //省份默认值
                        provinceCode = jobj.getString("ProvinceCode");
                        //转成实体类
                        InformCodeBean informCodeBean = new Gson().fromJson(getResources().getString(R.string.city_select), InformCodeBean.class);
                        List<InformCodeBean.DtSheng> dtSheng = informCodeBean.getDtSheng();
                        for (InformCodeBean.DtSheng dtSheng1 : dtSheng) {
                            if (dtSheng1.getProvinceCode().equals(jobj.getString("ProvinceCode"))) {
                                //省
                                province = dtSheng1.getProvinceName();
                                Logger.d(province);
                                modify_cityname.setText(province);
                            }
                        }
                    }

                    if (jobj.has("CityCode")) {
                        //城市默认值
                        cityCode = jobj.getString("CityCode");
                        //转成实体类
                        InformCodeBean informCodeBean = new Gson().fromJson(getResources().getString(R.string.city_select), InformCodeBean.class);
                        List<InformCodeBean.DtCity> dtCities = informCodeBean.getDtCity();
                        for (InformCodeBean.DtCity dtCity : dtCities) {
                            if (dtCity.getCityCode().equals(jobj.getString("CityCode"))) {
                                //市
                                city = dtCity.getCityName();
                                //省市
                                modify_cityname.append("-" + city);
                            }
                        }
                    }
                }


                //头像
                if (jobj.has("HeadPicFieldID")) {

                    Glide.with(this).load(jobj.getString("HeadPicFieldID")).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            modify_inform_pic.setImageBitmap(resource);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            resource.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                            byte[] pic = baos.toByteArray();
                            base_pic = Base64.encodeToString(pic, Base64.DEFAULT);
                        }
                    });
                }

                //性别
                if (jobj.has("Sex")) {
                    if (jobj.getBoolean("Sex")) {
                        //男
                        modify_man.setImageResource(R.drawable.mmain_modify_selectm);
                        modify_woman.setImageResource(R.drawable.mmain_modify_women);
                        Sex = 1;
                    } else {
                        //女
                        modify_woman.setImageResource(R.drawable.mmain_modify_selectw);
                        modify_man.setImageResource(R.drawable.mmain_modify_man);
                        Sex = 0;
                    }
                }
            } else {
                outputLong(jobj.getString("Notice"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**
     * 获取主题色，主题色赋值
     */
    private void initthemeColor() {
        //主题色赋值
        themeColor = GlobalParameter.getThemeColor();

        //标题栏颜色赋值
        modify_infom_include.setBackgroundColor(themeColor);

        modify_inform_title.setText("修改信息");

    }



    /**
     * 监听处理事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.modify_inform_back:
                finish();//退出当前页面
                break;

            //头像
            case R.id.modify_inform_pic:
                setAlertDialogPic();
                break;

            //性别男
            case R.id.modify_man:
                modify_man.setImageResource(R.drawable.mmain_modify_selectm);
                modify_woman.setImageResource(R.drawable.mmain_modify_women);
                Sex = 1;
                break;

            //性别女
            case R.id.modify_woman:
                modify_woman.setImageResource(R.drawable.mmain_modify_selectw);
                modify_man.setImageResource(R.drawable.mmain_modify_man);
                Sex = 0;
                break;

            //地区
            case R.id.modify_cityname:
                //跳转到修改城市的页面
                Intent intent = new Intent(this, ProvincePageActivity.class);
                intent.putExtra("ProvinceCityName", modify_cityname.getText().toString());
                startActivityForResult(intent, ProvincePageActivity.PROVINCE_REQUESTCODE);
                break;

            //完成
            case R.id.modify_inform_finish:

                //提交个人信息
                new HttpUtil().callJson(handler, 1, this, GlobalConfig.MAPPMAIN_SETUSERINFO, "AppUserID", GlobalConfig.AppUserID, "PersonFlag", GlobalParameter.getPersonflag(), "UserID", GlobalParameter.getManagerid() + GlobalParameter.getMemberid(), "HeadPicFieldID", base_pic, "Sex", Sex + "", "ProvinceCode", provinceCode, "CityCode", cityCode, "Address", modify_address.getText().toString());
                break;
        }

    }



    /**
     * 点击头像后的业务逻辑
     */
    private void setAlertDialogPic() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.mmain_modify_picdlg, null);
        TextView photograph = (TextView) view.findViewById(R.id.modify_pic_photograph);
        TextView album = (TextView) view.findViewById(R.id.modify_pic_album);
        TextView cancel = (TextView) view.findViewById(R.id.modify_pic_cancel);

        //设置视图显示
        alertBuilder.setView(view);
        final AlertDialog alert = alertBuilder.create();
        alert.show();

        //设置监听
        photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用相机
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    //设置图片路径
                    path = Environment.getExternalStorageDirectory().getPath();
                    //图片文件名
                    name = DataUtil.getNowDateString() + ".jpg";
                    //调用相机
                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //文件名
                    File imgFile = new File(path + File.separator + name);

                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));

                    startActivityForResult(intent1, PHOTOGRAPH);
                }
                alert.cancel();
            }
        });
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用相册
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*"), ALBUM);
                alert.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                alert.cancel();
            }
        });

    }



    /**
     * 调用相册相机的业务逻辑
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTOGRAPH && resultCode == Activity.RESULT_OK) {
            getPhtoGraphPic();
        } else if (requestCode == ALBUM && resultCode == Activity.RESULT_OK && data != null) {
            //调用裁剪界面
            Uri uri1 = data.getData();
            startPhotoZoom(uri1);
        } else if (requestCode == ALBUMCROP && resultCode == Activity.RESULT_OK && data != null) {
            getCropPic(data);

        } else if (requestCode == ProvincePageActivity.PROVINCE_REQUESTCODE && resultCode == ProvincePageActivity.PROVINCE_RESULTCODE && data != null) {
            getProviCity(data);
        }
    }



    /**
     * 获取相机图片并裁剪
     */
    private void getPhtoGraphPic() {
        //图片名称
        File file = new File(path + File.separator + name);
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
            intent.putExtra("crop", "true");
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // outputX outputY 是裁剪图片宽高
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, ALBUMCROP);
        }
    }



    /**
     * 裁剪后的相册图片
     */
    private void getCropPic(Intent data) {
        Bundle bun = data.getExtras();
        Bitmap bitmap = null;
        if (bun != null) {
            bitmap = bun.getParcelable("data");

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            byte[] bytes = out.toByteArray();
            //提交Base64图
            base_pic = Base64.encodeToString(bytes, Base64.DEFAULT);
            //设置头像图片
            modify_inform_pic.setImageBitmap(bitmap);
        }
    }



    /**
     * 获取省份和城市名称
     */
    private void getProviCity(Intent data) {
        //返回省份城市名称
        ArrayList<InformCodeBean.DtCity> CodeList = (ArrayList<InformCodeBean.DtCity>) new Gson().fromJson(getResources().getString(R.string.city_select), InformCodeBean.class).getDtCity();
        for (InformCodeBean.DtCity informCodeBean : CodeList) {
            if (informCodeBean.getCityName().equals(data.getStringExtra("cityName"))) {
                //获取城市编号
                cityCode = informCodeBean.getCityCode();
                provinceCode = informCodeBean.getProvinceCode();
            }
        }
        if (data.getStringExtra("provinceName").equals("") || data.getStringExtra("cityName").equals("")) {
            //刚进入时的数据
            String provinceCityName = province + "-" + city;
            modify_cityname.setText(provinceCityName);
        } else {
            //返回的数据
            String provinceCityName = data.getStringExtra("provinceName") + "-" + data.getStringExtra("cityName");
            //显示最新的数据
            modify_cityname.setText(provinceCityName);
        }
    }



    /**
     * 裁剪图片
     */
    public void startPhotoZoom(Uri uri) {
        Logger.d(uri.toString());
        /*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页 安卓系统早已经有自带图片裁剪功能, 是直接调本地库的
		 */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.setData(uri);
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, ALBUMCROP);
    }

}
