package com.hbbc.mshare.login;

import android.content.Context;
import android.util.Log;

import com.hbbc.mshare.user.search.HistoryDao;
import com.hbbc.util.Constants;
import com.hbbc.util.DataBaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by Administrator on 2017/8/4.
 * TODO 封装出一个通用的Dao类,根据cls来得到对应类的Dao.
 */
public class UserDao {

    private static UserDao userDao;

    public DataBaseHelper helper;

    //// TODO: 2017/8/4 <?,?>
    private Dao<LoginResultBean, ?> dao;



    private UserDao(Context context) {

        if (helper == null)
            helper = DataBaseHelper.getDBHelperInstance(context);

    }



    /**
     * 对外只提供一个Dao实例对象使用
     */
    public static UserDao getDaoInstance(Context context) {

        if (userDao == null) {
            synchronized (HistoryDao.class) {
                if (userDao == null) {
                    userDao = new UserDao(context);
                }
            }
        }
        return userDao;
    }



    public void releaseHelper() {

        if (helper != null) {
            OpenHelperManager.releaseHelper();
            helper = null;
        }
    }



    public int insert(LoginResultBean bean) {

        int result = -1;
        try {
            if (dao == null)
                dao = helper.getDao(LoginResultBean.class);
            result = dao.create(bean);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public int delete(LoginResultBean bean) {

        int result = -1;
        try {
            if (dao == null)
                dao = helper.getDao(LoginResultBean.class);
            result = dao.delete(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }



    public int update(LoginResultBean bean) {

        int result = -1;
        try {
            if (dao == null)
                dao = helper.getDao(LoginResultBean.class);
            result = dao.update(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }



    public void resetAllUsersStatus() {

        try {
            if (dao == null)
                dao = helper.getDao(LoginResultBean.class);
            dao.updateBuilder().updateColumnValue(Constants.isCurrentUser, false).update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * 按手机号是否相同来判断
     */
    public boolean contain(LoginResultBean bean) {

        boolean isContained = false;
        try {
            if (dao == null)
                dao = helper.getDao(LoginResultBean.class);
            LoginResultBean result =
                    dao.queryBuilder().where().eq(Constants.PhoneNumber, bean.getPhoneNumber()).queryForFirst();//查不到返回null
            if (result != null)
                Log.d("tag", "contain: 查询到的已经存在的对象为" + result.toString());
            isContained = result != null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isContained;
    }



    public int queryId(LoginResultBean bean) {

        int id = -1;
        try {
            if (dao == null)
                dao = helper.getDao(LoginResultBean.class);
            LoginResultBean resultBean = dao.queryBuilder().where().eq(Constants.PhoneNumber, bean.getPhoneNumber()).queryForFirst();
            if(resultBean!=null)
                id=resultBean.getId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;

    }

    public LoginResultBean queryWithPhoneNumber(String phoneNumber){
        LoginResultBean resultBean=null;
        try {
            if (dao == null)
                dao = helper.getDao(LoginResultBean.class);
            resultBean = dao.queryBuilder().where().eq(Constants.PhoneNumber, phoneNumber).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultBean;
    }



    /**
     * 查询出当前表中哪个用户是默认自动登陆用户,即 isCurrentUser=true;
     */
    public LoginResultBean query() {

        LoginResultBean resultBean = null;
        try {
            if (dao == null)
                dao = helper.getDao(LoginResultBean.class);
            resultBean = dao.queryBuilder().where().eq(Constants.isCurrentUser, true).queryForFirst();//查不到返回null
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultBean;
    }



    public boolean updateVerificationState() {

        try {
            if (dao == null)
                dao = helper.getDao(LoginResultBean.class);
            LoginResultBean query = query();
            query.setCertificationStatus(1);
            int rows = dao.update(query);
            // TODO: 2017/8/8 为什么这个SQL写法不正确呢?

//            int rows = dao.updateRaw("update login_result_bean set " + Constants.VerificationState + " = ? where " + Constants.isCurrentUser + " = ? ",
//                    "true", "true");

            return rows!=0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
