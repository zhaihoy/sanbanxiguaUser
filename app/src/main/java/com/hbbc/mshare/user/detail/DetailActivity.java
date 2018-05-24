package com.hbbc.mshare.user.detail;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.mshare.user.HDPicActivity;
import com.hbbc.mshare.user.reservation.ReservationActivity;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.ToastUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/12.
 *
 */
public class DetailActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener, AdapterView.OnItemClickListener {

    private ImageView iv_top;

    private GridView grid_label;

    private GridView grid_img;

    private Button btn_reserve;

    private Image_Adapter image_adapter;

    private Detail_Bean detail_bean;
    private ArrayList<String> labels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        overridePendingTransition(R.anim.global_in,R.anim.global_out_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_detail);
        detail_bean = (Detail_Bean) getIntent().getSerializableExtra("detail_bean");//在跳转到此activity前就要判断下此bean是否为空
        initView();
    }

    @Override
    protected void initView() {

        MyTopbar topBar = (MyTopbar) findViewById(R.id.top_bar);
        topBar.setOnTopBarClickListener(this);

        iv_top = (ImageView) findViewById(R.id.iv_top);
        grid_label = (GridView) findViewById(R.id.grid_label);
        grid_img = (GridView) findViewById(R.id.grid_image);
        btn_reserve = (Button) findViewById(R.id.btn_reserve);
        TextView tv_price= (TextView) findViewById(R.id.tv_price);
        TextView tv_desc= (TextView) findViewById(R.id.tv_desc);
        btn_reserve.setOnClickListener(this);

        Glide.with(this).load(detail_bean.getPicList().get(0))
                .error(R.drawable.mshare_pic_failure)
                .placeholder(R.drawable.mshare_pic_failure)
                .into(iv_top);

        labels = getLabels();
        Log.d("tag", "initView: LableCount--->"+labels.size());
        if(labels.size()>0)
        {
            grid_label.setVisibility(View.VISIBLE);
            ArrayAdapter<String> label_adapter = new ArrayAdapter<>(this, R.layout.mshare_grid_label, labels);
            grid_label.setAdapter(label_adapter);
        }
        else
        {
            grid_label.setVisibility(View.GONE);
        }

        image_adapter = new Image_Adapter();
        grid_img.setAdapter(image_adapter);
        image_adapter.setData(detail_bean.getPicList());

        tv_desc.setText(detail_bean.getGoodsIntroduceText());
        tv_price.setText(String.valueOf(detail_bean.getGoodsUsePrice()));

        grid_img.setOnItemClickListener(this);

    }


    /**
     * 处理服务器返回的labelContent,返回一个集合labels
     */

    private ArrayList<String> getLabels() {

        ArrayList<String> labels=new ArrayList<>();

        String labelContent=detail_bean.getLabelContent();
        if(TextUtils.isEmpty(labelContent)){
            return labels;
        }
        String[] split = labelContent.split(";");
        for (int i = 0; i < split.length; i++) {
            String[] split1 = split[i].split(",");
            StringBuilder sb=new StringBuilder();
            for (int j = 0; j < split1.length; j++) {
                if(j==1)
                    sb.append(split1[1]).append(" ");
                if(j==2)
                    sb.append(split1[2]);
            }

            labels.add(sb.toString());
        }

        return labels;
    }



    @Override
    public void onClick(View v) {

        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_reserve:
                Intent intent = new Intent(this, ReservationActivity.class);
                intent.putExtra("detail_bean",detail_bean);
                intent.putStringArrayListExtra("labels",labels);
                startActivity(intent);
                overridePendingTransition(R.anim.global_in,R.anim.global_out_left);
                break;
        }
    }


    @Override
    public void setOnLeftClick() {

        finish();
    }



    @Override
    public void setOnRightClick() {

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ToastUtil.toast_debug("item clicked...");
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
