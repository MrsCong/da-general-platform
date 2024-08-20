package com.dgp.common.utils;

import com.dgp.common.exception.NormalException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

/**
 * @author:LQL
 * @date:2020年3月14日
 * @description:日期处理工具
 */
@Slf4j
public class DateUtil {

    /**
     * 年-月-日 时:分:秒
     */
    public static final String FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 年-月-日
     */
    public static final String FORMAT_YMD = "yyyy-MM-dd";
    /**
     * 时:分:秒
     */
    public static final String FORMAT_HMS = "HH:mm:ss";
    /**
     * 年月日时分秒
     */
    public static final String FORMAT_YMDHMS_NOS = "yyyyMMddHHmmss";

    /**
     * 年-月-日 时:分:秒:SSS
     */
    public static final String FORMAT_YMDHMS_SSS = "yyyy-MM-dd HH:mm:ss:SSS";

    public static final String fmt5 = "yyyyMMdd";

    /**
     * 一天的毫秒数
     */
    public static final Long dayLong = 1000 * 24 * 60 * 60L;

    /**
     * 一小时的毫秒数
     */
    public static final Long hourLong = 1000 * 60 * 60L;

    /**
     * 一分钟的毫秒数
     */
    public static final Long miniuteLong = 1000 * 60 * 60L;


    /**
     * 年-月
     */
    public static final String FORMAT_YM = "yyyy-MM";

    public static String getDate_ymdhms() {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(FORMAT_YMDHMS);
        Date date = new Date();
        return simpledateformat.format(date);
    }

    public static Date parseDate(String timeStr, String timeFormat) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(timeFormat);
        try {
            return simpledateformat.parse(timeStr);
        } catch (ParseException e) {
            log.error("{} cannot be converted to Time", timeStr, e);
        }
        return null;
    }

    /**
     * string转LocalDateTime
     *
     * @param str
     * @return
     */
    public static LocalDateTime getLocalDateTime(String str, String timeFm) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(timeFm);
        return LocalDateTime.parse(str, dateTimeFormatter);
    }

    /**
     * string转LocalDate
     *
     * @param str
     * @return
     */
    public static LocalDate getLocalDate(String str, String timeFm) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(timeFm);
        return LocalDate.parse(str, dateTimeFormatter);
    }

    public static LocalDate getLocalDate(String str) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_YMD);
        return LocalDate.parse(str, dateTimeFormatter);
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static String asStringByLocalDateTime(Date date) {
        return DateUtil
                .getStrDateTime(Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault())
                        .toLocalDateTime());
    }

    public static String getStrTime(LocalDateTime date) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(FORMAT_YMDHMS_SSS);
        return df.format(date);
    }

    public static String getStrTime(LocalDateTime date, String fmt) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(fmt);
        return df.format(date);
    }

    public static String getStrDateTime(LocalDateTime date) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(FORMAT_YMDHMS_NOS);
        return df.format(date);
    }

    public static String getStrDateTimeStandard(LocalDateTime date) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(FORMAT_YMDHMS);
        return df.format(date);
    }

    public static String getStrDate(LocalDate date) {
        if (Objects.isNull(date)) {
            return "";
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(FORMAT_YMD);
        return df.format(date);
    }

    public static String getNowDateStr() {
        return getStrDate(LocalDate.now());
    }

    public static String getNowDateStr(String format) {
        return getStrDate(LocalDate.now(), format);
    }

    public static Date strToDate(String time) {
        String date = time.replace("Z", " UTC");// 是空格+UTC
        Date dateTime = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");// 格式化的表达式
        try {
            dateTime = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    /**
     * LocalDateTime转化时间戳
     *
     * @param dateTime
     * @return
     */
    public static long localDateTimeToTimestamp(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static long localDateTimeToTimestampSecond(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli() / 1000;
    }

    public static long localDateToTimestamp(LocalDate date) {
        return date.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
    }

    /**
     * 计算某天 到当前天的天数差 当前时间也计算 今天-今天 = 1（天）
     *
     * @param dateTime
     * @return
     */
    public static Long betweenDateAndNow(LocalDateTime dateTime) {
        return LocalDate.now().toEpochDay() - dateTime.toLocalDate().toEpochDay() + 1;
    }

    public static Long betweenDateAndNow(LocalDate dateTime) {
        return LocalDate.now().toEpochDay() - dateTime.toEpochDay() + 1;
    }

    public static String getDateStr(String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return df.format(LocalDateTime.now());
    }

    /**
     * 输入1-7得到对应的中文星期转义
     *
     * @param week 1~7, 1 - 星期一， 2 - 星期二， ...
     * @return 星期的中文
     */
    public static String getWeekZH(Integer week) {
        if (null == week || week <= 0 || week >= 8) {
            return "";
        }

        String[] weekList = {"一", "二", "三", "四", "五", "六", "日"};
        return "星期" + weekList[week - 1];
    }

    /**
     * 输入一周的数字星期几 得到是否工作日 范围 1-7
     *
     * @param day 1-7
     * @return 是工作日：true 不是工作日：false 输入范围外的返回null
     */
    public static Boolean checkIsWorkingDay(int day) {
        if (day >= 1 && day <= 7) {
            return day <= 5;
        }
        throw new NormalException("星期索引超界！");
    }

    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static LocalDateTime getDateTimeOfTimestamp1000(long timestamp) {
        int mul = 1000;
        Instant instant = Instant.ofEpochMilli(timestamp * mul);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return String
     */
    public static String getStrDate(LocalDate date, String fmt) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(fmt);
        return df.format(date);
    }

    /**
     * LocalDateTime 转 LocalDate
     *
     * @param localDateTime 传入的 LocalDateTime
     * @return LocalDate
     */
    public static LocalDate getLocalDateTimeToLocalDate(LocalDateTime localDateTime) {
        String createLocalDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return LocalDate.parse(createLocalDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 将yyyy-MM-dd hh:mm:ss 转成 yyyy年MM月dd日 hh:mm:ss
     *
     * @param localDateTime 时间字符串
     * @return string
     */
    public static String getLocalDateTimeForStr(String localDateTime) {
        localDateTime = localDateTime.trim().replace("T", " ");
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(localDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss").format(date);
    }

    /**
     * 获取日期交集
     *
     * @param localDate1  固定日期始
     * @param localDate2  固定日期末
     * @param searchDate1 查询日期始
     * @param searchDate2 查询日期末
     * @return 交集数组
     */
    private static LocalDate[] getIntersection(LocalDate localDate1, LocalDate localDate2,
                                               LocalDate searchDate1,
                                               LocalDate searchDate2) {

        LocalDate from;
        LocalDate to;
        // 说明：searchDate1小于searchDate2（| 表示 localDate 、 表示 searchDate）
        if (localDate1.isBefore(searchDate1) && localDate2.isAfter(searchDate2)) {
            // ______|____、________、___|_______
            from = searchDate1;
            to = searchDate2;
        } else if (localDate1.isBefore(searchDate1) && localDate2.isBefore(searchDate2)
                && searchDate1.isBefore(localDate2)) {
            // ______|____、___________|____、___
            from = searchDate1;
            to = localDate2;
        } else if (localDate1.isAfter(searchDate1) && localDate2.isAfter(searchDate2)
                && localDate1.isBefore(searchDate2)) {
            // ___、___|____________、___|_______
            from = localDate1;
            to = searchDate2;
        } else if (localDate1.isAfter(searchDate1) && localDate2.isBefore(searchDate2)) {
            // ___、___|_______________|___、____
            from = localDate1;
            to = localDate2;
        } else {
            // 没有交集
            return null;
        }

        return new LocalDate[]{from, to};
    }

    /**
     * yyyyMMdd 转换为yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String formatDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return "";
        }
        return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6);
    }

    public static Date reverse2Date(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (date.trim().length()) {
            case 8:
                simple = new SimpleDateFormat("HH:mm:ss");
                break;
            case 10:
                simple = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case 19:
                simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        try {
            return simple.parse(date.trim());
        } catch (ParseException arg2) {
            arg2.printStackTrace();
            return null;
        }
    }

    public static java.sql.Date reverse2SqlDate(String date) {
        SimpleDateFormat simple = null;
        switch (date.trim().length()) {
            case 8:
                simple = new SimpleDateFormat("HH:mm:ss");
                break;
            case 10:
                simple = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case 19:
                simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        try {
            java.sql.Date e = new java.sql.Date(simple.parse(date.trim()).getTime());
            return e;
        } catch (ParseException arg2) {
            arg2.printStackTrace();
            return null;
        }
    }

    /**
     * 当前时间的时间戳
     *
     * @return 时间
     */
    public static long current() {
        return System.currentTimeMillis();
    }

    /**
     * 当前时间的时间戳（秒）
     *
     * @return 当前时间秒数
     */
    public static long currentSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 判断是否是同一天
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        // TODO Auto-generated method stub

        Calendar calendar = Calendar.getInstance();
        Calendar calendarNow = Calendar.getInstance();
        calendar.setTime(date);
        calendarNow.setTime(new Date());
        return calendar.get(Calendar.YEAR) == calendarNow.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == calendarNow.get(Calendar.MONTH)
                && calendar.get(Calendar.DAY_OF_MONTH) == calendarNow.get(Calendar.DAY_OF_MONTH);

    }

    public static String format(Date date) {
        return format(date, FORMAT_YMD);
    }

    public static String format5(Date date) {
        return format(date, fmt5);
    }

    public static String getStrMonthDate(Date date) {
        if (Objects.isNull(date)) {
            return "";
        }
        return format(date, FORMAT_YM);
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return "";
    }

    /**
     * 获取某一天日期
     */
    public static Date getSomeDate(Date date, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, amount);
        return calendar.getTime();
    }

    /**
     * 判断某一天是否周末
     */
    public static boolean isWeekend(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    /**
     * 获取指定时间内星期几的所有日期
     */
    public static List<String> getWeekInTimes(LocalDate start, LocalDate end, List<Integer> weeks) {
        ArrayList<String> dataList = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long days = ChronoUnit.DAYS.between(start, end);
        Calendar startCalender = GregorianCalendar.from(start.atStartOfDay(ZoneId.systemDefault()));
        for (int i = 0; i < days - 1; i++) {
            startCalender.add(Calendar.DATE, 1);
            weeks.forEach(lst -> {
                // 1代表周日，7代表周六
                if (lst == 7) {
                    lst = 0;
                }
                if (startCalender.get(Calendar.DAY_OF_WEEK) == lst + 1) {
                    dataList.add(format.format(startCalender.getTime()));
                }
            });
        }
        return dataList;
    }

    /**
     * 获取某年某月有多少天
     */
    public static int getDayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 0);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取时间段内的日期
     */
    public static List<Date> getPeriodDateList(Date dBegin, Date dEnd) {
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        calBegin.set(Calendar.DAY_OF_YEAR, calBegin.get(Calendar.DAY_OF_YEAR) - 1);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);
        calEnd.set(Calendar.DAY_OF_YEAR, calEnd.get(Calendar.DAY_OF_YEAR) - 1);
        List<Date> dateList = new ArrayList<>();
        while (calEnd.getTime().after(calBegin.getTime())) {
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(calBegin.getTime());
        }
        calBegin.add(Calendar.DAY_OF_MONTH, 1);
        dateList.add(calBegin.getTime());
        return dateList;
    }

    /**
     * 获取当年的最后一天
     */
    public static Date getCurrYearLast() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年的第一天
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 获取某年最后一天
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    /**
     * 获取某年最后一天
     */
    public static Date getTodayZero() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static boolean overNow(String string) {
        LocalDateTime strDate = getLocalDateTime(string, FORMAT_YMDHMS);
        LocalDateTime now = LocalDateTime.now();
        return localDateTimeToTimestamp(strDate) >= localDateTimeToTimestamp(now);
    }

    public static String makeStrDateMore(String str, Integer ofSet) {
        LocalDate dateTime = getLocalDate(str);
        long in = localDateToTimestamp(dateTime);
        LocalDate now = LocalDate.now();
        LocalDate localDate = now.plusDays(ofSet);
        long out = localDateToTimestamp(localDate);
        if (in > out) {
            return getStrDate(localDate);
        }
        return str;
    }

    /**
     * 两个日期间的天数计算
     */
    public static long daysBetween(Date from, Date to) {
        long difference = (from.getTime() - to.getTime()) / 86400000;
        return Math.abs(difference);
    }

    /**
     * LQL 2022年1月19日
     *
     * @return long
     * @throws
     * @Title: getHourtime
     * @Description: 一小时还剩多少秒
     */

    public static long getHourtime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        return (calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    /**
     * LQL 2022年1月19日
     *
     * @return long
     * @throws
     * @Title: getDaytime
     * @Description: 一天还剩多少秒
     */

    public static long getDaytime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return (calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    /**
     * LQL 2022年1月19日
     *
     * @return long
     * @throws
     * @Title: getWeektime
     * @Description: 一周还剩多少秒
     */

    public static long getWeektime() {
        long dayTime = getDaytime();
        Calendar calendar = Calendar.getInstance();
        int day = 7 - calendar.get(Calendar.DAY_OF_WEEK) + 1;
        return day * 86400 + dayTime;

    }

    /**
     * LQL 2022年1月19日
     *
     * @return long
     * @throws
     * @Title: getMonthtime
     * @Description: 一月还剩多少秒
     */

    public static long getMonthtime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.DATE, 0);
        calendar.add(Calendar.MONTH, 1);
        return (calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    /**
     * LQL 2022年1月19日
     *
     * @return long
     * @throws
     * @Title: getYeartime
     * @Description: 一年还剩多少秒
     */

    public static long getYeartime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.DATE, 0);
        calendar.set(Calendar.MONTH, 0);
        calendar.add(Calendar.YEAR, 1);
        return (calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    public static void main(String agrs[]) {

        System.out.println(getYeartime());
    }

}
