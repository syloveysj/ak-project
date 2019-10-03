package com.yunjian.ak.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/23 18:50
 * @Version 1.0
 */
public class StringUtil {
    public StringUtil() {
    }

    public static String getBetweenStr(String str, String start, String end) {
        return str.substring(str.indexOf(start) + 1, str.indexOf(end)).trim();
    }

    public static String getMatchedStr(String str, String regex, int flag) {
        Pattern pattern = Pattern.compile(regex, flag);
        Matcher matcher = pattern.matcher(str);
        return matcher.find() ? matcher.group() : null;
    }

    public static String firstCharacterToUpper(String srcStr) {
        return srcStr.substring(0, 1).toUpperCase() + srcStr.substring(1);
    }

    public static String replaceUnderlineAndfirstToUpper(String srcStr, String org, String ob) {
        String newString = "";
        boolean b = false;

        while(srcStr.indexOf(org) != -1) {
            int first = srcStr.indexOf(org);
            if (first != srcStr.length() - 1) {
                newString = newString + srcStr.substring(0, first) + ob;
                srcStr = srcStr.substring(first + org.length(), srcStr.length());
                srcStr = firstCharacterToUpper(srcStr);
            } else {
                srcStr = srcStr.substring(0, srcStr.length() - 1);
            }
        }

        newString = newString + srcStr;
        return newString;
    }

    public static String getClassPathByNameSpace(String nameSpace) {
        return nameSpace.split("Mapper$")[0];
    }
}
