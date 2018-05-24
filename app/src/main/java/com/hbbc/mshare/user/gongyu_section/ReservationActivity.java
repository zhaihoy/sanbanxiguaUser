package com.hbbc.mshare.user.gongyu_section;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.mshare.user.detail.Detail_Bean;
import com.hbbc.mshare.user.main.MainActivity;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.GlideImageLoader;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 *
 */
public class ReservationActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener {


    private Banner banner;

    private Detail_Bean detail_bean;

    private TextView tv_intro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_gongyu_reservation);
        detail_bean = (Detail_Bean) getIntent().getSerializableExtra("detail_bean");
        initView();
    }

    @Override
    protected void initView() {
        initTopBar();
        initBanner();

        Button btn_RoutePlanning = (Button) findViewById(R.id.btn_Routeplanning);
        Button btn_reserve = (Button) findViewById(R.id.btn_reserve);
        tv_intro = (TextView) findViewById(R.id.tv_intro);
        TextView tv_price = (TextView) findViewById(R.id.tv_price);
        TextView tv_location = (TextView) findViewById(R.id.tv_location);
        TextView tv_zhuangtai = (TextView) findViewById(R.id.tv_zhuangtai);

        GridView gridViewForLabel = (GridView) findViewById(R.id.grid_label);


        //设置路径的规划
        btn_RoutePlanning.setOnClickListener(this);
        //设置酒店预订单的路径
        btn_reserve.setOnClickListener(this);


        if(detail_bean == null)
            return;
        tv_intro.setText(detail_bean.getGoodsIntroduceText());
        tv_price.setText(detail_bean.getGoodsUsePrice());
        tv_location.setText("地址: "+detail_bean.getGoodsName());
        tv_zhuangtai.setText("");

        String labelContent = detail_bean.getLabelContent();
        String[] split = labelContent.split(";");
        ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            labels.add(split[i]);
        }
        GongYu_Label_Adapter adapter = new GongYu_Label_Adapter(labels);
        gridViewForLabel.setAdapter(adapter);


    }



    private void initTopBar() {
        MyTopbar topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_reserve:
                if(detail_bean == null){
                    ToastUtil.toast("当前公寓不可预定,请重新选择");
                    return;
                }
                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra("detail_bean",detail_bean);
                startActivity(intent);
                break;
            case R.id.btn_Routeplanning:

                //点击按钮跳转到地图的首页,自动规划路径

                Intent intents = new Intent(ReservationActivity.this, MainActivity.class);
                startActivity(intents);
                break;
        }


    }



    private void initBanner() {

        banner = (Banner) findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader())
                .setBannerAnimation(Transformer.Default)
                .setBannerStyle(BannerConfig.NUM_INDICATOR)
                .isAutoPlay(true)
                .setDelayTime(3000);
        if(detail_bean == null) return;
        List<String> picList = detail_bean.getPicList();
        banner.setImages(picList);

        banner.start();

    }



    @Override
    public void setOnLeftClick() {

        finish();
    }



    @Override
    public void setOnRightClick() {

    }
}
