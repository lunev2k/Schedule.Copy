package com.lunev2k.schedule.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {

    private DateTimeUtil() {
    }

    public static String getFormatDate(Date date) {
        SimpleDateFormat outputformat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return outputformat.format(date);
    }

    public static String getFormatTime(Date time) {
        SimpleDateFormat outputformat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return outputformat.format(time);
    }

    public static String getFormatDateTime(Date datetime) {
        return String.format("%s %s", getFormatTime(datetime), getFormatDate(datetime));
    }
}