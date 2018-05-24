package com.hbbc.mshare.sharer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbbc.R;
import com.hbbc.util.MyLinearLayout;

import java.util.Random;

/**
 * Created by Administrator on 2017/8/28.
 * 动态接口暴露与静态XML使用 同时对应提供!
 */
public class OptionItem extends MyLinearLayout {

    private int[] resIds = new int[]{R.drawable.sharer_blue_circle, R.drawable.sharer_blue_circle, R.drawable.sharer_orange_circle,
            R.drawable.sharer_red_circle, R.drawable.sharer__cyan_circle, R.drawable.sharer_pink_circle, R.drawable.sharer_light_blue_circle};

    private Drawable[] drawables = new Drawable[4];

    private String title;

    private int textSize;

    private String content;

    private TextView tv_title;

    private TextView tv_content;

//    private RadioButton rb;

    private ImageView iv_arrow;

    private int iv_width;



    public OptionItem(Context context) {

        this(context, null);
    }



    public OptionItem(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }



    public OptionItem(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OptionItem);
        //processing attrs values specified in XML
        Random random = new Random();
        int index = random.nextInt(resIds.length);
        int iconResId = a.getResourceId(R.styleable.OptionItem_option_icon, resIds[index]);
        tv_title = new TextView(context);
        Drawable drawable_left = getResources().getDrawable(iconResId);
        drawable_left.setBounds(0, 0, drawable_left.getMinimumWidth(), drawable_left.getMinimumHeight());
        drawables[0] = drawable_left;
        tv_title.setCompoundDrawables(drawable_left, null, null, null);

        title = a.getString(R.styleable.OptionItem_option_title);
        if (title != null) {
            tv_title.setText(title);
        }
        int drawablePadding = a.getInt(R.styleable.OptionItem_drawablePadding, 5);
        tv_title.setCompoundDrawablePadding((int) (getResources().getDisplayMetrics().density * drawablePadding));

        textSize = a.getInt(R.styleable.OptionItem_textSize, 14);

        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        addView(tv_title);//左侧textView添加完成
        //add placeHolder view
        View placeHolder = new View(context);
        LayoutParams params = new LayoutParams(0, 0, 1f);
        placeHolder.setLayoutParams(params);
        addView(placeHolder);
        //add content view
        tv_content = new TextView(context);
        content = a.getString(R.styleable.OptionItem_content);
        tv_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        tv_content.setText(content);
        addView(tv_content);

        //add radio button 之前的需求是用的RadioButton在条目的最右边.现在改成向右的箭头了
        //rb = new RadioButton(context);
        //Use an ImageView replacing the previous RadioButton;
        iv_arrow = new ImageView(getContext());
        iv_arrow.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv_arrow.setImageResource(R.drawable.mshare_system_message_blue_arrow);

        int iv_dimension = a.getInt(R.styleable.OptionItem_iv_arrow_dimension, 10);
        iv_width = (int) (getResources().getDisplayMetrics().density * iv_dimension);
        LinearLayout.LayoutParams iv_params = new LinearLayout.LayoutParams(iv_width, iv_width);

        int rightMargin = a.getInt(R.styleable.OptionItem_iv_arrow_margin_left, 10);
        iv_params.leftMargin = (int) (getResources().getDisplayMetrics().density * rightMargin);

        addView(iv_arrow,iv_params);

        boolean isVisible = a.getBoolean(R.styleable.OptionItem_iv_arrow_status, true);
        iv_arrow.setVisibility(isVisible ? VISIBLE : GONE);

//        int rb_dimension = a.getInt(R.styleable.OptionItem_radio_button_dimension, 10);
//        rb_width = (int) (getResources().getDisplayMetrics().density * rb_dimension);
//        LinearLayout.LayoutParams rb_params = new LinearLayout.LayoutParams(rb_width, rb_width);
//
//        int rightMargin = a.getInt(R.styleable.OptionItem_radio_button_margin_left, 10);
//        rb_params.leftMargin = (int) (getResources().getDisplayMetrics().density * rightMargin);
//
//        addView(rb, rb_params);
//        rb.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        rb.setBackgroundResource(R.drawable.sharer_radio_button_selector);
//        rb.setChecked(true);
//        boolean isVisible = a.getBoolean(R.styleable.OptionItem_radio_button_status, true);
//        rb.setVisibility(isVisible ? VISIBLE : GONE);

        a.recycle();
    }

    /**
     * <attr name="option_icon" format="reference"/>
     <attr name="option_title" format="string"/>

     <attr name="drawablePadding" format="integer"/>
     <attr name="textSize" format="integer"/>

     <attr name="content" format="string"/>
     <attr name="radio_button_margin_left" format="integer"/>

     <attr name="radio_button_dimension" format="integer"/>
     <attr name="radio_button_status" format="boolean"/>
     */
    /**
     * 设置选项前面的图标
     *
     * @param resId
     */
    public OptionItem setOptionIcon(int resId) {

        Drawable drawable_left = getResources().getDrawable(resId);
        drawable_left.setBounds(0, 0, drawable_left.getMinimumWidth(), drawable_left.getMinimumHeight());
        drawables[0] = drawable_left;
        return this;
    }

    /**
     * 设置textview的drawablePadding,单位为dp
     */
    public OptionItem setDrawablePadding(int padding){

        int drawablePadding = (int) (getResources().getDisplayMetrics().density * padding);
        tv_title.setCompoundDrawablePadding(drawablePadding);
        return this;
    }



    /**
     * 设置选项标题
     *
     * @param title
     */
    public OptionItem setOptionTitle(String title) {

        tv_title.setText(title);
        return this;
    }

    /**
     * 获取选项标题
     */
    public String getOptionTitle(){
        return tv_title.getText().toString().trim();
    }



    /**
     * 设置选项文字大小
     */
    public OptionItem setTextSize(int size) {

        textSize = size;
        return this;
    }



    /**
     * 设置选中的内容
     */
    public OptionItem setContent(String content) {

        tv_content.setText(content);
        return this;
    }

    /**
     * 获取正在显示的内容
     */
    public String getContent(){
        return tv_content.getText().toString().trim();
    }



    /**
     * 设置Radio Button的尺寸,高宽必须相同
     */
    public OptionItem setIvArrowDimension(int dimension) {

         iv_width= (int) (getResources().getDisplayMetrics().density * dimension);
         LinearLayout.LayoutParams params = (LayoutParams) iv_arrow.getLayoutParams();
        params.width=iv_width;
        params.height=iv_width;
        iv_arrow.setLayoutParams(params);
        return this;
    }



    /**
     * 设置RadioButotn的marginLeft
     */
    public OptionItem setIvArrowMarginLeft(int marginLeft) {

        int leftMargin = (int) (getResources().getDisplayMetrics().density * marginLeft);
        LinearLayout.LayoutParams params = (LayoutParams) iv_arrow.getLayoutParams();
        params.setMargins(leftMargin, 0, 0, 0);
        iv_arrow.setLayoutParams(params);
        return this;
    }

    /**
     * 设置RadioButton的显示状态
     */
    public OptionItem setIvArrowStatus(boolean status){
        iv_arrow.setVisibility(status?VISIBLE:GONE);
        return this;
    }


}
