package com.lunev2k.schedule.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lunev2k.schedule.model.LearnersItem;
import com.lunev2k.schedule.model.LessonsItem;
import com.lunev2k.schedule.model.TotalItem;
import com.lunev2k.schedule.utils.DateTimeUtil;
import com.lunev2k.schedule.utils.PrefsUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.lunev2k.schedule.utils.Constants.FINISH_DATE;
import static com.lunev2k.schedule.utils.Constants.START_DATE;

public class DatabaseRepository implements Repository {

    private final Context context;
    private DbHelper dbHelper;

    public DatabaseRepository(Context context) {
        dbHelper = new DbHelper(context);
        this.context = context;
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
    public List<LearnersItem> getLearners() {
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
        List<LearnersItem> list = new ArrayList<>();
        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndex(DatabaseContract.LearnerTable._ID));
            String name = c.getString(c.getColumnIndex(DatabaseContract.LearnerTable.COLUMN_NAME_NAME));
            int pay = c.getInt(c.getColumnIndex(DatabaseContract.LearnerTable.COLUMN_NAME_PAY));
            LearnersItem learner = new LearnersItem(id, name, pay);
            list.add(learner);
        }
        c.close();
        return list;
    }

    @Override
    public List<LessonsItem> getLessons() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlQuery = "SELECT ls._id, date, name, cost FROM lesson ls join study st on st._id = ls.study join learner lr on lr._id = st.learner WHERE date BETWEEN ? AND ? ORDER BY date";
        Cursor c = db.rawQuery(sqlQuery, new String[]{
                String.valueOf(PrefsUtils.getInstance(context).getLong(START_DATE)),
                String.valueOf(PrefsUtils.getInstance(context).getLong(FINISH_DATE))
        });
        List<LessonsItem> list = new ArrayList<>();
        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndex(DatabaseContract.LessonTable._ID));
            Date date = new Date(c.getLong(c.getColumnIndex(DatabaseContract.LessonTable.COLUMN_NAME_DATE)));
            String name = c.getString(c.getColumnIndex(DatabaseContract.LearnerTable.COLUMN_NAME_NAME));
            int pay = c.getInt(c.getColumnIndex(DatabaseContract.LessonTable.COLUMN_NAME_COST));
            LessonsItem lesson = new LessonsItem(id, date, name, pay);
            list.add(lesson);
        }
        c.close();
        return list;
    }

    @Override
    public List<TotalItem> getTotals() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(new Date(PrefsUtils.getInstance(context).getLong(START_DATE)));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(new Date(PrefsUtils.getInstance(context).getLong(FINISH_DATE)));
        List<TotalItem> list = new ArrayList<>();
        while (startCalendar.before(finishCalendar)) {
            Log.d(getClass().getName(), DateTimeUtil.getFormatDateTime(startCalendar.getTime()));
            Calendar calendar = (Calendar) startCalendar.clone();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sqlQuery = "select count(*) from lesson where date between ? and ?";
            Cursor c = db.rawQuery(sqlQuery, new String[]{
                    String.valueOf(startCalendar.getTimeInMillis()),
                    String.valueOf(calendar.getTimeInMillis())
            });
            int count = 0;
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            TotalItem item = new TotalItem(startCalendar.getTime(), count);
            list.add(item);
            c.close();
            startCalendar.add(Calendar.DATE, 1);
        }
        return list;
    }
}
