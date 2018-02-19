package com.lunev2k.schedule.model;

import java.util.Date;

public class LessonsItem {
    private final long id;
    private final Date date;
    private final String name;
    private final int cost;

    public LessonsItem(long id, Date date, String name, int cost) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.cost = cost;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public Date getDate() {
        return date;
    }
}
