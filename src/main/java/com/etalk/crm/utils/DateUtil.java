package com.etalk.crm.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * 日期Util类
 *
 * @author calvin
 */
public class DateUtil {
    private static String defaultDatePattern = "yyyy-MM-dd";
    protected static final Logger logger = LogManager.getLogger(DateUtil.class);

    /**
     * 获得默认的 date pattern
     */
    public static String getDatePattern() {
        return defaultDatePattern;
    }

    /**
     * 返回预设Format的当前日期字符串(yyyy-MM-dd)
     */
    public static String getToday() {
        Date today = new Date();
        return format(today);
    }

    /**
     * 获取格式 yyyy-MM-dd HH:mm:ss
     **/
    public static String getStringToday() {
        Date today = new Date();
        return format(today, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 返回预设Format的当前日期字符串(yyyyMMdd)
     */
    public static String getToday(String pattern) {
        Date today = new Date();
        return format(today, pattern);
    }

    /**
     * 返回当前时间戳(精确到毫秒)
     */
    public static long getTime() {
        Date today = new Date();
        return today.getTime();
    }

    /**
     * 返回当前时间戳(精确到天)
     */
    public static long getDayStamp() {
        return getCurrentDate().getTime();
    }

    /**
     * 时间戳转日期,yyyy-MM-dd HH:mm:ss
     */
    public static Date getDateParse(long time) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(time);

        try {
            date = format.parse(d);
        } catch (ParseException e) {
            logger.error(e.getLocalizedMessage());
        }
        return date;
    }

    /**
     * 时间加分钟
     */
    public static Date addTimeToMin(Date date, int min) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, min); // 减填负数
        return calendar.getTime();
    }

    /**
     * 计算两个日期之间相差的分钟数
     *
     * @param smdate bdate  不区分大小先后
     * @return 相差分钟数
     * @throws ParseException
     */
    public static int minBetween(Date smdate, Date bdate) {
        long between_days = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            if (time1 > time2) {
                between_days = (time1 - time2) / (1000 * 3600);
            } else {
                between_days = (time2 - time1) / (1000 * 3600);
            }
        } catch (ParseException e) {
            logger.error(e.getLocalizedMessage());
        }
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        long between_days = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        } catch (ParseException e) {
            logger.error(e.getLocalizedMessage());
        }
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(String smdate, String bdate) {

        long between_days = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        } catch (ParseException e) {
            logger.error(e.getLocalizedMessage());
        }
        return Integer.parseInt(String.valueOf(between_days));
    }


    /**
     * 日期加天数
     */
    public static Date addDateToDay(Date date, int day) {
        // SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 日期加天数
     */
    public static String addDateToDay(String date, int day) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(sf.parse(date));
        } catch (ParseException e) {
            logger.error(e.getLocalizedMessage());
        }
        calendar.add(Calendar.DATE, day);
        return sf.format(calendar.getTime());
    }

    /**
     * 返回参数Format格式化Datetime (yyyy-MM-dd hh:mm:ss)
     *
     * @throws ParseException
     */
    public static Date getCurrentDatetime() {
        Date currentDate = new Date();
        currentDate = parse(format(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
        return currentDate;
    }

    /**
     * 返回参数Format格式化Datetime (yyyyMMddhhmmssSSS)
     *
     * @throws ParseException
     */
    public static String getDatetime() {
        return format(new Date(), "yyyyMMddHHmmssSSSS");
    }

    /**
     * 返回参数Format格式化Datetime (HH:mm:ss)
     *
     * @throws ParseException
     */
    public static String getTime(Date date) {
        return format(date, "HH:mm:ss");
    }

    /**
     * 返回参数Format格式化Date （yyyy-MM-dd）
     *
     * @throws ParseException
     */
    public static Date getCurrentDate() {
        Date currentDate = new Date();
        currentDate = parse(getToday(), "yyyy-MM-dd");
        return currentDate;
    }

    /**
     * 使用预设Format格式化Date成字符串
     */
    public static String format(Date date) {
        return date == null ? " " : format(date, getDatePattern()).toString();
    }

    /**
     * 使用参数Format格式化Date成字符串
     */
    public static String format(Date date, String pattern) {
        return date == null ? " " : new SimpleDateFormat(pattern).format(date).toString();
    }

    /**
     * 使用参数Format格式化Date成英文格式字符串
     */
    public static String formatEnglish(Date date, String pattern) {
        return date == null ? " " : new SimpleDateFormat(pattern, Locale.ENGLISH).format(date).toString();
    }

    /**
     * 使用预设格式将字符串转为Date
     */
    public static Date parse(String strDate) {

        return StringUtils.isBlank(strDate) ? null : parse(strDate, getDatePattern());
    }

    /**
     * 使用参数Format将字符串转为Date
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            logger.error(e.getLocalizedMessage());
        }
        return date;
    }

    /**
     * 获取指定月最大天数
     */
    public static int getMaxDayOfMonth(Date date) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(date);
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        return day;
    }

    /**
     * 获取当前月最大天数
     */
    public static int getMaxDayOfMonth() {
        Calendar aCalendar = Calendar.getInstance();
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        return day;
    }

    /**
     * 在日期上增加数个整月
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    public static String getLastDayOfMonth(String year, String month) {
        Calendar cal = Calendar.getInstance();
        // 年
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        // 月，因为Calendar里的月是从0开始，所以要-1
        // cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        // 日，设为一号
        cal.set(Calendar.DATE, 1);
        // 月份加一，得到下个月的一号
        cal.add(Calendar.MONTH, 1);
        // 下一个月减一为本月最后一天
        cal.add(Calendar.DATE, -1);
        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));// 获得月末是几号
    }

    public static Date getDate(String year, String month, String day)
            throws ParseException {
        String result = year + "- "
                + (month.length() == 1 ? ("0 " + month) : month) + "- "
                + (day.length() == 1 ? ("0 " + day) : day);
        return parse(result);
    }

    /**
     * 功能描述：返回分
     *
     * @param date 日期
     * @return 返回分钟
     */
    public static int getMinute(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse(date));
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 功能描述：把日期分钟换成倍数
     *
     * @param date 日期 ;mins 分钟倍数的基数
     * @return 返回日期
     */
    public static Date getRoundMinute(Date date, int mins) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int min = calendar.get(Calendar.MINUTE);
        if (min == 0) {
            return date;
        } else {
            return addTimeToMin(date, mins - (min % mins));
        }

    }

    /**
     * 获取下周周一的日期
     */
    public static Date getNextWeekFirstDay() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);

        if (day != Calendar.SUNDAY)
            cal.add(Calendar.WEEK_OF_MONTH, 1);

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        //System.out.println(cal.getTime());
        return cal.getTime();
    }

    /**
     * 获取下周周一的日期
     */
    public static String getNextMonday() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);

        if (day != Calendar.SUNDAY)
            cal.add(Calendar.WEEK_OF_MONTH, 1);

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        //System.out.println(cal.getTime());
        return format(cal.getTime(), "yyyy-MM-dd");
    }

    /**
     * 获取下周周日的日期
     */
    public static Date getNextWeekLastDay() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);

        if (day != Calendar.SUNDAY)
            cal.add(Calendar.WEEK_OF_MONTH, 2);
        else
            cal.add(Calendar.WEEK_OF_MONTH, 1);

        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        //System.out.println(cal.getTime());
        return cal.getTime();
    }

    /**
     * 获取下周周日的日期
     */
    public static String getNextSunday() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);

        if (day != Calendar.SUNDAY)
            cal.add(Calendar.WEEK_OF_MONTH, 2);
        else
            cal.add(Calendar.WEEK_OF_MONTH, 1);

        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        //System.out.println(cal.getTime());
        return format(cal.getTime(), "yyyy-MM-dd");
    }

    //获取当前时间是星期几
    public static int getWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                return 7;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
        }
        return 0;
    }

    public static int getWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                return 7;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
        }
        return 0;
    }

    /**
     * 檢查日期格式正確性
     */
    public static boolean isValidDate(String str, String pattern) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            //logger.error(e.getMessage(), e);
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

    /**
     * 功能描述：返回分
     *
     * @param
     * @return 返回分钟
     */
    public static int getMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 根据当前日期获得所在周的日期区间（周一和周日日期）
     *
     * @return
     * @throws ParseException
     * @author jay
     */
    public static List<String> getTimeInterval(Date date) {
        List<String> weekDay = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        for (int i = 0; i < 7; i++) {
            // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            // 获得当前日期是一个星期的第几天
            int day = cal.get(Calendar.DAY_OF_WEEK);
            cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day + i);
            String imptimeBegin = format(cal.getTime());
            // System.out.println("本周第"+(i+1)+"天：" + imptimeBegin);
            weekDay.add(imptimeBegin);
        }
        return weekDay;
    }

    /**
     * 根据当前日期获得下周的日期
     *
     * @param num 1为下周2为下下周 以此类推
     * @return 日期集合
     * @author jay
     */
    public static List<String> getLastTimeInterval(int num) {
        List<String> weekDay = new ArrayList<String>();
        for (int i = 0; i < 7; i++) {
            Calendar calendar1 = Calendar.getInstance();
            int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
            if (dayOfWeek == 0) {
                dayOfWeek = 7;
            }
            int offset1 = 1 - dayOfWeek;
            calendar1.add(Calendar.DATE, offset1 + (7 * num) + i);
            // System.out.println(sdf.format(calendar1.getTime()));// last Monday
            String nextBeginDate = format(calendar1.getTime());
            System.out.println(nextBeginDate);
            weekDay.add(nextBeginDate);
        }
        return weekDay;
    }
}