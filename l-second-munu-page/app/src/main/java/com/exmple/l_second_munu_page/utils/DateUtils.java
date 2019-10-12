package com.exmple.l_second_munu_page.utils;

import android.text.TextUtils;
import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {


    /**
     * 秒
     */
    private static String seconds = "秒前";
    /**
     * 分
     */
    private static String minutes = "分钟前";
    /**
     * 时
     */
    private static String hours = "小时前";

    /**
     * 天
     */
    private static String days = "天前";
    /**
     * 月
     */
    private static String months = "个月前";
    /**
     * 刚刚
     */
    private static String before = "刚刚";

    /**
     * The format used is 2013-07-13 05:07
     */
    public static final String PATTERN_YMDHM = "yyyy-MM-dd HH:mm";

    public static final String PATTERN_YMDHM2 = "yyyy/MM/dd HH:mm";

    public static final String PATTERN_YMDHMS = "yyyy-MM-dd HH:mm:ss";

    /**
     * The format used is 2013-07-13
     */
    public static final String PATTERN_YMD = "yyyy-MM-dd";

    /**
     * The format used is 05:07
     */
    public static final String PATTERN_TIME = "HH:mm:ss";

    public static final String PATTERN_FEEDBACK_TIME = "HH:mm";

    public static String format(Date date, String format) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String format(String lo, String format) {
        return format(lo, format, TimeUnit.SECONDS);
    }

    public static String format(String lo, String format, TimeUnit timeU) {
        long time = 0L;
        try {
            time = Long.parseLong(lo);
        } catch (Exception e) {
        }
        if (timeU == TimeUnit.SECONDS) {
            time *= 1000;
        }
        return format(new Date(time), format);
    }

    public static String getYearMonthDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    public static String getDate(String dateStr, String format) {
        if (TextUtils.isEmpty(dateStr)) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(dateStr);
            return format(date, format);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static long getTimeLong(String str) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse(str);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getStringDate(String lo, String format) {
        long time = 0L;
        try {
            time = Long.parseLong(lo);
        } catch (Exception e) {
        }
        Date date = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat(format);
        return sd.format(date);
    }



    // 获取当前月份
    public static int currentMon() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 时间戳转特定格式时间
     *
     * @param dataFormat
     * @param timeStamp
     * @return
     */
    public static String formatData(String dataFormat, long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        timeStamp = timeStamp * 1000;
        SimpleDateFormat format = new SimpleDateFormat(dataFormat);
        return format.format(new Date(timeStamp));
    }

    /**
     * 描述：获取milliseconds表示的日期时间的字符串.
     *
     * @return String 日期时间字符串
     */
    public static String getStringByFormat(long milliseconds) {
        String thisDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            thisDateTime = mSimpleDateFormat.format(milliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thisDateTime;
    }

    /**
     * 判断当前系统时间是否在指定时间的范围内
     *
     * @param beginHour 开始小时，例如22
     * @param beginMin  开始小时的分钟数，例如30
     * @param endHour   结束小时，例如 8
     * @param endMin    结束小时的分钟数，例如0
     * @return true表示在范围内，否则false
     */
    public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {
        boolean result = false;
        final long aDayInMillis = 1000 * 60 * 60 * 24;
        final long currentTimeMillis = System.currentTimeMillis();

        Time now = new Time();
        now.set(currentTimeMillis);

        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        startTime.hour = beginHour;
        startTime.minute = beginMin;

        Time endTime = new Time();
        endTime.set(currentTimeMillis);
        endTime.hour = endHour;
        endTime.minute = endMin;

        if (!startTime.before(endTime)) {
            // 跨天的特殊情况（比如22:00-8:00）
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!now.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
            // 普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
        }
        return result;

    }


    /**
     * 将时间戳转换为指定时间(按指定格式).
     *
     * @param timestamp 时间戳.
     * @param pattern   时间格式.
     * @return 指定时间(按指定格式).
     * @version 1.0
     * @createTime 2014年5月14日, 下午3:58:22
     * @updateTime 2014年5月14日, 下午3:58:22
     * @createAuthor paladin
     * @updateAuthor paladin
     * @updateInfo
     */
    public static String timestampToPatternTime(long timestamp, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(timestamp));
    }


    /**
     * 返回当前时间的格式为 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(System.currentTimeMillis());
    }


    /**
     * 获取当前时间(按指定格式).
     *
     * @param pattern 时间格式.
     * @return 当前时间(按指定格式).
     * @version 1.0
     * @createTime 2014年5月14日, 下午3:54:59
     * @updateTime 2014年5月14日, 下午3:54:59
     * @createAuthor paladin
     * @updateAuthor paladin
     * @updateInfo
     */
    public static String getCurrentTime(long systemTime, String pattern) {
        return timestampToPatternTime(systemTime, pattern);
    }


    /**
     * 判断是否为同一天
     *
     * @param time1 时间丑
     */
    public static boolean isSameDay(long time1, long time2) {
        Date date1 = new Date();
        date1.setTime(time1);

        Date date2 = new Date();
        date2.setTime(time2);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        //
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)) {
            return true;

        }
        return false;
    }



    /**
     * 判断是否为同一个月
     *
     * @param time1 时间丑
     */
    public static boolean isSameMonth(long time1, long time2) {
        Date date1 = new Date();
        date1.setTime(time1);

        Date date2 = new Date();
        date2.setTime(time2);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        //
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        if (calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) {
            return true;

        }
        return false;
    }




    /**
     * 判断是否为昨天(效率比较高)
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true昨天 false不是
     * @throws ParseException
     */
    public static boolean isYesterday(long day)  {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = new Date(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }







}
