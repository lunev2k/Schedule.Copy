package com.lunev2k.schedule.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.lunev2k.schedule.database.DatabaseContract.SQL_CREATE_LEARNERS;
import static com.lunev2k.schedule.database.DatabaseContract.SQL_CREATE_LESSONS;
import static com.lunev2k.schedule.database.DatabaseContract.SQL_CREATE_STUDIES;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "schedule.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LEARNERS);
        db.execSQL(SQL_CREATE_STUDIES);
        db.execSQL(SQL_CREATE_LESSONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
