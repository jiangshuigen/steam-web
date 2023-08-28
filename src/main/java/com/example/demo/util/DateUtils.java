package com.example.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
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
}
