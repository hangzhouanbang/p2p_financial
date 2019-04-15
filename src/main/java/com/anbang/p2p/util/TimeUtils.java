package com.anbang.p2p.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    /**
     * 格式化日期字符串 yyyyMMddHHmmss
     */
    public static String getStringDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date);
    }
}
