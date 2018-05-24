package com.hbbc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/8/1.
 */
public class MShareUtil {
    /**
     * 检验:输入的验证码是不是合法
     */
    public static boolean isVerificationLegal(String verification_code) {
        Pattern pattern=Pattern.compile("\\d{4}");
        Matcher matcher=pattern.matcher(verification_code);
        return matcher.matches();
    }

    //�验证手机号是否正确ֻ��
    public static boolean isMobileNO(String mobiles) {

        Pattern p = Pattern.compile("^(13[0-9]|14[57]|15[0-35-9]|17[6-8]|18[0-9])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    //�验证身份证号(18位)是否正确ֻ��
    public static boolean isIDLegal(String id) {

        Pattern p = Pattern.compile("^[1-9][0-9]{5}(19[0-9]{2}|200[0-9]|2010)(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[0-9]{3}[0-9xX]$");
//        Pattern p = Pattern.compile("^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$"); 漏洞:少一位也可以进入.
        Matcher m = p.matcher(id);
        return m.matches();
    }



}
