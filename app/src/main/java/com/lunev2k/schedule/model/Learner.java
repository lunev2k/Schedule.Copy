package com.lunev2k.schedule.model;

public class Learner {
    private final long id;
    private final String name;
    private final int pay;

    public Learner(long id, String name, int pay) {
        this.id = id;
        this.name = name;
        this.pay = pay;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPay() {
        return pay;
    }

    @Override
    public String toString() {
        return getName();
    }
}
