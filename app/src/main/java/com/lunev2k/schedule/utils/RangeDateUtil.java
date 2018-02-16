package com.lunev2k.schedule.utils;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by lunev on 16.02.2018.
 */

public class RangeDateUtil {

    private RangeDateUtil() {
    }

    public static Date getStartDate(Context context) {
        Date date = new Date(PrefsUtils.getInstance(context).getLong("startDate"));
        if (date.getTime() == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            return calendar.getTime();
        }
        return date;
    }

    public static Date getFinishDate(Context context) {
        Date date = new Date(PrefsUtils.getInstance(context).getLong("finishDate"));
        if (date.getTime() == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            return calendar.getTime();
        }
        return date;
    }
}

