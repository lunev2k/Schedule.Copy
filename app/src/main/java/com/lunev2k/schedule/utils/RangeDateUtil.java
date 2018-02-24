package com.lunev2k.schedule.utils;

import com.lunev2k.schedule.App;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

public class RangeDateUtil {

    @Inject
    PrefsUtils mPrefsUtils;

    public RangeDateUtil() {
        App.getComponent().inject(this);
    }

    public Date getStartDate() {
        Date date = new Date(mPrefsUtils.getLong("startDate"));
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

    public Date getFinishDate() {
        Date date = new Date(mPrefsUtils.getLong("finishDate"));
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

