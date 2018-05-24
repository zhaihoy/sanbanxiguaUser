package com.hbbc.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hbbc.mshare.login.LoginResultBean;
import com.hbbc.mshare.user.search.SearchHistoryBean;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Administrator on 2017/8/2.
 *
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static DataBaseHelper dbHelper;

    Context context;



    public DataBaseHelper(Context context) {

        super(context, "mshare_db", null, 1);
        this.context = context;
    }



    /**
     * 全局惟一一个DBHelper对象
     */
    public static DataBaseHelper getDBHelperInstance(Context context) {

        if (dbHelper == null)
            synchronized (DataBaseHelper.class) {
                if (dbHelper == null)
                    dbHelper=new DataBaseHelper(context);
            }
        return dbHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {

        try {
            //创建对应的一张表,保存搜索记录
            TableUtils.createTable(connectionSource, SearchHistoryBean.class);
            //保存登陆注册信息
            TableUtils.createTable(connectionSource, LoginResultBean.class);
            ToastUtil.toast_debug("创建数据库及表成功");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

    }
}
