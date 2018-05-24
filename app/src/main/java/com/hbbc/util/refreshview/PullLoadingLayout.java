package com.hbbc.util.refreshview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hbbc.R;


public class PullLoadingLayout extends FrameLayout {

	static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;

	private final ImageView headerImage;
	private final ProgressBar headerProgress;
	private final TextView headerText;

	private String pullLabel;
	private String refreshingLabel;
	private String releaseLabel;
	
	private int textColor = 0xff000000;

	private final Animation rotateAnimation, resetRotateAnimation;

	public PullLoadingLayout(Context context, final int mode, String releaseLabel, String pullLabel, String refreshingLabel) {
		super(context);

		ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.util_pull_to_refresh_header, this);
		headerText = (TextView) header.findViewById(R.id.pull_to_refresh_text);
		headerImage = (ImageView) header.findViewById(R.id.pull_to_refresh_image);
		headerProgress = (ProgressBar) header.findViewById(R.id.pull_to_refresh_progress);
		//如果releaseLabel为空的话隐藏ImageView
//		if (releaseLabel.equals("")){
//			headerImage.setVisibility(View.GONE);
//		}
		final Interpolator interpolator = new LinearInterpolator();
		rotateAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
		        0.5f);
		rotateAnimation.setInterpolator(interpolator);
		rotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		rotateAnimation.setFillAfter(true);

		resetRotateAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f,
		        Animation.RELATIVE_TO_SELF, 0.5f);
		resetRotateAnimation.setInterpolator(interpolator);
		resetRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		resetRotateAnimation.setFillAfter(true);

		this.releaseLabel = releaseLabel;
		this.pullLabel = pullLabel;//"下拉可以刷新..."
		this.refreshingLabel = refreshingLabel;//"正在刷新";

		switch (mode) {
			case PullToRefreshBase.MODE_PULL_UP_TO_REFRESH:
				headerText.setText(pullLabel);
				headerText.setTextColor(textColor);
				headerImage.setImageResource(R.drawable.mpush_g_pull_arrow_up);
				break;
			case PullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH:
			default:
				headerText.setText(releaseLabel);
				headerText.setTextColor(textColor);
				headerImage.setImageResource(R.drawable.mpush_g_pull_arrow_down);
				break;
		}
	}

	public void reset() {
		headerText.setText(pullLabel);
		headerText.setTextColor(textColor);
		headerImage.setVisibility(View.VISIBLE);
		headerProgress.setVisibility(View.GONE);
	}

	public void releaseToRefresh() {
		//Log.d("aaa", releaseLabel);
		headerText.setText(releaseLabel);
		headerText.setTextColor(textColor);
		headerImage.clearAnimation();
		headerImage.startAnimation(rotateAnimation);
	}

	public void setPullLabel(String pullLabel) {
		this.pullLabel = pullLabel;
	}

	public void refreshing() {
		headerText.setText(refreshingLabel);
		headerText.setTextColor(textColor);
		headerImage.clearAnimation();
		headerImage.setVisibility(View.INVISIBLE);
		headerProgress.setVisibility(View.VISIBLE);
	}

	public void setRefreshingLabel(String refreshingLabel) {
		this.refreshingLabel = refreshingLabel;
	}

	public void setReleaseLabel(String releaseLabel) {
		this.releaseLabel = releaseLabel;
	}

	public void pullToRefresh() {
		headerText.setText(pullLabel);
		headerText.setTextColor(textColor);
		headerImage.clearAnimation();
		headerImage.startAnimation(resetRotateAnimation);
	}

	public void setTextColor(int color) {
		headerText.setTextColor(textColor);
		textColor = color;
		headerText.setTextColor(color);
	}


}
