package com.lunev2k.schedule.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";

    static final String SQL_CREATE_LEARNERS =
            "CREATE TABLE " + LearnerTable.TABLE_NAME + " (" +
                    LearnerTable._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    LearnerTable.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    LearnerTable.COLUMN_NAME_CONTACT + INT_TYPE + COMMA_SEP +
                    LearnerTable.COLUMN_NAME_PAY + INT_TYPE +
                    " )";

    static final String SQL_CREATE_STUDIES =
            "CREATE TABLE " + StudyTable.TABLE_NAME + " (" +
                    StudyTable._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    StudyTable.COLUMN_NAME_FINISH_DATE + INT_TYPE + COMMA_SEP +
                    StudyTable.COLUMN_NAME_LEARNER + INT_TYPE + COMMA_SEP +
                    " FOREIGN KEY(`learner`) REFERENCES `learner`(`_id`))";

    static final String SQL_CREATE_LESSONS =
            "CREATE TABLE " + LessonTable.TABLE_NAME + " (" +
                    LessonTable._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    LessonTable.COLUMN_NAME_DATE + INT_TYPE + COMMA_SEP +
                    LessonTable.COLUMN_NAME_COST + INT_TYPE + COMMA_SEP +
                    LessonTable.COLUMN_NAME_STUDY + INT_TYPE + COMMA_SEP +
                    " FOREIGN KEY(`study`) REFERENCES `study`(`_id`))";

    private DatabaseContract() {
    }

    static abstract class LearnerTable implements BaseColumns {
        static final String TABLE_NAME = "learner";
        static final String COLUMN_NAME_CONTACT = "contact";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_PAY = "pay";
    }

    static abstract class StudyTable implements BaseColumns {
        static final String TABLE_NAME = "study";
        static final String COLUMN_NAME_FINISH_DATE = "finish_date";
        static final String COLUMN_NAME_LEARNER = "learner";
    }

    static abstract class LessonTable implements BaseColumns {
        static final String TABLE_NAME = "lesson";
        static final String COLUMN_NAME_DATE = "date";
        static final String COLUMN_NAME_COST = "cost";
        static final String COLUMN_NAME_STUDY = "study";
    }
}
