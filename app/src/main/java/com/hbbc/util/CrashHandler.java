package com.hbbc.util;

/**
 * Created by Administrator on 2017/9/5.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //遇到异常,关闭掉所有的页面并退出系统
        MyApplication.getInstance().exit();
    }
}
