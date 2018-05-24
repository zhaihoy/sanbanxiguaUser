package com.hbbc.mshare.reservation;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.mshare.HDPicActivity;
import com.hbbc.mshare.OrderPaymentActivity;
import com.hbbc.mshare.detail.Detail_Bean;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.CustomGridView;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.ToastUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/17.
 *
 */
public class ReservationActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener, AdapterView.OnItemClickListener {

    private Detail_Bean detail_bean;

    private CustomGridView grid_label;

    private CustomGridView grid_img;

    private ImageView iv_img;//头像控件

    private ArrayList<String> labels;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_reserve);
        detail_bean = (Detail_Bean) getIntent().getSerializableExtra("detail_bean");
        labels = getIntent().getStringArrayListExtra("labels");
        initView();

//        getHeapInfo();
    }



    private void getHeapInfo() {

        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int heapSize = manager.getMemoryClass();
        Log.d("tag", "getHeapInfo: heapSize===" + heapSize);//192MB wow!!!
    }



    @Override
    protected void initView() {

        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        iv_img = (ImageView) findViewById(R.id.iv_img);
        TextView tv_price = (TextView) findViewById(R.id.tv_price);//物品使用单价
        tv_price.setText(String.valueOf(detail_bean.getGoodsUsePrice()));
        TextView tv_desc= (TextView) findViewById(R.id.tv_desc);//物品描述信息
        tv_desc.setText(detail_bean.getGoodsIntroduceText());
        TextView tv_amount= (TextView) findViewById(R.id.tv_amount);//底部金额
        tv_amount.setText(String.valueOf(detail_bean.getGoodsDeposit()));

        grid_label = (CustomGridView) findViewById(R.id.grid_label);
        grid_img = (CustomGridView) findViewById(R.id.grid_image);

        Button btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(this);

        if (detail_bean != null)
            bindDataIfNotNull();

    }

    /**
     * 如果上个界面传过来了Detail_Bean了,那么就绑定数据到界面
     */
    private void bindDataIfNotNull() {
        // TODO: 2017/8/9  
        Glide.with(this).load(detail_bean.getPicList().get(0)).placeholder(R.mipmap.mshare_logo).into(iv_img);

//        String[] data = new String[]{"使用年限 九成", "折旧率 80%", "外观情况 完好"};
        ArrayAdapter<String> label_adapter = new ArrayAdapter<>(this, R.layout.mshare_grid_label, labels);
        grid_label.setAdapter(label_adapter);

        final ReservationImageAdapter imageAdapter = new ReservationImageAdapter();
        grid_img.setAdapter(imageAdapter);
        // TODO: 2017/8/9 这个地方要注意,解决的是怎么个问题
        grid_img.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                //改写Glide缓存策略,让此页面中的图片直接来自外存储中.原图缓存到SD卡中.
                imageAdapter.setData(detail_bean.getPicList());
                grid_img.removeOnLayoutChangeListener(this);

            }
        });

        grid_img.setOnItemClickListener(this);

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

        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_pay:
                //从此入口跳转到"物品订单支付页面",还是要把Detail_Bean传递过去.
                Intent intent = new Intent(this, OrderPaymentActivity.class);
                intent.putExtra("detail_bean",detail_bean);
                intent.putStringArrayListExtra("labels",labels);
                startActivity(intent);
                overridePendingTransition(R.anim.global_in, 0);
                break;
        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ToastUtil.toast("item clicked...");
        //开启一个新的全屏页面,显示大图,并实现双击放大缩小功能.
        if(detail_bean==null||detail_bean.getPicList().size()==0){
            return;
        }
        Intent intent=new Intent();
        intent.setClass(this,HDPicActivity.class);
        intent.putStringArrayListExtra("picList", (ArrayList<String>) detail_bean.getPicList());
        intent.putExtra("selectedPosition",position);
        startActivity(intent);


    }
}
