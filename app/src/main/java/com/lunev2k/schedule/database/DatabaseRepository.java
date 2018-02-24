package com.lunev2k.schedule.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lunev2k.schedule.model.Learner;
import com.lunev2k.schedule.model.LearnersItem;
import com.lunev2k.schedule.model.Lesson;
import com.lunev2k.schedule.model.LessonsItem;
import com.lunev2k.schedule.model.Study;
import com.lunev2k.schedule.model.TotalItem;
import com.lunev2k.schedule.utils.PrefsUtils;
import com.lunev2k.schedule.utils.RangeDateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        return db.insert(DatabaseContract.LearnerTable.TABLE_NAME, null, cv);
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

    @Override
    public List<LearnersItem> getLearnerItems() {
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
        long begDate = RangeDateUtil.getStartDate(context).getTime();
        long endDate = RangeDateUtil.getFinishDate(context).getTime();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlQuery = String.format(Locale.getDefault(),
                "SELECT ls._id, date, name, cost FROM lesson ls join study st on st._id = ls.study join learner lr on lr._id = st.learner WHERE ls.date BETWEEN %d AND %d ORDER BY date",
                begDate, endDate);
        Cursor c = db.rawQuery(sqlQuery, null);
        List<LessonsItem> list = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                long id = c.getLong(c.getColumnIndex(DatabaseContract.LessonTable._ID));
                Date date = new Date(c.getLong(c.getColumnIndex(DatabaseContract.LessonTable.COLUMN_NAME_DATE)));
                String name = c.getString(c.getColumnIndex(DatabaseContract.LearnerTable.COLUMN_NAME_NAME));
                int pay = c.getInt(c.getColumnIndex(DatabaseContract.LessonTable.COLUMN_NAME_COST));
                LessonsItem lesson = new LessonsItem(id, date, name, pay);
                list.add(lesson);
            } while (c.moveToNext());
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

    @Override
    public void addLessons(Date date, Learner learner, List<Lesson> lessons) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cvStudy = new ContentValues();
            if (date == null) {
                cvStudy.putNull(DatabaseContract.StudyTable.COLUMN_NAME_FINISH_DATE);
            } else {
                cvStudy.put(DatabaseContract.StudyTable.COLUMN_NAME_FINISH_DATE, date.getTime());
            }
            cvStudy.put(DatabaseContract.StudyTable.COLUMN_NAME_LEARNER, learner.getId());
            long idStudy = db.insert(DatabaseContract.StudyTable.TABLE_NAME, null, cvStudy);
            for (Lesson lesson : lessons) {
                ContentValues cvLesson = new ContentValues();
                cvLesson.put(DatabaseContract.LessonTable.COLUMN_NAME_DATE, lesson.getDate().getTime());
                cvLesson.put(DatabaseContract.LessonTable.COLUMN_NAME_STUDY, idStudy);
                db.insert(DatabaseContract.LessonTable.TABLE_NAME, null, cvLesson);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public Learner getLearner(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseContract.LearnerTable.COLUMN_NAME_NAME,
                DatabaseContract.LearnerTable.COLUMN_NAME_PAY
        };
        String selection = DatabaseContract.LearnerTable._ID + " = ?";
        String[] selectionArgs = new String[]{Long.toString(id)};
        Cursor c = db.query(
                DatabaseContract.LearnerTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        c.moveToFirst();
        String name = c.getString(c.getColumnIndex(DatabaseContract.LearnerTable.COLUMN_NAME_NAME));
        int pay = c.getInt(c.getColumnIndex(DatabaseContract.LearnerTable.COLUMN_NAME_PAY));
        c.close();
        return new Learner(id, name, pay);
    }

    @Override
    public Lesson getLesson(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseContract.LessonTable.COLUMN_NAME_DATE,
                DatabaseContract.LessonTable.COLUMN_NAME_COST,
                DatabaseContract.LessonTable.COLUMN_NAME_STUDY
        };
        String selection = DatabaseContract.LessonTable._ID + " = ?";
        String[] selectionArgs = new String[]{Long.toString(id)};
        Cursor c = db.query(
                DatabaseContract.LessonTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        c.moveToFirst();
        Date date = new Date(c.getLong(c.getColumnIndex(DatabaseContract.LessonTable.COLUMN_NAME_DATE)));
        int cost = c.getInt(c.getColumnIndex(DatabaseContract.LessonTable.COLUMN_NAME_COST));
        Study study = getStudy(c.getLong(c.getColumnIndex(DatabaseContract.LessonTable.COLUMN_NAME_STUDY)));
        Learner learner = study.getLearner();
        c.close();
        return new Lesson(id, date, cost, learner, study);
    }

    @Override
    public Study getStudy(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseContract.StudyTable.COLUMN_NAME_FINISH_DATE,
                DatabaseContract.StudyTable.COLUMN_NAME_LEARNER
        };
        String selection = DatabaseContract.StudyTable._ID + " = ?";
        String[] selectionArgs = new String[]{Long.toString(id)};
        Cursor c = db.query(
                DatabaseContract.StudyTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        c.moveToFirst();
        Date date = null;
        if (c.getLong(c.getColumnIndex(DatabaseContract.StudyTable.COLUMN_NAME_FINISH_DATE)) != 0) {
            date = new Date(c.getLong(c.getColumnIndex(DatabaseContract.StudyTable.COLUMN_NAME_FINISH_DATE)));
        }
        long idLearner = c.getLong(c.getColumnIndex(DatabaseContract.StudyTable.COLUMN_NAME_LEARNER));
        c.close();
        return new Study(id, date, getLearner(idLearner));
    }
}
