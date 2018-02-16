package com.lunev2k.schedule.database;

import com.lunev2k.schedule.model.Learner;

import java.util.List;

/**
 * Created by lunev on 16.02.2018.
 */

public interface Repository {
    long addLearner(String name, int pay);

    List<Learner> getLearners();
}
