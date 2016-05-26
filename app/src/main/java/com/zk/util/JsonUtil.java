package com.zk.util;

/**
 * Created by Administrator on 2016/5/26.
 */
public class JsonUtil {


    /**
     * 去除bom报头
     */
    public static String formatString(String s) {
        if (s != null) {
            s = s.replaceAll("\ufeff", "");
        }
        return s;
    }
}
