package com.anbang.p2p.util;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    private static double DAY_TIME =  24 * 60 * 60 * 1000;

    /**
     * 格式化日期字符串 yyyyMMddHHmmss
     */
    public static String getStringDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date);
    }

    /**
     * 格式化日期字符串 yyyyMMddHHmmss
     */
    public static String getStringDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(time);
    }

    /**
     * 格式化日期字符串 yyyyMMddHHmmss
     */
    public static String getStringHr(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        return sdf.format(time);
    }

    public static double repayTime (long maxTime, long nowTime) {
        Long compareTime = maxTime - nowTime;
        double midTime = Double.valueOf(compareTime.toString());
        double result = midTime / DAY_TIME;
        return result;
    }

}
