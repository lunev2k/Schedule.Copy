package com.lunev2k.schedule.model;

import java.util.Date;

public class TotalItem {
    private final Date date;
    private final int count;

    public TotalItem(Date date, int count) {
        this.date = date;
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public int getCount() {
        return count;
    }
}
