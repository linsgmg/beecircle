package com.lin.baselib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {


    private TimeUtils() {
    }

    /**
     * 获取系统当前时间 格式 2020-06-30
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    /**
     * 获取时间 格式 2020-06-30
     *
     * @return
     */
    public static String getDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    /**
     * 获取时间 格式 2020-06-30 08:30
     *
     * @return
     */
    public static String getDate2(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(date);
    }

    /**
     * 获取时间 格式 2020-06-30 20:20:20
     *
     * @return
     */
    public static String getDate3(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * 获取时间 格式 2020-06-30 20:20:20
     *
     * @return
     */
    public static int getCurrentTimePoint() {
        int currentTime = (int) (System.currentTimeMillis() / 1000);
        return currentTime;
    }

    /**
     * 获取时间 格式 2020-06-30
     *
     * @return
     */
    public static String getDate2(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date1 = getDate(date);
        return simpleDateFormat.format(date1);
    }

    /**
     * 获取时间 格式 2020-06-30
     *
     * @return
     */
    public static String getDateYR(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
        Date date1 = getDate(date);
        return simpleDateFormat.format(date1);
    }

    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param str1 the first date
     * @param str2 the second date
     * @return true <br/>false
     */
    public static boolean isDateOneBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() >= dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param str1 the first date
     * @param str2 the second date
     * @return true <br/>false
     */
    public static boolean isDateOneBigger2(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() >= dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getDate(String str) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy-MM-dd");
            Date date = formatter.parse(str);
            return date;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getDateSecond(String str) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date date = formatter.parse(str);
            return date;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * 时间的加减
     *
     * @param time
     * @param calculate_time
     * @return
     */
    public static String calculateTime(String time, int calculate_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date date = sdf.parse(time);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(date);
            rightNow.add(Calendar.DAY_OF_YEAR, calculate_time);
            Date final_time = rightNow.getTime();
            String format = sdf.format(final_time);
            return format;
//            Log.e(TAG, source + "减60天为：" + format);
            //2019-06-10减60天为：2019-04-11
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间的加减
     *
     * @param time
     * @param calculate_time
     * @return
     */
    public static String calculateTime2(String time, String calculate_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date date = sdf.parse(time);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(date);
            double v = Double.parseDouble(calculate_time);
            int v1 = Integer.parseInt(Arith.round(String.valueOf(v * 3600), 0));
            rightNow.add(Calendar.SECOND, v1);
            Date final_time = rightNow.getTime();
            String format = sdf.format(final_time);
            return format;
//            Log.e(TAG, source + "减60天为：" + format);
            //2019-06-10减60天为：2019-04-11
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得两个日期间距多少天
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static long getTimeDistance(Date beginDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(beginDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, fromCalendar.getMinimum(Calendar.HOUR_OF_DAY));
        fromCalendar.set(Calendar.MINUTE, fromCalendar.getMinimum(Calendar.MINUTE));
        fromCalendar.set(Calendar.SECOND, fromCalendar.getMinimum(Calendar.SECOND));
        fromCalendar.set(Calendar.MILLISECOND, fromCalendar.getMinimum(Calendar.MILLISECOND));

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, fromCalendar.getMinimum(Calendar.HOUR_OF_DAY));
        toCalendar.set(Calendar.MINUTE, fromCalendar.getMinimum(Calendar.MINUTE));
        toCalendar.set(Calendar.SECOND, fromCalendar.getMinimum(Calendar.SECOND));
        toCalendar.set(Calendar.MILLISECOND, fromCalendar.getMinimum(Calendar.MILLISECOND));

        long dayDistance = (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);
        dayDistance = Math.abs(dayDistance);

        return dayDistance;
    }


    /**
     * 获得两个日期间距多少秒
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static long getTimeDistanceSecond(Date beginDate, Date endDate) {

        long to = endDate.getTime();
        long from = beginDate.getTime();
        long distance = from - to;
        long dayDistance = distance;

        return dayDistance;
    }

    //十位时间戳字符串转小时分钟
    public static String Hourmin(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    //十位时间戳字符串转分钟秒
    public static String MinSecond(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    //十位时间戳字符串转年月
    public static String YearMon(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    //十位时间戳字符串转月日
    public static String MonthDay(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("MM月dd日");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    //获取13位字符串格式的时间戳
    public static String getTime13() {

        long time = System.currentTimeMillis();

        String str13 = String.valueOf(time);

        return str13;

    }

    //获取10位字符串格式的时间戳
    public static String getTime() {

        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳

        String str = String.valueOf(time);

        return str;

    }

    public static String YMDHMS(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    public static String DHMS(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("dd日HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        Long i = Long.parseLong(time);
        String times = sdr.format(new Date(i * 1L));
        return times;
    }

    public static String secondToTime(long second) {
        long hours = second / 3600;//转换小时数
        long days = hours / 24;//转换天数
        hours = hours % 24;//剩余秒数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        return unitFormat(days) + "天" + unitFormat(hours) + ":" + unitFormat(minutes) + ":" + unitFormat(second);
//        return unitFormat(second) + "秒";
    }

    public static String secondToHMS(long second) {
        long hours = second / 3600;//转换小时数
        long days = hours / 24;//转换天数
        hours = hours % 24;//剩余秒数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        return unitFormat(hours) + ":" + unitFormat(minutes) + ":" + unitFormat(second);
    }

    public static String secondToMS(long second) {
        long hours = second / 3600;//转换小时数
        long days = hours / 24;//转换天数
        hours = hours % 24;//剩余秒数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        return unitFormat(minutes) + ":" + unitFormat(second);
    }

    private static String unitFormat(long i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + i;
        else
            retStr = "" + i;
        return retStr;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 获取系统当前时间 格式 2020-06-30
     *
     * @return
     */
    public static String getCurrentTimeSecond() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
