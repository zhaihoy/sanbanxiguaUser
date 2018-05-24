package com.hbbc.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * 本地参数管理
 */
public class GlobalParameter {

    //================ 设置项定义 =================
    public static final String SAVE_PREFS_NAME = "com.hbbc.pgynandroid";

    /**
     * 用户名
     */
    private static final String NAME = "Name";

    /**
     * 主题色
     */
    private static final String THEMECOLOR = "ThemeColor";

    /**
     * 会员ID
     */
    private static final String MEMBERID = "MemberID";

    /**
     * 管理员ID
     */
    private static final String MANAGERID = "ManagerID";

    /**
     * 用户头像
     */
    private static final String HEADPICFIELDID = "HeadPicFieldID";

    /**
     * 用户身份
     */
    private static final String PERSONFLAG = "PersonFlag";

    /**
     * 登录Key值
     */
    private static final String LOGINKEY = "LoginKey";

    /**
     * 手机号
     */
    private static final String PHONENUM = "PhoneNum";

    /**
     * 管理员姓名
     */
    private static final String MANAGERNAME = "ManagerName";

    /**
     * 会员姓名
     */
    private static final String MEMBERNAME = "MemberName";

    /**
     * 管理员的职位
     */
    private static final String POSITION = "Position";

    /**
     * 用户性别
     */
    private static final String SEX = "Sex";

    /**
     * 会员监护人姓名
     */
    private static final String PARENTNAME = "ParentName";

    /**
     * 会员监护人手机号
     */
    private static final String PARENTPHONENUM = "ParentPhoneNum";

    /**
     * 地址
     */
    private static final String ADDRESS = "Address";

    /**
     * 省编号
     */
    private static final String PROVINCECODE = "ProvinceCode";

    /**
     * 城市编号
     */
    private static final String CITYCODE = "CityCode";

    /**
     * 个推记录ID
     */
    private static final String GWGTID = "GWGTID";

    /**
     * 设置存储参数
     */
    private static SharedPreferences settings;

    /**
     * 设置存储参数
     */
    private static SharedPreferences.Editor editor; //存储控制器



    /**
     * 获取个推ID
     */
    public static String getGwgtid() {
        return getString(GWGTID);
    }



    /**
     * 记录个推ID
     */
    public static void setGwgtid(String gwgtid) {
        savePreference(GWGTID, gwgtid);
    }



    /**
     * 获取会员监护人手机号
     */
    public static String getParentphonenum() {
        return getString(PARENTPHONENUM);
    }



    /**
     * 设置会员监护人手机号
     */
    public static void setParentphonenum(String parentphonenum) {
        savePreference(PARENTPHONENUM, parentphonenum);
    }



    /**
     * 获取地址
     */
    public static String getAddress() {
        return getString(ADDRESS);
    }



    /**
     * 设置地址
     */
    public static void setAddress(String address) {
        savePreference(ADDRESS, address);
    }



    /**
     * 获取省编号
     */
    public static String getProvincecode() {
        return getString(PROVINCECODE);
    }



    /**
     * 设置省编号
     */
    public static void setProvincecode(String provincecode) {
        savePreference(PROVINCECODE, provincecode);
    }



    /**
     * 获取主题色
     */
    public static int getThemeColor() {
        return getInteger(THEMECOLOR);
    }



    /**
     * 设置主题色
     */
    public static void setThemeColor(int themecolor) {
        savePreference(THEMECOLOR, themecolor);
    }



    /**
     * 获取城市编号
     */
    public static String getCitycode() {
        return getString(CITYCODE);
    }



    /**
     * 设置城市编号
     */
    public static void setCitycode(String citycode) {
        savePreference(CITYCODE, citycode);
    }



    /**
     * 获取管理员姓名
     */
    public static String getManagername() {
        return getString(MANAGERNAME);
    }



    /**
     * 设置管理员姓名
     */
    public static void setManagername(String managername) {
        savePreference(MANAGERNAME, managername);
    }



    /**
     * 获取会员姓名
     */
    public static String getMembername() {
        return getString(MEMBERNAME);
    }



    /**
     * 设置会员姓名
     */
    public static void setMembername(String membername) {
        savePreference(MEMBERNAME, membername);
    }



    /**
     * 获取管理员职位
     */
    public static String getPosition() {
        return getString(POSITION);
    }



    /**
     * 设置管理员职位
     */
    public static void setPosition(String position) {
        savePreference(POSITION, position);
    }



    /**
     * 获取性别信息
     */
    public static String getSex() {
        return getString(SEX);
    }



    /**
     * 设置性别信息
     */
    public static void setSex(String sex) {
        savePreference(SEX, sex);
    }



    /**
     * 获取会员监护人姓名
     */
    public static String getParentname() {
        return getString(PARENTNAME);
    }



    /**
     * 设置会员监护人姓名
     */
    public static void setParentname(String parentname) {
        savePreference(PARENTNAME, parentname);
    }



    //Context赋值
    public static void initSharePreferences(Context context) {
        settings = context.getSharedPreferences(SAVE_PREFS_NAME, Context.MODE_PRIVATE);
        //存储控制器
        editor = settings.edit();
    }



    /**
     * 获取用户手机号
     */
    public static String getPhonenum() {
        return getString(PHONENUM);
    }



    /**
     * 存储手机号
     */
    public static void setPhonenum(String phonenum) {
        savePreference(PHONENUM, phonenum);
    }



    /**
     * 获取登录码
     */
    public static String getLoginkey() {
        return getString(LOGINKEY);
    }



    /**
     * 存储登录key值
     */
    public static void setLoginkey(String loginkey) {
        savePreference(LOGINKEY, loginkey);
    }



    public static String getPersonflag() {

        return getString(PERSONFLAG);
    }



    /**
     * 设置用户身份
     */
    public static void setPersonflag(String flag) {
        savePreference(PERSONFLAG, flag);
    }



    /**
     * 获取用户名
     */
    public static String getName() {
        return getString(NAME);
    }



    /**
     * 存储用户名
     */
    public static void setName(String name) {
        savePreference(NAME, name);
    }



    /**
     * 获取会员ID
     */
    public static String getMemberid() {
        return getString(MEMBERID);
    }



    /**
     * 存储会员ID
     */
    public static void setMemberid(String memberID) {
        savePreference(MEMBERID, memberID);
    }



    /**
     * 获取管理员ID
     */
    public static String getManagerid() {

        return getString(MANAGERID);
    }



    /**
     * 存储管理员ID
     */
    public static void setManagerid(String managerID) {
        savePreference(MANAGERID, managerID);
    }



    /**
     * 存储用户头像
     */
    public static void setHeadpicfield(String headpicfieldid) {
        savePreference(HEADPICFIELDID, headpicfieldid);
    }



    /**
     * 获取用户头像
     */
    public static String getHeadpicfieldid() {
        return getString(HEADPICFIELDID);
    }



    /**
     * 保存本地参数
     */
    public static void savePreference(String preference, Object object) {

        if (object instanceof Integer) {
            editor.putInt(preference, (Integer) object);
        } else if (object instanceof String) {
            editor.putString(preference, (String) object);
        } else if (object instanceof Long) {
            editor.putLong(preference, (Long) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(preference, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(preference, (Float) object);
        }
        editor.commit();
    }



    /**
     * 默认值为""
     */
    public static String getString(String preference) {
        return settings.getString(preference, "");
    }



    /**
     * 设置带有默认值,String
     */
    public static String getDefaultString(String preference, String defaul) {
        return settings.getString(preference, defaul);
    }



    /**
     * 默认值为false
     */
    public static Boolean getBoolean(String preference) {
        return settings.getBoolean(preference, false);
    }



    /**
     * 带有默认值,Boolean
     */
    public static Boolean getDefaultBoolean(String preference, boolean defaul) {
        return settings.getBoolean(preference, defaul);
    }



    /**
     * 默认值为0
     */
    public static Long getLong(String preference) {
        return settings.getLong(preference, 0);
    }



    /**
     * 带有默认值,Long
     */
    public static Long getDefaultLong(String preference, long defaul) {
        return settings.getLong(preference, defaul);
    }



    /**
     * 带有默认值,Float
     */
    public static Float getFloat(String preference, float defaul) {
        return settings.getFloat(preference, defaul);
    }



    /**
     * 默认值为0
     */
    public static Float getDefaultFloat(String preference) {
        return settings.getFloat(preference, 0.0f);
    }



    /**
     * 使用ContentValues进行存储
     */
    public static void savePreferenceContentValues(ContentValues cv) {
        Iterator<Map.Entry<String, Object>> iterator = cv.valueSet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Object> m = iterator.next();
            Object object = m.getValue();
            savePreference(m.getKey(), object);
        }
        //提交
        editor.commit();
    }



    /**
     * 判断是否有此值
     */
    public static boolean contains(String key) {
        return settings.contains(key);
    }



    /**
     * 删除此值
     */
    public static void removeKey(String key) {
        editor.remove(key);
        //异步删除
        SharePreferenceCompat.apply(editor);
    }



    /**
     * 删除所有key,value值
     */
    public static void clearKeyValues() {
        editor.clear();
        //异步清空
        SharePreferenceCompat.apply(editor);
    }



    /**
     * 清空存储数据,但是Key还存在
     */
    public static void clearSharePreference() {
        setHeadpicfield("");
        setManagerid("");
        setMemberid("");
        setName("");
        setPhonenum("");
        setPersonflag("");
        setLoginkey("");
        setMembername("");
        setManagername("");
        setSex("");
        setPosition("");
        setAddress("");
        setCitycode("");
        setProvincecode("");
        setGwgtid("");
    }



    /**
     * 带有默认值，Integer
     */
    public static Integer getDefaultInteger(String preference, int defa) {
        return settings.getInt(preference, defa);
    }



    /**
     * 默认值为0
     */
    public static Integer getInteger(String preference) {
        return settings.getInt(preference, 0);
    }



    /**
     * 存储兼容类
     */
    static class SharePreferenceCompat {
        private static final Method METHOD = findApplyMethod();



        private static Method findApplyMethod() {

            Class clazz = SharedPreferences.Editor.class;

            try {
                return clazz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                LogUtil.exception(e);
            }
            return null;
        }



        /**
         * 执行SharePreference.Editor中Apply函数
         */
        public static void apply(SharedPreferences.Editor editor) {

            if (findApplyMethod() != null) {
                try {
                    //执行apply函数
                    METHOD.invoke(editor);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    LogUtil.exception(e);
                }
                //提交
                editor.commit();
            }
        }
    }
}
