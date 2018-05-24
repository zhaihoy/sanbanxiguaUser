package com.hbbc.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 检查更新工具类
 * 查到更新下载并安装
 * @author peak
 *
 */
public class CheckUpdateUtil {
	
	private Context context;
	ProgressDialog pBar ;
	
	public CheckUpdateUtil(Context context) {
		this.context = context;
		pBar = new ProgressDialog(context);
		pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pBar.setMax(100);
	}
	
//	public void notNewVersionShow() {
//		int verCode = CommonUtil.getVerCode(context);
//		String verName = CommonUtil.getVerName(context);
//		StringBuffer sb = new StringBuffer();
//		sb.append("当前版本:");
//		sb.append(verCode);
//		sb.append(",已是最新版,无需更新!");
//		Dialog dialog = new AlertDialog.Builder(context).setTitle("软件更新")
//				.setMessage(sb.toString())// 设置内容
//				.setPositiveButton("确定",// 设置确定按钮
//						new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog,
//							int which) {
//						dialog.dismiss();
//					}
//				}).create();// 创建
//		// 显示对话框
//		dialog.show();
//	}

	public void doNewVersionUpdate(final String apkurl, Context context, final String newversion) {
		String verName = CommonUtil.getVerName(context)+"."+CommonUtil.getVerCode(context);
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");  
		sb.append(verName);  
		sb.append("\t\t 发现新版本:");
		sb.append(newversion);
//		sb.append(GlobalConfig.newVersionName);
		Dialog dialog = new AlertDialog.Builder(context)
		.setTitle("软件更新")  
		.setMessage(sb.toString())  
		// 设置内容  
		.setPositiveButton("更新",// 设置确定按钮  
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				//新版本
				pBar.setTitle("正在下载");
				pBar.setMessage("请稍候...");  
				downFile(apkurl);
			}
		})  
		.setNegativeButton("暂不更新",  
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {  
				// 点击"取消"按钮之后退出程序  
				dialog.dismiss(); 
			}  
		}).create();// 创建  
		// 显示对话框  
		dialog.show();
	}

	/**
	 * 下载新版本的apk
     */
	void downFile(final String urlString) {
		pBar.show();  
		new Thread() {
			public void run() {
                FileOutputStream fileOutputStream=null;
				try {
					URL url = new URL(urlString);
					HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

					urlConn.setConnectTimeout(5000);
					urlConn.setDoInput(true); // 设置输入流采用字节流
					urlConn.setDoOutput(true);// 设置输出流采用字节流
					urlConn.setRequestMethod("GET");
					urlConn.connect();// 连接既往服务端发送信息

					int code = urlConn.getResponseCode();//返回码
					if(code==200){
						InputStream is=urlConn.getInputStream();
						int contentLength = urlConn.getContentLength();
						if (is != null) {
							File file = new File(
									Environment.getExternalStorageDirectory(),
									GlobalConfig.AppName+ GlobalConfig.newVersion);
							fileOutputStream = new FileOutputStream(file);
							byte[] buf = new byte[1024];
							int ch = -1;
							int count = 0;

							while ((ch = is.read(buf)) != -1) {
								fileOutputStream.write(buf, 0, ch);
								count += ch;

								Message msg=handler.obtainMessage();
								msg.arg1=count*100/contentLength;
								msg.what=2;
								handler.sendMessage(msg);
							}
						}
						fileOutputStream.flush();

						down();
					}
				} catch (IOException e) {
					e.printStackTrace();  
				}  finally {
                    try {
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                    } catch (IOException e) {
						LogUtil.exception(e);
                    }
                }
            }
		}.start();  
	}

	//与下面down()中对应
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what){
				case 1:

					pBar.cancel();
					update();
					break;
				case 2:
					int v=msg.arg1;
					pBar.setProgress(v);
					break;
			}
		}
	};

	/**
	 * 下载完成，通过handler将下载对话框取消
	 */
	public void down(){
		Message message = handler.obtainMessage();
		message.what=1;
		handler.sendMessage(message);
	}

	/**
	 * 安装应用
	 */
	public void update(){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), GlobalConfig.AppName+ GlobalConfig.newVersion))
				, "application/vnd.android.package-archive");
		context.startActivity(intent);
	}


}
