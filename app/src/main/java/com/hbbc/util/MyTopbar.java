package com.hbbc.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbbc.R;

/**
 * TopBar控件
 */
public class MyTopbar extends RelativeLayout {

    private ImageView left_ImgView;

    private TextView titleView;

    private ImageView right_ImgView;

    private int right_isvisible;        //右边图片是否显示

    private int left_isvisible;         //左边图片是否显示

    private Drawable left_img;

    private Drawable right_img;

    private String title;

    private float title_size;

    private int title_color;

    private int background;

    private float left_img_width;

    private float left_img_height;

    private float right_img_width;

    private float right_img_height;

    private float topbar_marginright_rightimg;

    private float topbar_marginleft_leftimg;

    private OnTopBarClickListener onTopBarClickListener;



    public MyTopbar(Context context) {

        this(context, null);
    }



    public MyTopbar(Context context, AttributeSet attrs) {

        super(context, attrs);
        TypedArray taArray = context.obtainStyledAttributes(attrs,
                R.styleable.MyTopbar);

        left_ImgView = new ImageView(context);
        titleView = new TextView(context);
        right_ImgView = new ImageView(context);

        left_img = taArray.getDrawable(R.styleable.MyTopbar_topbar_left_img);
        left_img_width = taArray.getDimension(R.styleable.MyTopbar_topbar_left_img_width, getDp2Px(30));
        left_img_height = taArray.getDimension(R.styleable.MyTopbar_topbar_left_img_height, getDp2Px(30));
        topbar_marginleft_leftimg = taArray.getDimension(R.styleable.MyTopbar_topbar_marginleft_leftimg, 0);

        right_img = taArray.getDrawable(R.styleable.MyTopbar_topbar_right_img);
        right_img_width = taArray.getDimension(R.styleable.MyTopbar_topbar_right_img_width, getDp2Px(30));
        right_img_height = taArray.getDimension(R.styleable.MyTopbar_topbar_right_img_height, getDp2Px(30));
        topbar_marginright_rightimg = taArray.getDimension(R.styleable.MyTopbar_topbar_marginright_rightimg, 0);

        title = taArray.getString(R.styleable.MyTopbar_topbar_title);
        title_color = taArray.getColor(R.styleable.MyTopbar_topbar_title_color, Color.BLACK);
//        title_size = taArray.getDimension(R.styleable.MyTopbar_topbar_title_size, 0);
        int text_size = taArray.getInt(R.styleable.MyTopbar_topbar_title_size, 12);
        background = taArray.getColor(R.styleable.MyTopbar_topbar_background, Color.BLACK);

//        left_ImgView.setBackground(left_img);
//        left_img.setBounds(0,0,40,40);//// TODO: 2017/9/1 必须要加上吗

        left_ImgView.setImageDrawable(left_img);
        left_ImgView.setClickable(true);
        left_ImgView.setBackgroundResource(R.drawable.top_bar_selector);
//        right_ImgView.setBackground(right_img);
        right_ImgView.setImageDrawable(right_img);

        right_ImgView.setBackgroundResource(R.drawable.top_bar_selector);
        right_ImgView.setScaleType(ImageView.ScaleType.CENTER);
        titleView.setText(title);
        titleView.setTextColor(title_color);
//        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP,title_size);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, text_size);

        final RelativeLayout.LayoutParams leftimgParams = new RelativeLayout.LayoutParams((int) left_img_width, (int) left_img_height);
        left_isvisible = taArray.getInt(R.styleable.MyTopbar_topbar_left_visible, 0);
        leftimgParams.setMargins((int) topbar_marginleft_leftimg, 0, 0, 0);
        leftimgParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        leftimgParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        left_ImgView.setScaleType(ImageView.ScaleType.CENTER);


        RelativeLayout.LayoutParams rightimgParams = new RelativeLayout.LayoutParams((int) right_img_width, (int) right_img_height);
        right_isvisible = taArray.getInt(R.styleable.MyTopbar_topbar_right_visible, 0);
        rightimgParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        rightimgParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        rightimgParams.setMargins(0, 0, (int) topbar_marginright_rightimg, 0);


        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        addView(right_ImgView, rightimgParams);
        addView(left_ImgView, leftimgParams);
        switch (right_isvisible) {
            case 0:
                right_ImgView.setVisibility(View.VISIBLE);
                break;
            case 4:
                right_ImgView.setVisibility(View.INVISIBLE);
                break;
            case 8:
                right_ImgView.setVisibility(View.GONE);
                break;
        }

        switch (left_isvisible) {
            case 0:
                left_ImgView.setVisibility(View.VISIBLE);
                break;
            case 4:
                left_ImgView.setVisibility(View.INVISIBLE);
                break;
            case 8:
                left_ImgView.setVisibility(View.GONE);
                break;
        }

        addView(titleView, titleParams);


        setBackgroundColor(background);
        left_ImgView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                if (onTopBarClickListener != null) {
                    left_ImgView.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            onTopBarClickListener.setOnLeftClick();
                        }
                    }, 200);
                }
            }
        });

        right_ImgView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (onTopBarClickListener != null) {
                    right_ImgView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onTopBarClickListener.setOnRightClick();
                        }
                    }, 250);
                }
            }
        });
        if (right_img == null) {
            right_ImgView.setVisibility(GONE);
        }

        taArray.recycle();
    }



    public int getDp2Px(float dpi) {

        return (int) (dpi * getResources().getDisplayMetrics().densityDpi + 0.5f);
    }



    public int getSp2Px(float sp) {

        return (int) (sp * getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }



    public void setTitle(String title) {

        titleView.setText(title);
    }



    public void setLeft_img(Drawable drawable) {

        left_ImgView.setBackground(drawable);
    }



    public void setRight_img(Drawable drawable) {

        right_ImgView.setBackground(drawable);
    }



    public void setOnTopBarClickListener(OnTopBarClickListener onTopBarClickListener) {

        this.onTopBarClickListener = onTopBarClickListener;
    }



    /**
     * 左边图片是否显示
     */
    public void setLeftImageVisible(int visible) {

        left_ImgView.setVisibility(visible);
    }



    /**
     * 右边图片是否显示
     */
    public void setRightImageVisible(int visible) {

        right_ImgView.setVisibility(visible);
    }



    public void setLeftColorFilter(int color) {

        left_ImgView.setColorFilter(color);
    }



    public void setTitleColor(int color) {

        titleView.setTextColor(color);
    }



    public interface OnTopBarClickListener {

        void setOnLeftClick();

        void setOnRightClick();
    }
}
