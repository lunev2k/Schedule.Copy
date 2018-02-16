package com.lunev2k.schedule.model;

import com.lunev2k.schedule.database.Repository;

import java.util.List;

/**
 * Created by lunev on 16.02.2018.
 */

public class Educate {
    private final Repository repository;

    public Educate(Repository repository) {
        this.repository = repository;
    }

    public Learner createLearner(String name, int pay) {
        long id = repository.addLearner(name, pay);
        return new Learner(id, name, pay);
    }

    public List<Learner> getLearners() {
        return repository.getLearners();
    }
}
