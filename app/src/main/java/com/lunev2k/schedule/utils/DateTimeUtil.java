package com.lunev2k.schedule.utils;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by lunev on 16.02.2018.
 */

public class DateTimeUtil {

    private DateTimeUtil() {
    }

    public static String getFormatDate(Date date) {
        return DateFormat.getDateInstance().format(date);
    }

    public static String getFormatTime(Date time) {
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(time);
    }

    public static String getFormatDateTime(Date datetime) {
        return String.format("%s %s", getFormatDate(datetime), getFormatTime(datetime));
    }

}