package com.lunev2k.schedule.model;

public class LearnersItem {
    private final long id;
    private final String name;
    private final int pay;

    public LearnersItem(long id, String name, int pay) {
        this.id = id;
        this.name = name;
        this.pay = pay;
    }

    public String getName() {
        return name;
    }

    public int getPay() {
        return pay;
    }

    public long getId() {
        return id;
    }
}
