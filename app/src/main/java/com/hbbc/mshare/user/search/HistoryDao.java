package com.hbbc.mshare.user.search;

import android.content.Context;

import com.hbbc.util.DataBaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2017/8/2.
 * 封装一个通用的Dao类,根据cls来得到对应类的Dao.
 */
public class HistoryDao {

    private static HistoryDao historyDao;

    public DataBaseHelper helper;

    private Dao<SearchHistoryBean, ?> dao;

    private HistoryDao(Context context) {

        if (helper == null)
            helper = DataBaseHelper.getDBHelperInstance(context);

    }



    /**
     * 对外只提供一个Dao实例对象使用
     */
    public static HistoryDao getDaoInstance(Context context) {

        if (historyDao == null) {
            synchronized (HistoryDao.class) {
                if (historyDao == null) {
                    historyDao = new HistoryDao(context);
                }
            }
        }
        return historyDao;

    }



    public void releaseHelper() {

        if (helper != null) {
            OpenHelperManager.releaseHelper();
            helper = null;
        }
    }



    public int insert(SearchHistoryBean bean) {

        int result = -1;
        try {
            if (dao == null)
                dao = helper.getDao(SearchHistoryBean.class);
            result = dao.create(bean);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }



    public int delete(SearchHistoryBean bean) {

        int result = -1;
        try {
            if (dao == null)
                dao = helper.getDao(SearchHistoryBean.class);
            result = dao.delete(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }



    public int update(SearchHistoryBean bean) {

        int result = -1;
        try {
            if (dao == null)
                dao = helper.getDao(SearchHistoryBean.class);
            result = dao.update(bean);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    //查询本地中所有记录,并且要按照millis,从小到大线性排列
    public List<SearchHistoryBean> queryForAll() {

        List<SearchHistoryBean> result = null;
        try {
            if (dao == null)
                dao = helper.getDao(SearchHistoryBean.class);
            result = dao.queryBuilder().orderBy("millis", false).query();//降序排列

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }



    public boolean contain(SearchHistoryBean bean) {

        boolean isContained = false;
        try {
            if (dao == null)
                dao = helper.getDao(SearchHistoryBean.class);
            SearchHistoryBean result = dao.queryBuilder().where().eq("address", bean.getAddress()).queryForFirst();//查不到返回null
            isContained = result != null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isContained;
    }



    /**
     * 查询出此对象在数据库中对应的记录ID
     */
    public int query(SearchHistoryBean bean) {

        int id = -1;
        try {
            if (dao == null)
                dao = helper.getDao(SearchHistoryBean.class);
            SearchHistoryBean result = dao.queryBuilder().where().eq("address", bean.getAddress()).queryForFirst();//查不到返回null
            if (result != null)
                id = result.getId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }


}
