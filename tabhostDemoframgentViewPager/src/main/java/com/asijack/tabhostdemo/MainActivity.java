package com.asijack.tabhostdemo;
import java.util.ArrayList;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends FragmentActivity{
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	private ImageView image;
	private int currIndex;//当前页卡编码
	private int bmpW;//横线图片宽度
	private int offset;//图片移动的偏移量
	//需要滑动的的页卡
	private TextView view1,view2,view3,view4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//初始化TextView
		InitTextView();
		//初始化游标（图片）
		InitImage();
		//初始化ViewPager
		InitViewPager();
	}
	public void InitTextView(){
		//每个fragment(view) 对应的 textview
		view1=(TextView) findViewById(R.id.tv_guid1);
		view2=(TextView) findViewById(R.id.tv_guid2);
		view3=(TextView) findViewById(R.id.tv_guid3);
		view4=(TextView) findViewById(R.id.tv_guid4);
		//设置监听
		view1.setOnClickListener(new TxListener(0));
		view2.setOnClickListener(new TxListener(1));
		view3.setOnClickListener(new TxListener(2));
		view4.setOnClickListener(new TxListener(3));
		
	}
	public class TxListener implements View.OnClickListener{
		private int index=0;
		
		public TxListener (int i){
			index=i;
		}
		@Override
		public void onClick(View view) {
			//修改ViewPager调用setCurrentItem时，滑屏的速度
			mPager.setCurrentItem(index);
		}
		
	}
	//初始化图片的位移像素
	public 	void InitImage(){
		image=(ImageView) findViewById(R.id.cursor);
		//只有加载了Bitmap对象的时候才能对图片进行查看修改,decodeResource获取了Bitmap对象
		bmpW=BitmapFactory.decodeResource(getResources(), R.drawable.cours).getWidth();
//		Android 可设置为随着窗口大小调整缩放比例，但即便如此，手机程序设计人员还是必须知道手机屏幕的边界，以避免缩放造成的布局变形问题。
//		手机的分辨率信息是手机的一项重要信息，很好的是，Android 已经提供DisplayMetircs 类可以很方便的获取分辨率。
//		getWindowManager().getDefaultDisplay().getMetrics;
//		构造函数DisplayMetrics 不需要传递任何参数；调用getWindowManager() 之后，会取得现有Activity 的Handle 
//      此时，getDefaultDisplay() 方法将取得的宽高维度存放于DisplayMetrics 对象中，而取得的宽高维度是以像素为单位(Pixel) 
//      “像素”所指的是“绝对像素”而非“相对像素”。
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		//获取设备的宽度
		int screenW=dm.widthPixels;
		offset=(screenW/4 -bmpW)/2;//4个item的偏移量
//		offset = (screenW-2*bmpW)/4;//2 个item 的偏移量
		
		//imageView设置平移，使下划线平移到初始位置(平移一个offset)
		Matrix matrix=new Matrix();
		//  Matrix的操作，总共分为translate（平移），rotate（旋转），scale（缩放）和skew（倾斜）四种
		//每一种变换在Android的API里都提供了set, post和pre三种操作方式,除了translate，其他三种操作都可以指定中心点。
		//  set是直接设置Matrix的值，每次set一次，整个Matrix的数组都会变掉。
		//post是后乘，当前的矩阵乘以参数给出的矩阵。可以连续多次使用post，来完成所需的整个变换。
		matrix.postTranslate(offset, 0);
		image.setImageMatrix(matrix);
	}
	//初始化ViewPager
	public void InitViewPager(){
		mPager=(ViewPager) findViewById(R.id.viewpager);
		fragmentList=new ArrayList<Fragment>();
		Fragment firstFragment=FragmentFactory.newInstance("this is first fragment");
		Fragment secondFragment=FragmentFactory.newInstance("this is second fragment");
		Fragment thirdFragment=FragmentFactory.newInstance("this is third fragment");
		Fragment fourthFragment=FragmentFactory.newInstance("this is fourth fragment");
		fragmentList.add(firstFragment);
		fragmentList.add(secondFragment);
		fragmentList.add(thirdFragment);
		fragmentList.add(fourthFragment);
		//给ViewPager设置适配器
		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
		//设置当前显示的标签为第一页
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChanceListener());
		
	}
	
	//view页面滑动的监听
	public class MyOnPageChanceListener implements OnPageChangeListener{
		
		//此方法是在状态改变的时候调用，arg0参数 有三种状态0,1,2 ==1时表示正在滑动，==2时表示滑动完毕，==0时表示什么都没做
		//当页面开始滑动的时候，三种状态的变化顺序为（1，2，0）
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}
		//当页面滑动的时候会调用此方法，在滑动停止之前，此方法会一直得到调用
		//arg0 当前页面，及你点击滑动的页面  arg1 当前页面便宜的百分比  arg2 当前页面偏移的像素位置
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		//此方法是页面跳转完后 得到调用 arg0 是你当前选中的页面的position（位置编号）
		@Override
		public void onPageSelected(int arg0) {
			int one=offset*2+bmpW;//两个相邻页面的偏移量 
			Animation animation=new TranslateAnimation(currIndex*one, arg0*one,0,0);//平移动画
			currIndex=arg0;
			animation.setFillAfter(true);//动画中止时停留在最后一帧，不然会回到没有执行前的状态
			animation.setDuration(200);//动画持续时间0.2秒 
			image.startAnimation(animation);
			int i=currIndex +1;
			Toast.makeText(MainActivity.this, "您选择了第"+i+"个页卡", Toast.LENGTH_SHORT).show();
			
		}
	}
}


















