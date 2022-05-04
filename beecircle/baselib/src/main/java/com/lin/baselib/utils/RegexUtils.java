package com.lin.baselib.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    /**
     * 正则：正浮点数（最多3位）
     */
    public static final String REGEX_FLOUT2 = "^[0-9]+(.[0-9]{1,2})?$";
    public static final String REGEX_FLOUT3 = "^[0-9]+(.[0-9]{1,3})?$";
    public static final String REGEX_FLOUT8 = "^[0-9]+(.[0-9]{1,8})?$";
    /**
     * 6-16数字和字母组合密码
     */
    public static final String REGEX_PWD = "^(?=[0-9a-zA-Z]*[a-z])(?=[0-9a-zA-Z]*[A-Z])(?=[0-9a-zA-Z]*\\d)[0-9a-zA-Z]{6,16}$";

    /**
     * 验证是否正浮点数最多3位
     *
     * @param value
     * @return
     */
    public static boolean checkValue(String value, String regex) {

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(value);

        return matcher.matches();

    }
}
