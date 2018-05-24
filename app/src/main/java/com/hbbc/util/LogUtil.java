package com.hbbc.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.File;

/*
 * 本类主要是打印debug，info，exception信息
 * 1）日志可以输出到 文件/LogCat，正式发布的时候可以关闭。Debug日志多一些，Info少一些（）
 * 2）日志分成 Debug , Info
 * 3) 包含：log, debug, exception . exception 尽量多的大约退栈信息，方便定位问题。
 */
public class LogUtil {

    /**
     * 打印debug信息到Logcat和文件中
     * eg: LogUtil.debug("hello world");
     *
     * @param msg 日志内容
     */
    public static void debug(final String msg) {
        if (GlobalConfig.LogUtil_DebugFlag) {
            Log.d(GlobalConfig.LogUtil_TAG, msg);
        }
        //开启线程，写入日志文件，防止堵塞
        if (GlobalConfig.LogUtil_SaveLogFile) {
            new Thread() {
                public void run() {
                    String interDir = getInterFileFullName("log");
                    IOUtil.write2SD(DataUtil.getNowDateString() + GlobalConfig.LogUtil_TAG + " --> " + msg + "\n", interDir.toString());
                }
            }.start();
        }
    }



    /**
     * 打印info信息到Logcat
     * eg: LogUtil.info("hello world");
     *
     * @param msg 日志内容
     */
    public static void info(final String msg) {
        if (GlobalConfig.LogUtil_DebugFlag) {
            Log.i(GlobalConfig.LogUtil_TAG, msg);
        }
        //开启线程，写入日志文件，防止堵塞
        if (GlobalConfig.LogUtil_SaveLogFile) {
            new Thread() {
                public void run() {
                    String interDir = getInterFileFullName("log");
                    IOUtil.write2SD(DataUtil.getNowDateString() + GlobalConfig.LogUtil_TAG + " --> " + msg + "\n", interDir);
                }



                ;
            }.start();
        }
    }



    /**
     * 保存exception信息
     * eg:LogUtil.exception(e);
     *
     * @param e 异常对象
     */
    public static void exception(final Exception e) {
        exception("", e);
        Logger.e(e);
    }



    /**
     * 保存exception信息
     * eg:LogUtil.exception(msg);
     *
     * @param msg 异常信息
     */
    public static void exception(final String msg) {
        Logger.e(msg);
        exception(msg, null);
    }



    /**
     * 保存exception信息
     * eg:	LogUtil.exception("磁盘读写异常", e);
     * LogUtil.exception("磁盘读写异常", null);
     *
     * @param msg 异常日志信息
     * @param e   异常对象
     */
    public static void exception(final String msg, final Exception e) {
        //将exception错误信息写入日志文件
        if (GlobalConfig.LogUtil_SaveLogFile) {
            new Thread() {
                public void run() {
                    //String extroDir = getExterFileFullName("exception");
                    String interDir = getInterFileFullName("exception");
                    IOUtil.write2SD(DataUtil.getNowDateString() + GlobalConfig.LogUtil_TAG + " ---> " + msg + "\n", interDir);
                }



                ;
            }.start();
        }
    }



    /**
     * 判断写Debug日志的功能是否开启。返回True，表示开启，False表示没有开启。
     * 可以在Debug类日志输出之前用本方法做一下判断，以减少不必要的Debug日志的相关运算。
     */
    public static Boolean isDebugEnabled() {
        return GlobalConfig.LogUtil_DebugFlag;
    }



    /**
     * 创建外部sd卡的存储路径，和路径下文件的名字
     * 根目录下的名字在Constants下定义为Constants.IOUtil_APPName，文件夹下的文件的名字为filename
     *
     * @return 文件的名字
     */
    private static String getExterFileFullName(String filename) {
        File sdDir = null;
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            sdDir = Environment.getExternalStorageDirectory();      //获取外部sd卡的路径
        }
        File sdExterDir = new File(sdDir + File.separator + GlobalConfig.AppName);        //File.separator 在windows是 \  unix是 /
        if (!sdExterDir.exists()) {
            sdExterDir.mkdir();
        }
        File filePath = new File(sdExterDir + File.separator + " [ " + DataUtil.getNowDateString() + " ] " + filename + ".log");
        return filePath.toString();
    }



    /**
     * 获得（创建）内部sd卡的日志存储全文件名
     * 如：/data/data/[APP_APPName]/[LogUtil_SaveLogDir]/*.log
     *
     * @return 文件的全路径名称。如：/data/data/com.hbbc.sjk/Log/exception20150801.log
     */
    private static String getInterFileFullName(String filename) {
        File interDir = GlobalConfig.APP_Context.getDir(GlobalConfig.LogUtil_SaveLogDir, Context.MODE_APPEND);    //在/data/data/[sppname]创建路径
        File filePath = new File(interDir + File.separator + filename + DataUtil.getNowDateString() + ".log");
        return filePath.toString();
    }


}
