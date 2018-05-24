package com.hbbc.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hbbc.R;

/**
 * 屏幕显示属性工具类
 */
public class UIUtil {

    /**
     * 根据Touch点击，为View的前景图像（ImageView, ImageButton）或
     * 背景图像（Button）添加“发光”滤镜的方式，让界面展现出被选中的状态。 当TouchUp的时候，则自动去除滤镜。
     *
     * @param v     View
     * @param event MotionEvent
     * @return 当ImageView时，TouchDown的时候返回True（否则，将无法触发ImageView的TouchUp），其他情况均返回False
     */
    public static boolean drawViewTouchShow(View v, MotionEvent event) {

        // 要是处理的精细的话，点击图片的整体颜色偏暗的，此参数应高一些。亮色的，则需要低一些。一般在1.1到1.3比较合适
        float strength = 1.16f;

        // 若是按钮方式被Touch应更改其背景图片的显示状态
        if (v.getClass() == android.widget.Button.class) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float[] BT_SELECTED = new float[]{strength, 0, 0, 0, 0, 0,
                        strength, 0, 0, 0, 0, 0, strength, 0, 0, 0, 0, 0,
                        strength, 0};
                v.getBackground().setColorFilter(
                        new ColorMatrixColorFilter(BT_SELECTED));
            } else {
                v.getBackground().clearColorFilter();
            }
            return false;
        }
        //若是图片视图方式被Touch应更改其前景图片的显示状态//// TODO: 2017/9/1 只能是前景加colorFilter吗?
        if (v.getClass() == android.widget.ImageView.class
                || v.getClass() == android.widget.ImageButton.class) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float[] BT_SELECTED = new float[]{strength, 0, 0, 0, 0, 0,
                        strength, 0, 0, 0, 0, 0, strength, 0, 0, 0, 0, 0,
                        strength, 0};
                ((ImageView) v).setColorFilter(new ColorMatrixColorFilter(
                        BT_SELECTED));
                return true; // 此时需要返回true，否则，将无法获得onTouchOn的事件
            } else {
                ((ImageView) v).clearColorFilter();
            }
            return false;
        }

        //若是图片视图方式被Touch应更改其前景图片的显示状态//// TODO: 2017/9/1 只能是前景加colorFilter吗?
        if (v.getClass() == android.widget.LinearLayout.class) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float[] BT_SELECTED = new float[]{strength, 0, 0, 0, 0, 0,
                        strength, 0, 0, 0, 0, 0, strength, 0, 0, 0, 0, 0,
                        strength, 0};
                Drawable bg = ((LinearLayout) v).getBackground();
                if(bg!=null){
                    bg.setColorFilter(new ColorMatrixColorFilter(
                            BT_SELECTED));
                    return true; // 此时需要返回true，否则，将无法获得onTouchOn的事件
                }else{
                    return false;

                }
            } else {
                ((ImageView) v).clearColorFilter();
            }
            return false;
        }

        return false;

    }



    /**
     * 退出APP eg:UIUtil.exitApp(this, true);
     */
    public static void exitApp(final Activity activity, boolean confirmFlag) {
        if (confirmFlag) {
            String appName = activity.getResources().getString(
                    R.string.app_name);
            // 若需要用户确认的，提示用户确认后退出
            AlertDialog alertDialog = new AlertDialog.Builder(activity)
                    .setTitle("" + appName)
                    .setMessage("是否退出程序")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    // 用户确认退出程序，直接退出
                                    Intent intent = new Intent(
                                            Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    activity.startActivity(intent);
                                    android.os.Process
                                            .killProcess(android.os.Process
                                                    .myPid());
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    return;
                                }
                            }).create(); // 创建对话框
            alertDialog.show(); // 显示对话框
        } else {
            // 不用确认的，直接退出
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }



    /**
     * 设置全屏显示（去除标题栏）。注意，必须在setContentView方法执行前执行！
     * eg:UIUtil.setFullScreenShow(this);  界面对象句柄 cancal 是否全屏
     */
    public static void setFullScreenShow(Activity activity, boolean cancal) {
        // 使屏幕不显示标题栏(必须要在setContentView方法执行前执行)
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏，使内容全屏显示(必须要在setContentView方法执行前执行)
        if (cancal)
            activity.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
    }



    /**
     * 获取屏幕宽度
     * eg：int width=UIUtil.getWidthPixels(context);
     */
    public static int getWidthPixels(Context context) {
        return getPixels(context).widthPixels;
    }



    /**
     * 获取屏幕高度
     * eg：int width=UIUtil.getHeightPixels(context);
     */
    public static int getHeightPixels(Context context) {
        return getPixels(context).heightPixels;
    }



    /**
     * 获取宽度与高度,数组第一个数为宽度，第二个数为高度
     * eg：int[] width=UIUtil.getWidthPixels(context);
     */
    public static int[] getWidthHeightPixels(Context context) {
        int[] in = new int[2];
        DisplayMetrics metrics = getPixels(context);
        in[0] = metrics.widthPixels;
        in[1] = metrics.heightPixels;
        return in;
    }



    /**
     * 获取DisplayMetrice，eg:UIUtil.getPixels(this);
     */
    public static DisplayMetrics getPixels(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((BaseActivity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric;
    }
}
