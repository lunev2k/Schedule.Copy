package com.lunev2k.schedule.model;

import java.util.Date;

public class Lesson {
    private long id;
    private Date date;
    private int cost;
    private Learner learner;

    public Lesson(long id, Date date, int cost, Learner learner) {
        this.id = id;
        this.date = date;
        this.cost = cost;
        this.learner = learner;
    }

    public Lesson(Date date, int cost, Learner learner) {
        this.id = 0;
        this.date = date;
        this.cost = cost;
        this.learner = learner;
    }

    public Lesson(Date date, Learner learner) {
        this.id = 0;
        this.date = date;
        this.cost = 0;
        this.learner = learner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Learner getLearner() {
        return learner;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }
}

