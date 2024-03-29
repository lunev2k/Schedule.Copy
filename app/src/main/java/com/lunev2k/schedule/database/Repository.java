package com.lunev2k.schedule.database;

import com.lunev2k.schedule.model.Learner;
import com.lunev2k.schedule.model.LearnersItem;
import com.lunev2k.schedule.model.Lesson;
import com.lunev2k.schedule.model.LessonsItem;
import com.lunev2k.schedule.model.Study;
import com.lunev2k.schedule.model.TotalItem;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public interface Repository {
    long addLearner(long contactId, String name, int pay);

    void editLearner(long id, long contactId, String name, int pay);

    List<LearnersItem> getLearnerItems();

    List<Learner> getLearners();

    List<LessonsItem> getLessons();

    List<TotalItem> getTotals();

    void addLessons(Date date, Learner learner, List<Lesson> lessons);

    Learner getLearner(long id);

    Lesson getLesson(long id);

    Study getStudy(long id);

    void setPayment(long id, int pay);

    void moveLesson(long lessonId, Calendar datetime);

    void moveOneLesson(long lessonId, Calendar datetime);

    void moveManyLessons(long lessonId, Calendar datetime);

    List<LessonsItem> getLessonsByDate(Date date);

    void editLesson(long lessonId, long learnerId);

    void editOneLesson(long lessonId, long learnerId);

    void editManyLessons(long lessonId, long learnerId);

    void deleteLearner(long learnerId);

    void deleteLesson(long lessonId);

    void deleteOneLessons(long lessonId);

    void deleteManyLessons(long lessonId);

    int getCountDays();

    CharSequence getFormatDateByPosition(int position);

    Date getDateByPosition(int position);
}
