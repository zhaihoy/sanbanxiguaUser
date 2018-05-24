/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hbbc.zxing.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.hbbc.R;
import com.hbbc.zxing.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 */
public final class ViewfinderView extends View {
	private static final String TAG = "log";
	/**
	 * ˢ�½����ʱ��
	 */
	private static final long ANIMATION_DELAY = 10L;
	private static final int OPAQUE = 0xFF;

	/**
	 * �ĸ���ɫ�߽Ƕ�Ӧ�ĳ���
	 */
	private int ScreenRate;
	
	/**
	 * �ĸ���ɫ�߽Ƕ�Ӧ�Ŀ��
	 */
	private static  int CORNER_WIDTH = 3;
	/**
	 * ɨ����е��м��ߵĿ��
	 */
	private static  int MIDDLE_LINE_WIDTH = 2;
	
	/**
	 * ɨ����е��м��ߵ���ɨ������ҵļ�϶
	 */
	private static  int MIDDLE_LINE_PADDING = 2;
	
	/**
	 * �м�������ÿ��ˢ���ƶ��ľ���
	 */
	private static  int SPEEN_DISTANCE = 1;
	
	/**
	 * �ֻ����Ļ�ܶ�
	 */
	private static float density;
	/**
	 * �����С
	 */
	private static final int TEXT_SIZE = 16;
	/**
	 * �������ɨ�������ľ���
	 */
	private static final int TEXT_PADDING_TOP = 30;
	
	/**
	 * ���ʶ��������
	 */
	private Paint paint;
	
	/**
	 * �м们���ߵ����λ��
	 */
	private int slideTop;
	
	/**
	 * �м们���ߵ���׶�λ��
	 */
	private int slideBottom;
	
	/**
	 * ��ɨ��Ķ�ά��������������û��������ܣ���ʱ������
	 */
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	
	private final int resultPointColor;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;

	boolean isFirst;

	private Bitmap logo;
	
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		logo= BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

		density = context.getResources().getDisplayMetrics().density;
		//������ת����dp
		ScreenRate = (int)(20 * density);

		CORNER_WIDTH= (int) (3*density);
		MIDDLE_LINE_WIDTH= (int) (2*density);
		MIDDLE_LINE_PADDING= (int) (2*density);
//		SPEEN_DISTANCE= (int) (CameraManager.get().getFramingRect().height()*0.1);

		paint = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);

		resultPointColor = resources.getColor(R.color.possible_result_points);
		possibleResultPoints = new HashSet<ResultPoint>(5);
	}

	@Override
	public void onDraw(Canvas canvas) {
		//�м��ɨ�����Ҫ�޸�ɨ���Ĵ�С��ȥCameraManager�����޸�
		//首先得到这个扫描框矩形
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}
		SPEEN_DISTANCE = (int) (frame.height()*0.01+0.5);//根据框的高度决定速度的快慢

		//��ʼ���м��߻��������ϱߺ����±�
		if(!isFirst){
			isFirst = true;
			slideTop = frame.top;
			slideBottom = frame.bottom;
		}
		
		//得到屏幕的宽与高值
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		

		//1.把四块半透明区域给绘制出来了
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);

		//2.如果扫描到图片了之后,就把它画在矩形框所在区域
		//  否则就把矩形扫描框画出来
		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		} else {

			//画出来8个蓝色的角边线
			//#3ca4ed
			paint.setColor(getResources().getColor(R.color.mshare_scan_code_rectangle_color));
			canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top
					+ ScreenRate, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top
					+ ScreenRate, paint);
			canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
					+ ScreenRate, frame.bottom, paint);
			canvas.drawRect(frame.left, frame.bottom - ScreenRate,
					frame.left + CORNER_WIDTH, frame.bottom, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.bottom - CORNER_WIDTH,
					frame.right, frame.bottom, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ScreenRate,
					frame.right, frame.bottom, paint);

			
			//�����м����,ÿ��ˢ�½��棬�м���������ƶ�SPEEN_DISTANCE
			slideTop += SPEEN_DISTANCE;
			if(slideTop >= frame.bottom){
				slideTop = frame.top;
			}
			canvas.drawRect(frame.left + MIDDLE_LINE_PADDING,
					slideTop - MIDDLE_LINE_WIDTH/2, frame.right - MIDDLE_LINE_PADDING,
					slideTop + MIDDLE_LINE_WIDTH/2, paint);
			
			
			//��ɨ����������
			//画出说明文字到这个overLayer层上
			paint.setColor(Color.WHITE);
			paint.setTextSize(TEXT_SIZE * density);
			paint.setAlpha(0x40);
			paint.setTypeface(Typeface.create("System", Typeface.BOLD));
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(getResources().getString(R.string.scan_text), frame.left+frame.width()/2,
					(float) (frame.bottom + (float)TEXT_PADDING_TOP *density), paint);

			//画出APP LOGO 到overLayer上
//			paint.setColor(0xff0000ff);
//			canvas.drawBitmap(logo,0,0,paint);

			Collection<ResultPoint> currentPossible = possibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			if (currentPossible.isEmpty()) {
				lastPossibleResultPoints = null;
			} else {
				possibleResultPoints = new HashSet<>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(OPAQUE);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentPossible) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 6.0f, paint);
				}
			}
			if (currentLast != null) {
				paint.setAlpha(OPAQUE / 2);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentLast) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 3.0f, paint);
				}
			}

			
			//ֻˢ��ɨ�������ݣ�����ط���ˢ��
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
					frame.right, frame.bottom);
			
		}
	}


	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}


}
