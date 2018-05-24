package com.hbbc.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ParseException;
import android.telephony.TelephonyManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *手机基本信息工具类，eg:CommonUtil.getIMEI(context)
 */
public class CommonUtil {



	/**
	 * 返回手机的imei
	 */
	public static String getIMEI(Context context) {
		String imei="";
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			imei = telephonyManager.getDeviceId();
		}catch (Exception e){
			LogUtil.exception(e);
		}
		return imei;
	}



	/**
	 * 获得手机UA
	 */
	public static String getUa() {
		return android.os.Build.MODEL;
	}



    /**
     * 判断本手机是否注册过,eg:boolean flag=CommonUtil.isRegist(context);
     */
	public static boolean isRegist(Context context) {
		boolean flag = false;

		if (GlobalConfig.MemberUserID == null | GlobalConfig.MemberUserID.equals("")) {
			flag = false;
		} else {
			flag = true;
		}
		return flag;
	}



	/**
	 * 获取app版本号
	 */
	public static int getVerCode(Context context) {  
		int verCode = -1;  
		try {
			verCode = context.getPackageManager().getPackageInfo(
					GlobalConfig.PackageName, 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}  
		return verCode;
	}  



	/*
	 * 获取app版本名 ,eg:String verName=CommonUtil.getVerName(context);
	 */
	public static String getVerName(Context context) {  
		String verName = "";  
		try {  
			verName = context.getPackageManager().getPackageInfo(  
					GlobalConfig.PackageName, 0).versionName;
		} catch (NameNotFoundException e) {  
			e.printStackTrace(); 
		}  
		return verName;     
	}

	/**
	 *获取当前时间，eg:String date=CommonUtil.getcurrenttime(); 格式为11月12日 09:00
	 */
	public static String getCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"MM月dd日    HH:mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}



	/**
	 *获取当前时间，eg:String date=CommonUtil.getcurrenttimesimple(); 格式为2016-11-12
	 */
	public static String getCurrentTimeSimple() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}



	/**
	 * 字符串转换成日期，eg:Date date=CommonUtil.StrToDate(str)
	 */
	public static Date strToDate(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			try {
				date = format.parse(str);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}



	/**
	 * 日期转换成字符串,eg:String date=CommonUtil.DataToStr(date);
	 */
	public static String dateToStr(Date date) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);
		return str;
	}

}
