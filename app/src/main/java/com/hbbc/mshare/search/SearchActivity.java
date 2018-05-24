package com.hbbc.mshare.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.hbbc.R;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */
public class SearchActivity extends BaseActivity
        implements TextWatcher, Inputtips.InputtipsListener, AdapterView.OnItemClickListener {

    private static final String SELECTED_ITEM = "selected_item";

    private ListView inputList;

    private List<Tip> tipList;

    private AutoCompleteTextView autoCompleteTextView;

    private SimpleAdapter tip_adapter;

    private SimpleAdapter history_adapter;

    private List<SearchHistoryBean> historys;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mshare_search);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.input_edittext);
        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        inputList = (ListView) findViewById(R.id.inputlist);
        TextView tv_location = (TextView) findViewById(R.id.tv_location);

        autoCompleteTextView.addTextChangedListener(this);
        inputList.setOnItemClickListener(this);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        //把传递过来的amapLocation中的地址显示到控件上
        String currentAddress = getIntent().getStringExtra("currentAddress");
        if (currentAddress != null)
            tv_location.setText(currentAddress);
        ToastUtil.toast_debug(currentAddress);


        showSearchHistory();
    }



    private void showSearchHistory() {
        //Todo 要不要起一个子线程呢?
        historys = HistoryDao.getDaoInstance(this).queryForAll();
        List<HashMap<String, String>> listString = new ArrayList<>();
        for (int i = 0; i < historys.size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", historys.get(i).getName());
            map.put("address", historys.get(i).getAddress());
            listString.add(map);
        }

        history_adapter = new SimpleAdapter(getApplicationContext(), listString, R.layout.mshare_search_item_layout,
                new String[]{"name", "address"}, new int[]{R.id.poi_field_id, R.id.poi_value_id});
        inputList.setAdapter(history_adapter);
        history_adapter.notifyDataSetChanged();
    }



    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String newText = s.toString().trim();
        if (TextUtils.isEmpty(newText)) {
            //当editText被删除空时,显示历史记录
            ToastUtil.toast_debug("显示历史记录");
            showSearchHistory();
            return;
        }
        InputtipsQuery inputtipsQuery = new InputtipsQuery(newText, GlobalConfig.currentCity);
        Inputtips inputtips = new Inputtips(this, inputtipsQuery);
        inputtips.setInputtipsListener(this);
        inputtips.requestInputtipsAsyn();
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }



    @Override
    public void afterTextChanged(Editable s) {

    }



    /**
     * 获取返回的搜索结果,并展示到listview上
     *
     * @param tipList 提示结果集合
     * @param rCode   查询的结果码 (AMapException.CODE_AMAP_SUCCESS)
     */
    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {

        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            this.tipList = tipList;
            List<HashMap<String, String>> listString = new ArrayList<>();
            for (int i = 0; i < tipList.size(); i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", tipList.get(i).getName());
                map.put("address", tipList.get(i).getAddress());
                listString.add(map);
            }
            tip_adapter = new SimpleAdapter(getApplicationContext(), listString, R.layout.mshare_search_item_layout,
                    new String[]{"name", "address"}, new int[]{R.id.poi_field_id, R.id.poi_value_id});
            inputList.setAdapter(tip_adapter);
            tip_adapter.notifyDataSetChanged();
        } else {
            outputShort("oops...查询失败");
        }
    }



    /**
     * 点击后把点击位置对应的对象Tip传递给主页面,并关闭自身
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String input = autoCompleteTextView.getText().toString().trim();
        Object selectedItem;
        SearchBean searchBean;
        if (TextUtils.isEmpty(input)) {
            selectedItem = historys.get(position);
            searchBean=new SearchBean(new LatLonPoint(historys.get(position).getLat(),historys.get(position).getLng()));
        } else {
            selectedItem = tipList.get(position);
            searchBean=new SearchBean(tipList.get(position).getPoint());

        }
        if (selectedItem == null) {
            ToastUtil.toast("该地址无效,请重新搜索");
            return;
        }
        //把选中的对象tip保存到本地数据库中
        saveSelectedTip2DB(selectedItem);

        Intent intent = new Intent();
        intent.putExtra(SELECTED_ITEM, searchBean);
        setResult(RESULT_OK, intent);
        finish();
    }



    private void saveSelectedTip2DB(Object item) {

        String input = autoCompleteTextView.getText().toString().trim();
        SearchHistoryBean bean;
        if (TextUtils.isEmpty(input)) {
            //输入为空时,说明此时显示的是历史记录列表
            bean = (SearchHistoryBean) item;
            bean.setMillis(System.currentTimeMillis());//Caution:一定不要忘记把它的currentMillis重置为最新时刻!!!

        } else {
            //输入不为空,说明此时显示的是搜索记录
            Tip tip = (Tip) item;
            if (tip == null)
                return;
            bean = new SearchHistoryBean(0, tip.getName(), tip.getAddress(),
                    System.currentTimeMillis(), tip.getPoint().getLatitude(), tip.getPoint().getLongitude());
        }

        HistoryDao dao = HistoryDao.getDaoInstance(this);
        int id = dao.query(bean);
        if (id != -1) {
            //先查一下,看它的id,现更新此id对应的row.
            bean.setId(id);
            int update = dao.update(bean);
            Toast.makeText(SearchActivity.this, "update_columns===" + update, Toast.LENGTH_SHORT).show();
        } else
            dao.insert(bean);
    }



    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
