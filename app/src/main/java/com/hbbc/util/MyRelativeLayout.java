package com.hbbc.util;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hbbc.R;

/**
 * Created by Administrator on 2017/7/7.
 * 实现思路: 单纯采用 '外部拦截法' 来控制PopupWindow.
 * 并且,对外提供一个开关,以便使用户注册后,button不再显示在PopupView中.
 * TODO 肯定也是可以单纯用内部拦截法来实现的!
 */
public class MyRelativeLayout extends RelativeLayout {

    private static final String TAG = "MyRelativeLayout";

    public OnSignUpClickListener listener;

    //在此方法中真正去响应拖动操作
    //此helper能否处理第二种拖动情况呢?即直接从MOVE事件开始响应拦截
    //
    int preY;

    private ViewDragHelper helper;

    private View popup_view;

    private Button btn_sign_up;

    private MyLinearLayout ll_bottom;

    private VelocityTracker velocityTracker;

    private OnViewPositionChangedListener positionChangedListener;

    private OnPopupWindowGoneListener onPopupWindowGoneListener;

    private ViewDragHelper.Callback cb = new ViewDragHelper.Callback() {

        //为什么此方法不执行?
        //它是什么时候被调用的? 是在shouldInterceptTouchEvent()方法之中,还是此方法拦截事件之后呢?
        //TODO 终于知道此判断条件为什么没有执行了! 原因就在于是从MOVE开始让Helper来判断的,而非DOWN开始!!!
        @Override
        public boolean tryCaptureView(View child, int pointerId) {

            if (child == popup_view
                    && child.getVisibility() != GONE) {
                return true;
            } else {
                return false;
            }
        }



        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
//            invalidate();穿透
//            capturedChild.invalidate();穿透更严重
            Log.d(TAG, "onViewCaptured: captured" + capturedChild.getClass().getSimpleName());
        }



        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

            //暴露出一个让地图伴随滑动的接口
            if (positionChangedListener != null) {
                positionChangedListener.onViewPositionChanged(-dy / 4);
            }
        }



        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //动态获取view的高度值

            if (top > 0) {
                top = 0;
            } else if (top < -child.getMeasuredHeight()) {
                top = -child.getMeasuredHeight();
            }
            return top;
        }



        @Override
        public int getViewVerticalDragRange(View child) {

            return 1;//1 is fine!
        }



        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {


            if (hidePopupWindow())//如果松手时,popup_view刚好移动到最高点时,就直接返回,不执行下面的滑动
                return;

            Log.d(TAG, "onViewReleased: releaseView's yvel===" + yvel);
            if (releasedChild != popup_view) {
                return;
            }
            //判断瞬时速率,以后可以调节此灵敏度,注意滑动方向
            if (yvel < -1500) {//向上滑动
                helper.smoothSlideViewTo(releasedChild, 0, -releasedChild.getMeasuredHeight());
                ViewCompat.postInvalidateOnAnimation(MyRelativeLayout.this);
                return;
            }

            int top = releasedChild.getTop();
            int divider_height = (int) (releasedChild.getMeasuredHeight() * 0.55);
            if (top > -divider_height) {
                helper.smoothSlideViewTo(releasedChild, 0, 0);
            } else {
                helper.smoothSlideViewTo(releasedChild, 0, -releasedChild.getMeasuredHeight());
            }

//            invalidate();
            popup_view.invalidate();//只重绘ViewGroup中的一个指定控件,这样是否可以呢?
        }

    };



    public MyRelativeLayout(Context context) {

        this(context, null);
    }



    public MyRelativeLayout(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }



    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

    }



    private boolean hidePopupWindow() {

        //向上弹性滑动的动画结束之后,把此view设置为GONE状态
        if (popup_view.getTop() == -popup_view.getMeasuredHeight()) {
            popup_view.setVisibility(GONE);
            //暴露接口,在窗口消失时,路径也跟着消失
            if(onPopupWindowGoneListener!=null)
            onPopupWindowGoneListener.onPopupWindowGone();
            return true;
        }
        return false;
    }



    public void setOnViewPositionChangedListener(OnViewPositionChangedListener listener) {

        positionChangedListener = listener;
    }



    public void setOnSignUpClickListener(OnSignUpClickListener listener) {

        this.listener = listener;
        btn_sign_up.setOnClickListener(this.listener);
    }



    /**
     * 实现弹性滑动
     * //为什么这个方法会一直被调用呢???*-*
     */
    @Override
    public void computeScroll() {
//        popup_view.setEnabled(false);//弹性滑动过程不接收任何操作
        if (helper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
//            popup_view.postInvalidate();
//            invalidate();
//            popup_view.invalidate();

        }/* else {
            //向上弹性滑动的动画结束之后,如果看不到此view后,把此view设置为GONE状态
            //Caution: 这个方法不能写在这!!!因为ComputeScroll()会在onDraw()方法中不断调用的!!!
            if (popup_view.getVisibility() != GONE)
                hidePopupWindow();
        }*/
    }



    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
        helper = ViewDragHelper.create(this, 2, cb);
        popup_view = findViewById(R.id.popup_window);
        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);

        ll_bottom = (MyLinearLayout) findViewById(R.id.ll_bottom);
        if (ll_bottom == null)//用来适配车主端,因为车主端popup_window中没有底部的 button.
            return;
        ll_bottom.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //转交给子view : button来处理事件
                //并且,不管button是否真的处理了此event,都返回true,不给后面的子view---MapView任何得到的机会!!!
                btn_sign_up.onTouchEvent(event);
                return true;
            }
        });
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

    }



    /**
     * 只在此方法中判断何时拦截,真正响应拦截的是下面onTouchEvent():分工明确!!!
     * 默认不拦截事件,我们要主动拦截,进行过滤
     * 不能把所有事件全部拦截了,只拦截自己需要的事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        //如果popup_view的状态为GONE时,就不要拦截任何事件了
     /*   if (popup_view.getVisibility() == GONE) {
            return false;
        }*/

        int action = ev.getAction();
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        //Caution: 此处必须用Button所在的线性布局来进行最后一个判断,直接用button来判断是不行的!
        //以后,如果改动,一定要慎重!!!
        if (action == MotionEvent.ACTION_DOWN && helper.isViewUnder(popup_view, x, y)
                && (!helper.isViewUnder(ll_bottom, x, y))) {
            return true;
        }

        return helper.shouldInterceptTouchEvent(ev);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        helper.processTouchEvent(event);
        return true;

    }

    public void setonPopupWindowGoneListener(OnPopupWindowGoneListener listener) {

        if (listener != null)
            onPopupWindowGoneListener = listener;
    }

    //暴露一个接口,用来设置ll_bottom的显示状态
    public void changeBottomButtonStatus(boolean shouldButtonGoneAway) {

        if (ll_bottom != null)
            ll_bottom.setVisibility(shouldButtonGoneAway ? GONE : VISIBLE);
    }

    public interface OnSignUpClickListener extends OnClickListener {

    }

    //定义一个:伴随滑动的接口
    public interface OnViewPositionChangedListener {
        void onViewPositionChanged(int dy);
    }


    //窗口消失,取消规划路径显示的接口
    public interface OnPopupWindowGoneListener {
        void onPopupWindowGone();
    }


}