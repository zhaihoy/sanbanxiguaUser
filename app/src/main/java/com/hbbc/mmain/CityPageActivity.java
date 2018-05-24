package com.hbbc.mmain;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.MyTopbar;

import java.util.ArrayList;

/**
 * 城市选择页面
 */
public class CityPageActivity extends BaseActivity implements AdapterView.OnItemClickListener, MyTopbar.OnTopBarClickListener {

    public static final int CITY_REQUESTCODE = 20;      //请求码

    public static final int CITY_RESULTCODE = 21;       //响应码

    private MyTopbar province_include;            //标题栏布局

    private TextView mmain_modify_procity;//标题，所有城市

    private TextView modify_currrent_area, mmain_modify_area;//隐藏当前地区和城市

    private ListView mmain_province_list;               //所有省份

    private ArrayList<String> cityList;                //城市列表

    private String provinceName;                        //省份信息

    private int themeColor = Color.WHITE;                 //主题色



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mmain_modify_prov);
        //获取城市数据
        Intent intent = getIntent();
        cityList = intent.getStringArrayListExtra("cityList");
        provinceName = intent.getStringExtra("provinceName");

        initView();

    }



    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        province_include = (MyTopbar) findViewById(R.id.province_include);
        mmain_province_list = (ListView) findViewById(R.id.mmain_province_list);
        modify_currrent_area = (TextView) findViewById(R.id.modify_currrent_area);
        mmain_modify_area = (TextView) findViewById(R.id.mmain_modify_area);
        mmain_modify_procity= (TextView) findViewById(R.id.mmain_modify_procity);

        initThemeColor();
        //设置适配器
        mmain_province_list.setAdapter(new MyBaseAdapter());
        mmain_province_list.setOnItemClickListener(this);

        //退出监听事件
        province_include.setOnTopBarClickListener(this);
    }



    /**
     * 点击监听事件
     */
    @Override
    public void onClick(View v) {
        //返回城市信息
        Intent intent = new Intent();
        intent.putExtra("provinceName", "");
        intent.putExtra("cityName", "");
        setResult(CITY_RESULTCODE, intent);
        finish();
    }



    /**
     * 主题色赋值
     */
    private void initThemeColor() {
        //主题色赋值
        themeColor = GlobalParameter.getThemeColor();

        province_include.setBackgroundColor(themeColor);

        mmain_modify_procity.setText("所有城市");

        //隐藏当前城市地区
        modify_currrent_area.setVisibility(View.GONE);
        mmain_modify_area.setVisibility(View.GONE);


        province_include.setTitle("选择城市");
    }



    /**
     * 条目点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //返回城市信息
        Intent intent = new Intent();
        intent.putExtra("provinceName", provinceName);
        intent.putExtra("cityName", cityList.get(position));
        setResult(CITY_RESULTCODE, intent);
        finish();
    }



    @Override
    public void setOnLeftClick() {
        finish();
    }



    @Override
    public void setOnRightClick() {

    }



    /**
     * 适配器
     */
    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cityList.size();
        }



        @Override
        public Object getItem(int position) {
            return cityList.get(position);
        }



        @Override
        public long getItemId(int position) {
            return position;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder myViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(CityPageActivity.this).inflate(R.layout.mmain_modify_provcity, null);
                myViewHolder = new MyViewHolder(convertView);
                convertView.setTag(myViewHolder);
            } else {
                myViewHolder = (MyViewHolder) convertView.getTag();
            }

            myViewHolder.textView.setText(cityList.get(position));
            return convertView;
        }



        class MyViewHolder {
            TextView textView;



            MyViewHolder(View view) {
                textView = (TextView) view.findViewById(R.id.modify_city_item);
            }
        }
    }
}
