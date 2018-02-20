package com.lunev2k.schedule.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_LEARNERS =
            "CREATE TABLE " + LearnerTable.TABLE_NAME + " (" +
                    LearnerTable._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    LearnerTable.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    LearnerTable.COLUMN_NAME_PAY + INT_TYPE +
                    " )";

    public static final String SQL_CREATE_STUDIES =
            "CREATE TABLE " + StudyTable.TABLE_NAME + " (" +
                    StudyTable._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    StudyTable.COLUMN_NAME_FINISH_DATE + INT_TYPE + COMMA_SEP +
                    StudyTable.COLUMN_NAME_LEARNER + INT_TYPE +
                    " )";

    public static final String SQL_CREATE_LESSONS =
            "CREATE TABLE " + LessonTable.TABLE_NAME + " (" +
                    LessonTable._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    LessonTable.COLUMN_NAME_DATE + INT_TYPE + COMMA_SEP +
                    LessonTable.COLUMN_NAME_COST + INT_TYPE + COMMA_SEP +
                    LessonTable.COLUMN_NAME_STUDY + INT_TYPE +
                    " )";

    public DatabaseContract() {
    }

    public static abstract class LearnerTable implements BaseColumns {
        public static final String TABLE_NAME = "learner";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PAY = "pay";
    }

    public static abstract class StudyTable implements BaseColumns {
        public static final String TABLE_NAME = "study";
        public static final String COLUMN_NAME_START_DATE = "start_date";
        public static final String COLUMN_NAME_FINISH_DATE = "finish_date";
        public static final String COLUMN_NAME_LEARNER = "learner";
    }

    public static abstract class LessonTable implements BaseColumns {
        public static final String TABLE_NAME = "lesson";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_COST = "cost";
        public static final String COLUMN_NAME_STUDY = "study";
    }
}
