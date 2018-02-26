package com.lunev2k.schedule.database;

import com.lunev2k.schedule.model.Learner;
import com.lunev2k.schedule.model.LearnersItem;
import com.lunev2k.schedule.model.Lesson;
import com.lunev2k.schedule.model.LessonsItem;
import com.lunev2k.schedule.model.Study;
import com.lunev2k.schedule.model.TotalItem;

import java.util.Date;
import java.util.List;

public interface Repository {
    long addLearner(String name, int pay);

    void editLearner(long id, String name, int pay);

    List<LearnersItem> getLearnerItems();

    List<Learner> getLearners();

    List<LessonsItem> getLessons();

    List<TotalItem> getTotals();

    void addLessons(Date date, Learner learner, List<Lesson> lessons);

    Learner getLearner(long id);

    Lesson getLesson(long id);

    Study getStudy(long id);

    void setPayment(long id, int pay);
}
