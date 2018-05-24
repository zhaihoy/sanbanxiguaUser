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

import com.google.gson.Gson;
import com.hbbc.R;
import com.hbbc.mmain.bean.InformCodeBean;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.GlobalParameter;
import com.hbbc.util.MyTopbar;

import java.util.ArrayList;

/**
 * 选择省份页面
 */
public class ProvincePageActivity extends BaseActivity implements AdapterView.OnItemClickListener, MyTopbar.OnTopBarClickListener {

    //请求码
    public static final int PROVINCE_REQUESTCODE = 10;

    //响应码
    public static final int PROVINCE_RESULTCODE = 11;


    private TextView modify_currrent_area;                    //当前地区

    private ListView mmain_province_list;                     //所有省份

    private MyTopbar province_include;                        //标题栏布局

    private ArrayList<InformCodeBean.DtSheng> DtShengList; //城市数据

    private InformCodeBean bean = new InformCodeBean();      //实体数据

    private int themeColor = Color.WHITE;                       //主题色



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mmain_modify_prov);
        initView();
    }



    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        mmain_province_list = (ListView) findViewById(R.id.mmain_province_list);
        province_include = (MyTopbar) findViewById(R.id.province_include);
        modify_currrent_area = (TextView) findViewById(R.id.modify_currrent_area);
        //主题色赋值
        initthemeColor();

        //当前地区赋值
        Intent intent = getIntent();
        String provincecityname = intent.getStringExtra("ProvinceCityName");
        modify_currrent_area.setText(provincecityname);

        //获取所有省份数据
        bean = new Gson().fromJson(getResources().getString(R.string.city_select), InformCodeBean.class);
        DtShengList = (ArrayList<InformCodeBean.DtSheng>) bean.getDtSheng();

        //适配器
        mmain_province_list.setAdapter(new MyBaseAdapter());
        //条目点击事件
        mmain_province_list.setOnItemClickListener(this);
        //返回监听事件
        province_include.setOnTopBarClickListener(this);
    }



    /**
     * 主题色赋值
     */
    private void initthemeColor() {
        //主题色赋值
        themeColor= GlobalParameter.getThemeColor();

        province_include.setBackgroundColor(themeColor);

        province_include.setTitle("选择省份");

    }



    /**
     * 条目的点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ArrayList<InformCodeBean.DtCity> cities = (ArrayList<InformCodeBean.DtCity>) bean.getDtCity();
        ArrayList<String> cityList = new ArrayList<>();

        //遍历城市加入cityList
        for (InformCodeBean.DtCity city : cities) {
            //城市列表
            if (city.getProvinceCode().equals(DtShengList.get(position).getProvinceCode())) {
                //将城市数据加入列表中
                cityList.add(city.getCityName());
            }
        }

        //跳转城市信息页面
        Intent intent = new Intent(this, CityPageActivity.class);
        intent.putExtra("provinceName", DtShengList.get(position).getProvinceName());
        intent.putStringArrayListExtra("cityList", cityList);
        startActivityForResult(intent, CityPageActivity.CITY_REQUESTCODE);
    }



    /**
     * 返回监听事件
     */
    @Override
    public void onClick(View v) {
        finish();
    }



    /**
     * 页面返回数据信息
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CityPageActivity.CITY_REQUESTCODE && resultCode == CityPageActivity.CITY_RESULTCODE && data != null) {
            //返回修改信息的省份城市名
            Intent intent = new Intent();
            intent.putExtra("cityName", data.getStringExtra("cityName"));
            intent.putExtra("provinceName", data.getStringExtra("provinceName"));
            setResult(PROVINCE_RESULTCODE, intent);
            finish();
        }

    }



    @Override
    public void setOnLeftClick() {
        finish();
    }



    @Override
    public void setOnRightClick() {

    }



    /**
     * 省份列表适配器
     */
    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return DtShengList.size();
        }



        @Override
        public Object getItem(int position) {
            return DtShengList.get(position);
        }



        @Override
        public long getItemId(int position) {
            return position;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MyViewHolder myViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ProvincePageActivity.this).inflate(R.layout.mmain_modify_provcity, null);
                myViewHolder = new MyViewHolder(convertView);
                convertView.setTag(myViewHolder);
            } else {
                myViewHolder = (MyViewHolder) convertView.getTag();
            }
            myViewHolder.textView.setText(DtShengList.get(position).getProvinceName());
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
