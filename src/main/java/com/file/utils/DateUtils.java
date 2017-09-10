package com.file.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * yyyyMMddHHmmss  yyyy-MM-dd HH:mm:ss
     * 将Date对象转换为特定格式的字符串，
     * 不建议过多调用这个函数，如果默认的时间格式符合，就调相应的函数
     *
     * @param pattern 转换格式
     * @return 转换后的时间字符串
     */
    public static String getStringWithPattern(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static String getStringDateTime(Date date) {
        return dateTimeFormat.format(date);
    }

    public static String getStringDate(Date date) {
        return dateFormat.format(date);
    }

    public static String getStringTime(Date date) {
        return timeFormat.format(date);
    }

    public static Date getDate(String dateStr) {
        try {
            if (dateStr.matches("\\d{1,4}-\\d{1,2}-\\d{1,2}")) {
                return dateFormat.parse(dateStr);
            } else if (dateStr.matches("\\d{1,4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
                return dateTimeFormat.parse(dateStr);
            } else if (dateStr.matches("\\d{1,2}:\\d{1,2}:\\d{1,2}")) {
                return timeFormat.parse(dateStr);
            }
            return dateTimeFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前时间days天后的时间
     *
     * @param days 控制往后的天数,
     * @return 时间对象
     */
    public static Date getDateAfter(Integer days) {
        if (days == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * 获取两个日期之间相差的天数
     *
     * @param startDay 开始日期
     * @param endDay   结束日期
     * @return 相差的天数
     */
    public static Integer getDaysBetween(Date startDay, Date endDay) {
        long dayMillis = endDay.getTime() - startDay.getTime();
        Long days = dayMillis / (1000 * 3600 * 24);
        return days.intValue();
    }

}
