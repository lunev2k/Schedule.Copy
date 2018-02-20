package com.lunev2k.schedule.model;

import java.util.Date;

public class Study {
    private final long id;
    private final Date date;
    private final Learner learner;

    public Study(long id, Date date, Learner learner) {

        this.id = id;
        this.date = date;
        this.learner = learner;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Learner getLearner() {
        return learner;
    }
}
