package com.lunev2k.schedule.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lunev2k.schedule.model.Learner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lunev on 16.02.2018.
 */

public class DatabaseRepository implements Repository {

    private DbHelper dbHelper;

    public DatabaseRepository(Context context) {
        dbHelper = new DbHelper(context);
    }

    @Override
    public long addLearner(String name, int pay) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.LearnerTable.COLUMN_NAME_NAME, name);
        cv.put(DatabaseContract.LearnerTable.COLUMN_NAME_PAY, pay);
        long id = db.insert(DatabaseContract.LearnerTable.TABLE_NAME, null, cv);
        return id;
    }

    @Override
    public List<Learner> getLearners() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseContract.LearnerTable._ID,
                DatabaseContract.LearnerTable.COLUMN_NAME_NAME,
                DatabaseContract.LearnerTable.COLUMN_NAME_PAY
        };
        String sortOrder = DatabaseContract.LearnerTable.COLUMN_NAME_NAME;
        Cursor c = db.query(
                DatabaseContract.LearnerTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        List<Learner> list = new ArrayList<>();
        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndex(DatabaseContract.LearnerTable._ID));
            String name = c.getString(c.getColumnIndex(DatabaseContract.LearnerTable.COLUMN_NAME_NAME));
            int pay = c.getInt(c.getColumnIndex(DatabaseContract.LearnerTable.COLUMN_NAME_PAY));
            Learner learner = new Learner(id, name, pay);
            list.add(learner);
        }
        c.close();
        return list;
    }
}
