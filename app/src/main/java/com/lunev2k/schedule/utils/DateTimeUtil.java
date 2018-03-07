package com.lunev2k.schedule.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {

    private DateTimeUtil() {
    }

    public static String getFormatDate(Date date) {
        return DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(date);
    }

    public static String getFormatTime(Date time) {
        DateFormat outputformat = new SimpleDateFormat("HH:mm");
        return DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(time);
    }

    public static String getFormatDateTime(Date datetime) {
        return String.format("%s %s", getFormatDate(datetime), getFormatTime(datetime));
    }

}