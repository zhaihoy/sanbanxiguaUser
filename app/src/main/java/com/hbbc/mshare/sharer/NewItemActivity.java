package com.hbbc.mshare.sharer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.hbbc.R;
import com.hbbc.mshare.login.BaseResultBean;
import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.login.UserDao;
import com.hbbc.util.BaseActivity;
import com.hbbc.util.BitmapEncodeTask;
import com.hbbc.util.Constants;
import com.hbbc.util.GlobalConfig;
import com.hbbc.util.HttpUtil;
import com.hbbc.util.LogUtil;
import com.hbbc.util.MyTopbar;
import com.hbbc.util.PermissionUtil;
import com.hbbc.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by Administrator on 2017/8/18.
 * <p/>
 * 最后再来适配Camera & SDCard 权限
 */
public class NewItemActivity extends BaseActivity implements MyTopbar.OnTopBarClickListener, BitmapEncodeTask.OnPostExecuteListener {

    private static final String TAG = "NewItemActivity";

    private static final int SDCARD_CAMERA_PERMISSION = 101;

    private static final int REQUEST_IMAGE = 1;

    private static final int RETRIEVE_TYPE_INFO = 2;

    private static final int RETRIEVE_SUBMIT_NEW_ITEM = 3;

    private static final int RETRIEVE_SELECTED_ITEM = 4;

    private static final int DELETE_SELECTED_ITEM = 5;

    private static final int PRICE_INPUT = 1001;

    private static final int DEPOSIT_INPUT = 1002;

    private static final int TAG_OPTIONS = 10001;

    private static final int TAG_LABEL_BEAN = 10002;

    private static final int Option_Pick_LatLng = 10003;

    private static final int GOODS_NAME = 10004;


    private RecyclerView rv;

    private ItemTouchHelper itemTouchHelper;

    private Uri uri;

    private RvAdapter adapter;

    private HttpUtil httpUtil;

    private LinearLayout optionContainerBottom;

    private LinearLayout optionContainerTop;

    private OptionItem option_goods_type;

    private OptionItem opiton_business_type;

    private OptionItem option_goods_charge;

    private OptionItem option_deposit;

    private ScrollView scrollView;

    private Button btn_submit;

    private EditText et_intro;

    private int labelCount;//记录有多少动态标签条目,方便提交时,倒序遍历

    private String goodsSNID;

    private MyTopbar topbar;

    private OptionItem option_choose_position;

    private LatLng selectedPosition;

    private OptionItem option_goods_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharer_add_item);

        checkPermission();
        initView();

        if (goodsSNID == null) {
            retrieveTypeInfo();
            topbar.setTitle("新增物品");
        } else {
            retrieveGoodsInfo();//如果是编辑页面
            topbar.setTitle("物品编辑");
        }

    }



    /**
     * 根据指定的SNID获取该物品的详情信息
     */
    private void retrieveGoodsInfo() {

        if (httpUtil == null)
            httpUtil = new HttpUtil();

        UserDao userDao = UserDao.getDaoInstance(this);
        LoginResultBean currentUser = userDao.query();
        if (currentUser == null) {
            ToastUtil.toast("当前没有登陆用户");
            return;
        }

        String phoneNumber = currentUser.getPhoneNumber();

        String[] params = new String[]{Constants.AppID,GlobalConfig.AppID,Constants.ECID, GlobalConfig.ECID,
                Constants.PhoneNumber, phoneNumber, Constants.GoodsSNID, goodsSNID};
        httpUtil.callJson(handler, RETRIEVE_SELECTED_ITEM, this,
                GlobalConfig.SHARER_SERVER_ROOT + GlobalConfig.SHARER_RETRIEVE_SELECTED_PRODUCT_INFO,
                ItemBean.class, params);

    }



    @Override
    protected void initView() {

        topbar = (MyTopbar) findViewById(R.id.top_bar);
        topbar.setOnTopBarClickListener(this);

        scrollView = (ScrollView) findViewById(R.id.top_scroll);

        rv = (RecyclerView) findViewById(R.id.rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rv.setLayoutManager(gridLayoutManager);

//        rv.setHasFixedSize(true);
        adapter = new RvAdapter();
        rv.setAdapter(adapter);
        itemTouchHelper = new ItemTouchHelper(new MyItemTouchHandler(adapter));
        itemTouchHelper.attachToRecyclerView(rv);
        //选项容器,动态添加可选择的标签
        optionContainerTop = (LinearLayout) findViewById(R.id.optionContainer_top);//上部固定标签容器
        optionContainerBottom = (LinearLayout) findViewById(R.id.optionContainer_down);//下部动态标签容器


        et_intro = (EditText) findViewById(R.id.et_intro);
        et_intro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //检测:获取焦点时,EditText的位置,不够就滚动ScrollView
                //TODO:
                if (hasFocus) {

                }


            }
        });
        //得到五个固定的条目
        option_goods_name = (OptionItem) findViewById(R.id.option_goods_name);
        option_goods_type = (OptionItem) findViewById(R.id.option_goods_type);
        opiton_business_type = (OptionItem) findViewById(R.id.option_buisness_type);
        option_goods_charge = (OptionItem) findViewById(R.id.option_charge);
        option_deposit = (OptionItem) findViewById(R.id.option_deposit);
        option_choose_position = (OptionItem) findViewById(R.id.option_latlng);

        option_goods_name.setOnClickListener(this);
        option_goods_charge.setOnClickListener(this);
        option_deposit.setOnClickListener(this);
        opiton_business_type.setOnClickListener(this);
        option_goods_type.setOnClickListener(this);
        option_choose_position.setOnClickListener(this);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        //
        Button btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            goodsSNID = intent.getStringExtra(Constants.GoodsSNID);
            Log.d(TAG, "initView: snid of NewItem====>" + goodsSNID);
            btn_delete.setVisibility(goodsSNID == null ? View.GONE : View.VISIBLE);

        } else {

        }

    }



    /**
     * 网络访问,获取类型信息
     */
    private void retrieveTypeInfo() {

        if (httpUtil == null)
            httpUtil = new HttpUtil();

        UserDao userDao = UserDao.getDaoInstance(this);
        LoginResultBean currentUser = userDao.query();
        if (currentUser == null) {
            ToastUtil.toast("当前没有登陆用户");
            return;
        }
        String phoneNumber = currentUser.getPhoneNumber();

        String[] params = new String[]{Constants.AppID,GlobalConfig.AppID,Constants.ECID, GlobalConfig.ECID, Constants.PhoneNumber, phoneNumber, Constants.AppType, GlobalConfig.AppType};
        httpUtil.callJson(handler, RETRIEVE_TYPE_INFO, this, GlobalConfig.SHARER_SERVER_ROOT + GlobalConfig.SHARER_RETRIEVE_ALL_TYPE_INFO,
                GoodsTypeLabelBean.class, params);


    }



    @Override
    protected void getMessage(Message msg) {

        switch (msg.what) {
            case RETRIEVE_TYPE_INFO:
                GoodsTypeLabelBean typeLabelBean = (GoodsTypeLabelBean) msg.obj;
                if (typeLabelBean != null) {
                    Log.d(TAG, "getMessage: typeLabelBean=====" + typeLabelBean.toString());
                    List<TypeBean> typeBeans = typeLabelBean.getGoodsTypeList();
                    List<LabelBean> labelBeans = typeLabelBean.getTypeLabelList();

                    //绑定到物品类型条目
                    if (typeBeans != null) {
                        option_goods_type.setTag(R.id.TAG_TYPE_LIST, typeBeans);
                        option_goods_type.setTag(R.id.TAG_LABEL_LIST, labelBeans);
                        //默认显示线性表中第一个物品类型 ##不让默认显示任何东西#
                    }

                    //动态添加label标签
//                    if (labelBeans != null) {
//                        labelCount = labelBeans.size();
//                        addLabelOptions(labelBeans);//现在是一股脑地全部标签添加进去了,要分类添加,默认添加对应于type的第一个,字段GoodsTypeID
//                    }
                    showDefaultSelection();

                }
                break;
            case RETRIEVE_SUBMIT_NEW_ITEM:
                BaseResultBean resultBean = (BaseResultBean) msg.obj;
                Log.d(TAG, "getMessage: result=-=>" + resultBean.toString());
                if (resultBean.getResult()) {
                    ToastUtil.toast("提交成功");
                }
                break;
            case RETRIEVE_SELECTED_ITEM:
                ItemBean selectedItem = (ItemBean) msg.obj;
                if (selectedItem != null && selectedItem.getResult()) {
                    showSelectedProductInfo(selectedItem);
                } else {
                    ToastUtil.toast_debug("获取失败");
                }
                break;
            case DELETE_SELECTED_ITEM:
                BaseResultBean result = (BaseResultBean) msg.obj;
                if (result != null && result.getResult()) {
                    ToastUtil.toast("删除成功");
                    Intent intent = new Intent();
                    intent.putExtra("DELETED_MARKER_SNID", Integer.valueOf(goodsSNID));
                    setResult(RESULT_OK,intent);
                    finish();
                }
                break;
        }
    }


    //新增物品时,默认选中第一种类型
    private void showDefaultSelection() {

        List<TypeBean> typeBeans = (List<TypeBean>) option_goods_type.getTag(R.id.TAG_TYPE_LIST);
        if(typeBeans == null || typeBeans.size() <= 0){
            return;
        }
        option_goods_type.setTag(R.id.TAG_TYPE, typeBeans.get(0));//让此条目对象指向与它关联的另外一个类型对象,方便提交时读取数据
        option_goods_type.setContent(typeBeans.get(0).getGoodsTypeName());
        //并且删除标签容器中所有标签条目,并添加当前类型所对应的所有标签条目
        optionContainerBottom.removeAllViews();
        TypeBean typeBean = (TypeBean) option_goods_type.getTag(R.id.TAG_TYPE);
        List<LabelBean> labelBeans = (List<LabelBean>) option_goods_type.getTag(R.id.TAG_LABEL_LIST);
        String typeID = typeBean.getGoodsTypeID();
        if (labelBeans != null && labelBeans.size() > 0) {
            List<LabelBean> list = new ArrayList<>();
            for (int i = 0; i < labelBeans.size(); i++) {
                LabelBean label = labelBeans.get(i);
                if (label.getGoodsTypeID() != null
                        && label.getGoodsTypeID().equals(typeID)) {//这两个ID相等是判断的关键,你要注意!
                    list.add(labelBeans.get(i));
                }
            }
            addLabelOptions(list);//更新标签容器中的所有条目
        }

    }



    /**
     * 将选中的商品的信息在界面展示出来以供修改
     */
    private void showSelectedProductInfo(ItemBean item) {

        Log.d(TAG, "showSelectedProductInfo: itemBean====>>>>" + item.toString());

        adapter.data.clear();
        for (String e : item.getPicList()) {
            adapter.data.add(e);
        }

        adapter.notifyDataSetChanged();

        List<TypeBean> typeBeans = item.getGoodsTypeList();
        List<LabelBean> labelBeans = item.getTypeLabelList();

        //绑定到物品类型条目
        if (typeBeans != null) {
            option_goods_type.setTag(R.id.TAG_TYPE_LIST, typeBeans);
            option_goods_type.setTag(R.id.TAG_LABEL_LIST, labelBeans);

            for (int i = 0; i < typeBeans.size(); i++) {
                if (item.getGoodsTypeID().equals(typeBeans.get(i).getGoodsTypeID())) {
                    option_goods_type.setContent(typeBeans.get(i).getGoodsTypeName());
                    option_goods_type.setTag(R.id.TAG_TYPE, typeBeans.get(i));
                    break;
                }
            }

        }

        //如果是付押金模式 , 要将押金条目显示出来
        option_goods_charge.setContent((item.getGoodsUsePrice() != null ? item.getGoodsUsePrice() : "0") + GlobalConfig.PriceType);
        opiton_business_type.setContent(item.getBusinessType().equals("1") ? GlobalConfig.BUSINESS_TYPE[0] : GlobalConfig.BUSINESS_TYPE[1]);
        option_deposit.setVisibility(item.getBusinessType().equals("1") ? View.GONE : View.VISIBLE);
        option_deposit.setContent(item.getGoodsDeposit() == null ? "0" : item.getGoodsDeposit() + "元");
        et_intro.setText(item.getGoodsIntroduceText());
        option_goods_name.setContent(item.getGoodsName());
        selectedPosition = new LatLng(item.getLat(), item.getLng());
        option_choose_position.setContent("位置选取成功");

        showDefaultSelection();
//        addLabelOptions(labelBeans);

        initLabelContent(item.getLabelContent());

    }



    private void initLabelContent(String labelContent) {//labelContent格式: 'key,value;key,value;'

        if(labelContent==null)return;
        String[] splits = labelContent.split(";");
        for (int i = 0; i < splits.length; i++) {
            String split = splits[i];
            String[] keyValue = split.split(",");
            String defaultLabelContent = keyValue[1];
            OptionItem item = (OptionItem) optionContainerBottom.getChildAt(i);
            item.setContent(defaultLabelContent);
        }


    }



    /**
     * 添加所有的标签选项
     */
    private void addLabelOptions(List<LabelBean> labelBeans) {

        //添加若干个非固定的标签条目
        for (int i = 0; i < labelBeans.size(); i++) {
            LabelBean labelBean = labelBeans.get(i);
            addSingleItem(labelBean);
        }

    }

    /**
     * 根据一个labelBean,添加一个标签条目
     */
    private void addSingleItem(LabelBean labelBean) {

        OptionItem item = new OptionItem(this);
        item.setOrientation(LinearLayout.HORIZONTAL);
        item.setBackgroundColor(Color.WHITE);
        item.setOptionIcon(R.drawable.sharer_red_circle)
                .setOptionTitle(labelBean.getLabelName())
                .setDrawablePadding(10)
                .setIvArrowDimension(18)
                .setIvArrowMarginLeft(10)
                .setTextSize(14);

        String[] options = processDummyResultFormServer(labelBean.getLabelOption());
        Log.d(TAG, "addSingleItem: opitons===>" + options);

        item.setTag(R.id.TAG_OPTIONS, options);//1.给它一个String[]变量的引用
        item.setTag(R.id.TAG_LABEL_BEAN, labelBean);//2.再给它一个对应显示的labelBean,方便提交时取数据

        if(goodsSNID ==null){//说明是新增页面,直接选中第一个
            item.setContent(options[0]);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int density = (int) getResources().getDisplayMetrics().density;
        params.topMargin = density;
        item.setPadding(20 * density, 10 * density, 20 * density, 10 * density);
        optionContainerBottom.addView(item, params);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showChoiceDialog(v);

            }
        });
    }



    /**
     * 处理服务器返回的选项,加工成String[]后输出
     */
    private String[] processDummyResultFormServer(String labelOption) {

        String[] split = labelOption.split(",");
//        for (int i = 0; i < split.length; i++) {
//            split[i] = truncate(split[i]);
//        }
        return split;
    }



    /**
     * 点击下面动态添加的选项后,弹出单选对话框
     */
    private void showChoiceDialog(final View view) {

        final String[] choices = (String[]) view.getTag(R.id.TAG_OPTIONS);
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        OptionItem item = (OptionItem) view;

                        item.setContent(choices[which]);
                        dialog.dismiss();
                    }
                })
                .show();

    }



    @Override
    public void onClick(View v) {

        super.onClick(v);
        switch (v.getId()) {
            case R.id.option_goods_name:
                showInputDialog(GOODS_NAME);
                break;
            case R.id.option_goods_type://弹出单选对话框
                List<TypeBean> typeBeans = (List<TypeBean>) option_goods_type.getTag(R.id.TAG_TYPE_LIST);
                if (typeBeans != null && typeBeans.size() > 0) {
                    showGoodsTypeDialog(typeBeans);
                }
                break;
            case R.id.option_buisness_type://弹出模式对话框
                showBusinessTypeDialog();// TODO: 2017/8/28 是否需要此信息?
                break;
            case R.id.option_charge:
                showInputDialog(PRICE_INPUT);
                break;
            case R.id.option_deposit:
                showInputDialog(DEPOSIT_INPUT);
                break;
            case R.id.option_latlng://开启新页面,地图上选取经纬度
                Intent intent = new Intent(this, ChooseLatLngActivity.class);
                intent.putExtra(Constants.CurrentPosition, selectedPosition);
                startActivityForResult(intent, Option_Pick_LatLng);
                overridePendingTransition(R.anim.global_in, 0);
                break;
            case R.id.btn_submit:
                submit();
                break;
            case R.id.btn_delete:
                new AlertDialog.Builder(this)
                        .setTitle("真的要删除当前物品吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                deleteCurrentItem();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        })
                        .show();
                break;

        }
    }



    /**
     * 删除当前正在编辑页面展示的商品,根据SNID来删除
     */
    private void deleteCurrentItem() {

        if (httpUtil == null)
            httpUtil = new HttpUtil();

        UserDao userDao = UserDao.getDaoInstance(this);
        LoginResultBean currentUser = userDao.query();
        String phoneNumber = currentUser.getPhoneNumber();
        String[] params = new String[]{Constants.AppID,GlobalConfig.AppID,Constants.ECID, GlobalConfig.ECID, Constants.PhoneNumber, phoneNumber,
                Constants.GoodsSNID, goodsSNID};

        httpUtil.callJson(handler, DELETE_SELECTED_ITEM, this,
                GlobalConfig.SHARER_SERVER_ROOT + GlobalConfig.SHARER_DELETE_CURRENT_PRODUCT,
                BaseResultBean.class, params);

    }



    /**
     * 弹出业务模式选择框Dialog.
     */
    private void showBusinessTypeDialog() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, GlobalConfig.BUSINESS_TYPE);
        new AlertDialog.Builder(this)
                .setCancelable(true)
//                .setSingleChoiceItems(GlobalConfig.BUSINESS_TYPE, -1, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
////                        opiton_business_type.setContent(GlobalConfig.BUSINESS_TYPE[which]);
////                        changeOptionItemStatus(which);//根据选择结果决定是否显示押金选项条目
////                        dialog.dismiss();
//                    }
//                })
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        opiton_business_type.setContent(GlobalConfig.BUSINESS_TYPE[which]);
                        changeOptionItemStatus(which);//根据选择结果决定是否显示押金选项条目
                        dialog.dismiss();
                    }
                })
                .show();

    }



    /**
     * 根据选择结果决定是否显示押金选项条目
     */
    private void changeOptionItemStatus(int which) {

        option_deposit.setVisibility(GlobalConfig.BUSINESS_TYPE[which].equals("押金使用") ? View.VISIBLE : View.GONE);
    }



    /**
     * 提交所有信息
     */
    private void submit() {

        //按顺序进行各种非空判断
        //1.图片至少有三张

        //2.物品类型及下面的各个选项必须都得有内容
        //是否达到了提交的条件了?

        LogUtil.debug(String.valueOf(R.id.TAG_LABEL_BEAN));

        if (!isReadyToSubmit()) {
            return;
        }

        ToastUtil.toast_debug("success");
        //下面拼接参数,发送请求
        // new BitmapEncodeTask(this).execute(adapter.data);
        //开启后台服务
        TypeBean typeBean = (TypeBean) option_goods_type.getTag(R.id.TAG_TYPE);//// TODO: 2017/9/2 特别注意,咱们往View上添加了多个tags,必须用R.id.xxx来获取!
        String labelContent = (assembleLabelContent());
        Log.e(TAG, "submit: labelContent------>"+ labelContent);

        Intent intent = new Intent(this, UploadImgService.class);

        intent.putExtra(Constants.GoodsName, option_goods_name.getContent());
        intent.putStringArrayListExtra(Constants.PicList, adapter.data);
        intent.putExtra("LatLng", selectedPosition);
        intent.putExtra(Constants.GoodsSNID, goodsSNID == null ? "" : goodsSNID);
        intent.putExtra(Constants.INTRODUCTION, et_intro.getText().toString().trim());
        intent.putExtra(Constants.BUSSINESS_TYPE, opiton_business_type.getContent().equals(GlobalConfig.BUSINESS_TYPE[0]) ? "1" : "2");
        intent.putExtra(Constants.USAGE_PRICE, option_goods_charge.getContent().replace(GlobalConfig.PriceType, ""));
        intent.putExtra(Constants.GOODS_DEPOSIT, option_deposit.getContent().replace("元", ""));
        intent.putExtra(Constants.GOODSTYPEID, typeBean.getGoodsTypeID());
        intent.putExtra(Constants.LABEL_CONTENT, labelContent);
        startService(intent);
        ToastUtil.toast("正在后台提交信息...");
        finish();
    }



    /**
     * 拼接参数
     */
    private String[] processRequestParams(String picList) {

        String labelContent = (goodsSNID == null ? assembleLabelContent() : "");
        Log.d(TAG, "processRequestParams: labelContent==>>>" + labelContent);

        TypeBean typeBean = (TypeBean) option_goods_type.getTag(R.id.TAG_TYPE);//// TODO: 2017/9/2 特别注意,咱们往View上添加了多个tags,必须用R.id.xxx来获取!

        UserDao userDao = UserDao.getDaoInstance(this);

        LoginResultBean currentUser = userDao.query();
        if (currentUser == null) {
            ToastUtil.toast("当前没有登陆用户");
            return null;
        }

        String phoneNumber = currentUser.getPhoneNumber();

        return new String[]{Constants.ECID, GlobalConfig.ECID,
                Constants.PhoneNumber, phoneNumber,
                Constants.GoodsSNID, goodsSNID == null ? "" : goodsSNID,
                Constants.PicList, picList,
                Constants.INTRODUCTION, et_intro.getText().toString().trim(),
                Constants.BUSSINESS_TYPE, opiton_business_type.getContent().equals(GlobalConfig.BUSINESS_TYPE[0]) ? "1" : "2",
                Constants.USAGE_PRICE, option_goods_charge.getContent().replace(GlobalConfig.PriceType, ""),
                Constants.GOODS_DEPOSIT, option_deposit.getContent().replace("元", ""),
                Constants.GOODSTYPEID, typeBean.getGoodsTypeID(),
                Constants.LABEL_CONTENT, labelContent};//// TODO: 2017/9/2 定金这一项给漏掉了!


    }



    /**
     * 组装标签内容,Server需要的内容冗余
     */
    private String assembleLabelContent() {

        StringBuilder sb = new StringBuilder();

        int childCount = optionContainerBottom.getChildCount();

        Log.d(TAG, "assembleLabelContent: childcount--->" + childCount);

        for (int i = 0; i < childCount; i++) {
            OptionItem item = (OptionItem) optionContainerBottom.getChildAt(i);
            LabelBean labelBean = (LabelBean) item.getTag(R.id.TAG_LABEL_BEAN);
            sb
//                    .append(labelBean.getGoodsTypeLabelID()).append(",")
                    .append(labelBean.getLabelName()).append(",")
                    .append(item.getContent())
                    .append(";");
        }

        return sb.toString();
    }



    /**
     * 检查是否满足了提交新物品的条件
     */
    private boolean isReadyToSubmit() {

        if (option_goods_name.getContent() == null || option_goods_name.getContent() == "") {
            ToastUtil.toast("物品名称不能为空");
            return false;
        }

        if (rv.getAdapter().getItemCount() < 4) {//图片数据小于3张
            ToastUtil.toast("图片数量不应小于三张");
            return false;
        }

        for (int i = 0; i < optionContainerTop.getChildCount(); i++) {
            OptionItem item = (OptionItem) optionContainerTop.getChildAt(i);
            if (item.getVisibility() == View.GONE)
                continue;
            if (TextUtils.isEmpty(item.getContent())) {
                ToastUtil.toast(item.getOptionTitle() + "不能为空");
                return false;
            }
        }

        for (int i = 0; i < optionContainerBottom.getChildCount(); i++) {
            OptionItem item = (OptionItem) optionContainerBottom.getChildAt(i);
            if (item.getVisibility() == View.GONE)
                continue;
            if (TextUtils.isEmpty(item.getContent())) {
                ToastUtil.toast(item.getOptionTitle() + "不能为空");
                return false;
            }
        }

        if (selectedPosition == null || selectedPosition.latitude < 0 || selectedPosition.longitude < 0) {//位置为空,不能提交
            ToastUtil.toast("物品位置不能为空");
            return false;
        }

        return true;
    }



    /**
     * 显示输入使用单价的Dialog
     */
    private void showInputDialog(final int type) {

        View inflated = LayoutInflater.from(this).inflate(R.layout.sharer_input_dialog_layout, null);
        EditText et_name = (EditText) inflated.findViewById(R.id.et_input);
        if (type == GOODS_NAME) {
            et_name.setInputType(InputType.TYPE_CLASS_TEXT);
        }else if(type == PRICE_INPUT || type ==DEPOSIT_INPUT){
            et_name.setInputType(EditorInfo.TYPE_CLASS_NUMBER|EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        }

        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setView(inflated)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AlertDialog inputDialog = (AlertDialog) dialog;
                        EditText et_usagePrice = (EditText) inputDialog.findViewById(R.id.et_input);
                        String input = et_usagePrice.getText().toString().trim();

                        if (!TextUtils.isEmpty(input)) {
                            if (type == PRICE_INPUT) {
                                option_goods_charge.setContent(input + GlobalConfig.PriceType);
                            } else if (type == DEPOSIT_INPUT) {
                                option_deposit.setContent(input + "元");
                            } else if (type == GOODS_NAME) {
                                option_goods_name.setContent(input);
                            }
                        }

                        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        if(manager != null){
                            manager.hideSoftInputFromWindow(et_intro.getWindowToken(),0);
                        }
                        et_intro.clearFocus();
                        ((LinearLayout)et_intro.getParent()).requestFocus();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ToastUtil.toast_debug("dialog cancel!");
                        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        if(manager != null){
                            manager.hideSoftInputFromWindow(et_intro.getWindowToken(),0);
                        }
                        ((LinearLayout)et_intro.getParent()).requestFocus();

                    }
                })
                .show();

    }



    /**
     * 显示选择物品类型的Dialog
     */
    private void showGoodsTypeDialog(final List<TypeBean> typeBeans) {

        final String[] items = new String[typeBeans.size()];
        for (int i = 0; i < typeBeans.size(); i++) {
            items[i] = typeBeans.get(i).getGoodsTypeName();
        }

        new AlertDialog.Builder(this).setCancelable(true)
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //// TODO: 2017/8/28  ...
                        option_goods_type.setTag(R.id.TAG_TYPE, typeBeans.get(which));//让此条目对象指向与它关联的另外一个类型对象,方便提交时读取数据
                        option_goods_type.setContent(items[which]);
                        //并且删除标签容器中所有标签条目,并添加当前类型所对应的所有标签条目
                        optionContainerBottom.removeAllViews();
                        TypeBean typeBean = (TypeBean) option_goods_type.getTag(R.id.TAG_TYPE);
                        List<LabelBean> labelBeans = (List<LabelBean>) option_goods_type.getTag(R.id.TAG_LABEL_LIST);
                        String typeID = typeBean.getGoodsTypeID();
                        if (labelBeans != null && labelBeans.size() > 0) {
                            List<LabelBean> list = new ArrayList<>();
                            for (int i = 0; i < labelBeans.size(); i++) {
                                LabelBean label = labelBeans.get(i);
                                if (label.getGoodsTypeID() != null
                                        && label.getGoodsTypeID().equals(typeID)) {//这两个ID相等是判断的关键,你要注意!
                                    list.add(labelBeans.get(i));
                                }
                            }
                            addLabelOptions(list);//更新标签容器中的所有条目
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }



    /**
     * 把服务器返回的这种格式的结果进行截取
     * 1七成新----->七成新
     */

    public String truncate(String segment) {

        return segment.substring(1);
    }


    private boolean checkPermission() {

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ||(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED)){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                PermissionUtil.showRationale(this,"读写存储卡权限不足");
            }else if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)){
                PermissionUtil.showRationale(this,"相机权限不足");
            }else {//如果没有权限,并且用户之前选择了不再询问,此处就直接申请权限!
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                        SDCARD_CAMERA_PERMISSION);
            }
            return false;
        } else {
            return true;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ToastUtil.toast_debug("onRequestPermissionResult()被调用了!");
        if (requestCode == SDCARD_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                chooseLocalPics();
            } else {
                checkPermission();
            }
        }
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

//        Log.d(TAG, "dispatchTouchEvent: x--->"+ev.getX()+",y--->"+ev.getRawY());
//        //判断此点是否在et_intro控件所在区域中
//        if(!isTouchPointInView(ev.getX(),ev.getY())){
//            ((ViewGroup)et_intro.getParent()).requestFocus();
//        }
//
//        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        manager.hideSoftInputFromWindow(et_intro.getWindowToken(),0);
        return super.dispatchTouchEvent(ev);
    }



    private boolean isTouchPointInView(float x, float y) {

        boolean isInView = false;
        //left of et_intro
        Log.d(TAG, "isTouchPointInView: left is:"+et_intro.getX()+",top is:"+et_intro.getY());
        int[] rect = new int[2];
        et_intro.getLocationOnScreen(rect);//left & top
        int width = et_intro.getWidth();
        int height = et_intro.getHeight();
        if((x >= rect[0]  && x <= rect[0]+width) && ((y >=rect[1]) && y<rect[1]+height) && et_intro.isFocused()){
            isInView = true;
        }
        return isInView;
    }



    @Override
    protected void onStart() {

        super.onStart();

        int adjustedHeight = (int) (GlobalConfig.display_width / 4 - getResources().getDisplayMetrics().density * 2 * 2);
        int height = (int) (adjustedHeight * 3 + getResources().getDisplayMetrics().density * 3 * 4);
        Log.d("tag", "onStart: height =  ==>" + height);
        rv.getLayoutParams().height = height;
        rv.hasFixedSize();
    }



    private void invokeSystemAlbum() {

        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        // whether show camera
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
        // max select image amount
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // default select images (support array list)
        intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, adapter.data);
        startActivityForResult(intent, REQUEST_IMAGE);

    }



    private void invokeSystemCamera() {

        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = Environment.getExternalStorageDirectory();
//        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);//可以考虑替换为getExternalFileDir();

        long currentMillis = SystemClock.uptimeMillis();

        File dest = new File(dir, String.valueOf(currentMillis) + ".jpg");
        uri = Uri.fromFile(dest);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 0);
    }



    //接收:1.选择图片的URLs 2.地图上选择物品的位置LatLng
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Log.d("tag", "onActivityResult: uri======>" + uri);
            adapter.data.add(uri.toString());
            adapter.notifyDataSetChanged();
        } else if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
            if (results != null) {
                adapter.data = results;
                adapter.notifyDataSetChanged();
            } else {
                ToastUtil.toast_debug("results===null");
            }
            Log.d("tag", "onActivityResult: uri====>>>" + uri);
//            ((ImageView) findViewById(R.id.iv)).setImageURI(uri);
        } else if (requestCode == Option_Pick_LatLng && resultCode == RESULT_OK) {
            LatLng latLng = data.getParcelableExtra(Constants.LatLng);
            if (latLng != null) {
                //界面提示选取成功并保存位置信息
                option_choose_position.setContent("位置选取成功");
                selectedPosition = latLng;
            } else {
                option_choose_position.setContent("位置选择失败");
            }
        }
    }



    /**
     * 弹出选择照片出处的Dialog
     */
    private void chooseLocalPics() {

        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setSingleChoiceItems(new String[]{"拍摄", "从相册选择"}, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {//调用相机拍摄
                            invokeSystemCamera();
                        } else if (which == 1) {//从相册中选择,支持多选,最多9张,多了没有!
                            invokeSystemAlbum();
                        }
                        dialog.dismiss();
                    }
                })
                .show();

    }



    @Override
    public void setOnLeftClick() {

        finish();
    }



    @Override
    public void setOnRightClick() {

    }



    @Override
    public void onStop() {

        super.onStop();
    }



    @Override
    public void onPostExecute(String encodedString) {

        String[] params = processRequestParams(encodedString);
        if (params == null) {
            return;
        }
        if (httpUtil == null)
            httpUtil = new HttpUtil();
        httpUtil.callJson(handler, RETRIEVE_SUBMIT_NEW_ITEM, this,
                GlobalConfig.SHARER_SERVER_ROOT + (goodsSNID == null ? GlobalConfig.SUBMIT_NEW_ITEM : GlobalConfig.SHARER_MODIFY),
                BaseResultBean.class, params);

        ToastUtil.toast("正在后台提交...");
        finish();

    }



    public class RvAdapter extends MyItemTouchHandler.ItemTouchAdapterImpl {

        private static final int TYPE_CAMERA_ICON = 0;

        private static final int TYPE_UPLOAD_ICON = 1;

        private static final int MAX_UPLOAD_COUNT = 9;//最大上传数量

        private ArrayList<String> data = new ArrayList<>();



        public RvAdapter() {

        }



        @Override
        public void onItemMove(int fromPosition, int toPosition) {


        }



        @Override
        public void onItemRemove(int position) {
//            data.remove(position);
//            notifyItemRemoved(position);
        }



        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sharer_drag_new_item_layout, parent, false);
            int adjustedHeight = (int) (GlobalConfig.display_width / 4 - getResources().getDisplayMetrics().density * 2 * 2);
            Log.d("tag", "onCreateViewHolder: adjustedHeight = ==>" + adjustedHeight + "display_width===>" + GlobalConfig.display_width);
            itemView.getLayoutParams().width = adjustedHeight;
            itemView.getLayoutParams().height = adjustedHeight;

            RecyclerView.ViewHolder vh;
            if (viewType == TYPE_CAMERA_ICON) {
                vh = new CameraViewHolder(itemView);
            } else {
                vh = new MyViewHolder(itemView);
            }

            return vh;
        }



        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            int viewType = getItemViewType(position);

            if (viewType == TYPE_CAMERA_ICON) {
                holder.itemView.setBackgroundResource(R.drawable.sharer_camera_icon);
                if (data.size() == MAX_UPLOAD_COUNT)
                    holder.itemView.setVisibility(View.INVISIBLE);
                else
                    holder.itemView.setVisibility(View.VISIBLE);
            } else {
                Glide.with(NewItemActivity.this).load(data.get(position)).into((ImageView) holder.itemView.findViewById(R.id.iv));

                //// TODO: 2017/9/2  
            }

            holder.itemView.findViewById(R.id.delete).setTag(position);
            if (position == data.size())
                holder.itemView.findViewById(R.id.delete).setVisibility(View.GONE);
            else
                holder.itemView.findViewById(R.id.delete).setVisibility(View.VISIBLE);

            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN
                            && position != data.size()) {
                        itemTouchHelper.startDrag(holder);
                    }
                    return false;
                }
            });

        }



        @Override
        public int getItemCount() {

            return data.size() + 1;
        }



        @Override
        public int getItemViewType(int position) {

            return position == data.size() ? TYPE_CAMERA_ICON : TYPE_UPLOAD_ICON;
        }



        public class CameraViewHolder extends RecyclerView.ViewHolder {

            public CameraViewHolder(View itemView) {

                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //选择相册或者相机拍摄
                        if (checkPermission()) {
                            chooseLocalPics();
                        }
                    }
                });

            }
        }


        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView tv;



            public MyViewHolder(View itemView) {

                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.text);
                itemView.findViewById(R.id.delete).setOnClickListener(this);
            }



            @Override
            public void onClick(View v) {

                int position = (int) v.getTag();
//                onItemRemove(position);
                data.remove(position);
                notifyDataSetChanged();


                for (int i = 0; i < data.size(); i++) {
                    Log.d("tag", "onClick: data==>" + data.get(i));
                }
            }
        }
    }
}
