package com.lunev2k.schedule.utils;

import java.text.DateFormat;
import java.util.Date;

public class DateTimeUtil {

    private DateTimeUtil() {
    }

    public static String getFormatDate(Date date) {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

    public static String getFormatTime(Date time) {
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(time);
    }

    public static String getFormatDateTime(Date datetime) {
        return String.format("%s %s", getFormatDate(datetime), getFormatTime(datetime));
    }

}