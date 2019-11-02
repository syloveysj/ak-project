package com.yunjian.ak.utils;

import org.apache.commons.lang.StringUtils;

import java.net.URL;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/6/21 15:39
 * @Version 1.0
 */
public class CommonUtil {
    // 获取url中域名
    public static String urlToDomain(String uri) {
        if(StringUtils.isEmpty(uri) || !StringUtils.startsWithIgnoreCase(uri, "http")) return null;

        try {
            URL url = new URL(uri);
            return url.getHost();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
