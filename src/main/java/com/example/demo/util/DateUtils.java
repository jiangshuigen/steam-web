package com.example.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    /**
     * 获取当天的失效时间
     *
     * @return
     */
    public static long getTime() {
        try {
            //计算失效时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(new Date());
            Date date2 = org.apache.commons.lang3.time.DateUtils.parseDate(date + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(new Date());
            long time = calendar1.getTimeInMillis() / 1000 - calendar2.getTimeInMillis() / 1000;
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当月的失效时间
     *
     * @return
     */
    public static long getMonthTime() {
        try {
            // 获取当前月份最后一天
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            SimpleDateFormat lastDayFormat = new SimpleDateFormat("yyyy-MM-dd");
            String lastDay = lastDayFormat.format(calendar.getTime());
            Date date2 = org.apache.commons.lang3.time.DateUtils.parseDate(lastDay + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(new Date());
            long time = calendar1.getTimeInMillis() / 1000 - calendar2.getTimeInMillis() / 1000;
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
