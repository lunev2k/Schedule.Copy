package com.lunev2k.schedule.database;

import com.lunev2k.schedule.model.LearnersItem;
import com.lunev2k.schedule.model.LessonsItem;
import com.lunev2k.schedule.model.TotalItem;

import java.util.List;

public interface Repository {
    long addLearner(String name, int pay);

    List<LearnersItem> getLearners();

    List<LessonsItem> getLessons();

    List<TotalItem> getTotals();
}
