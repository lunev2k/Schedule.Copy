package com.lunev2k.schedule.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lunev2k.schedule.App;
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

import javax.inject.Inject;

public class DatabaseRepository implements Repository {

    @Inject
    Context mContext;
    @Inject
    PrefsUtils mPrefsUtils;
    @Inject
    RangeDateUtil mRangeDateUtil;

    private DbHelper dbHelper;

    public DatabaseRepository(Context context) {
        App.getComponent().inject(this);
        dbHelper = new DbHelper(context);
    }

    @Override
    public long addLearner(long contactId, String name, int pay) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.LearnerTable.COLUMN_NAME_NAME, name);
        cv.put(DatabaseContract.LearnerTable.COLUMN_NAME_CONTACT, contactId);
        cv.put(DatabaseContract.LearnerTable.COLUMN_NAME_PAY, pay);
        return db.insert(DatabaseContract.LearnerTable.TABLE_NAME, null, cv);
    }

    @Override
    public void editLearner(long id, long contactId, String name, int pay) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.LearnerTable.COLUMN_NAME_NAME, name);
        cv.put(DatabaseContract.LearnerTable.COLUMN_NAME_CONTACT, contactId);
        cv.put(DatabaseContract.LearnerTable.COLUMN_NAME_PAY, pay);
        db.update(DatabaseContract.LearnerTable.TABLE_NAME, cv,
                DatabaseContract.LearnerTable._ID + " = ?", new String[]{String.valueOf(id)});
    }

    @Override
    public List<Learner> getLearners() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseContract.LearnerTable._ID,
                DatabaseContract.LearnerTable.COLUMN_NAME_NAME,
                DatabaseContract.LearnerTable.COLUMN_NAME_PAY,
                DatabaseContract.LearnerTable.COLUMN_NAME_CONTACT
        };
        String selection = DatabaseContract.LearnerTable.COLUMN_NAME_DELETED + " = ?";
        String[] selectionArgs = new String[]{"0"};
        String sortOrder = DatabaseContract.LearnerTable.COLUMN_NAME_NAME;
        Cursor c = db.query(
                DatabaseContract.LearnerTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        List<Learner> list = new ArrayList<>();
        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndex(DatabaseContract.LearnerTable._ID));
            String name = c.getString(c.getColumnIndex(DatabaseContract.LearnerTable.COLUMN_NAME_NAME));
            int pay = c.getInt(c.getColumnIndex(DatabaseContract.LearnerTable.COLUMN_NAME_PAY));
            long contact = c.getLong(c.getColumnIndex(DatabaseContract.LearnerTable.COLUMN_NAME_CONTACT));
            Learner learner = new Learner(id, name, pay, contact);
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
        String selection = DatabaseContract.LearnerTable.COLUMN_NAME_DELETED + " = ?";
        String[] selectionArgs = new String[]{"0"};
        String sortOrder = DatabaseContract.LearnerTable.COLUMN_NAME_NAME;
        Cursor c = db.query(
                DatabaseContract.LearnerTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
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
        long begDate = mRangeDateUtil.getStartDate().getTime();
        long endDate = mRangeDateUtil.getFinishDate().getTime();
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
        startCalendar.setTime(mRangeDateUtil.getStartDate());
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(mRangeDateUtil.getFinishDate());
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
                DatabaseContract.LearnerTable.COLUMN_NAME_PAY,
                DatabaseContract.LearnerTable.COLUMN_NAME_CONTACT
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
        long contact = c.getLong(c.getColumnIndex(DatabaseContract.LearnerTable.COLUMN_NAME_CONTACT));
        c.close();
        return new Learner(id, name, pay, contact);
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

    @Override
    public void setPayment(long id, int pay) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.LessonTable.COLUMN_NAME_COST, pay);
        db.update(DatabaseContract.LessonTable.TABLE_NAME, cv,
                DatabaseContract.LessonTable._ID + " = ?", new String[]{String.valueOf(id)});
    }

    @Override
    public void moveLesson(long lessonId, Calendar datetime) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.LessonTable.COLUMN_NAME_DATE, datetime.getTimeInMillis());
        db.update(DatabaseContract.LessonTable.TABLE_NAME, cv,
                DatabaseContract.LessonTable._ID + " = ?", new String[]{String.valueOf(lessonId)});
    }

    @Override
    public void moveOneLesson(long lessonId, Calendar datetime) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Lesson lesson = getLesson(lessonId);
            Learner learner = lesson.getLearner();
            Study study = lesson.getStudy();
            ContentValues cvStudy = new ContentValues();
            cvStudy.put(DatabaseContract.StudyTable.COLUMN_NAME_FINISH_DATE, study.getDate().getTime());
            cvStudy.put(DatabaseContract.StudyTable.COLUMN_NAME_LEARNER, learner.getId());
            long idStudy = db.insert(DatabaseContract.StudyTable.TABLE_NAME, null, cvStudy);
            ContentValues cvLesson = new ContentValues();
            cvLesson.put(DatabaseContract.LessonTable.COLUMN_NAME_DATE, datetime.getTimeInMillis());
            cvLesson.put(DatabaseContract.LessonTable.COLUMN_NAME_STUDY, idStudy);
            db.update(DatabaseContract.LessonTable.TABLE_NAME, cvLesson,
                    DatabaseContract.LessonTable._ID + " = ?", new String[]{String.valueOf(lessonId)});

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void moveManyLessons(long lessonId, Calendar datetime) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Lesson lesson = getLesson(lessonId);
            Learner learner = lesson.getLearner();
            Study study = lesson.getStudy();
            long diff = datetime.getTimeInMillis() - lesson.getDate().getTime();
            ContentValues cvStudy = new ContentValues();
            cvStudy.put(DatabaseContract.StudyTable.COLUMN_NAME_FINISH_DATE, study.getDate().getTime());
            cvStudy.put(DatabaseContract.StudyTable.COLUMN_NAME_LEARNER, learner.getId());
            long idStudy = db.insert(DatabaseContract.StudyTable.TABLE_NAME, null, cvStudy);
            Cursor cursor = db.query(DatabaseContract.LessonTable.TABLE_NAME,
                    null,
                    DatabaseContract.LessonTable.COLUMN_NAME_DATE + " >= ?",
                    new String[]{String.valueOf(lesson.getDate().getTime())},
                    null,
                    null,
                    null);
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex(DatabaseContract.LessonTable._ID));
                    long date = cursor.getLong(cursor.getColumnIndex(DatabaseContract.LessonTable.COLUMN_NAME_DATE));
                    ContentValues cvLesson = new ContentValues();
                    cvLesson.put(DatabaseContract.LessonTable.COLUMN_NAME_STUDY, idStudy);
                    cvLesson.put(DatabaseContract.LessonTable.COLUMN_NAME_DATE, date + diff);
                    db.update(DatabaseContract.LessonTable.TABLE_NAME, cvLesson,
                            DatabaseContract.LessonTable._ID + " = ?", new String[]{String.valueOf(id)});
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public List<LessonsItem> getLessonsByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long begDate = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long endDate = calendar.getTimeInMillis();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlQuery = String.format(Locale.getDefault(),
                "SELECT ls._id, date, name, cost FROM lesson ls join study st on st._id = ls.study join learner lr on lr._id = st.learner WHERE ls.date BETWEEN %d AND %d ORDER BY date",
                begDate, endDate);
        Cursor c = db.rawQuery(sqlQuery, null);
        List<LessonsItem> list = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                long id = c.getLong(c.getColumnIndex(DatabaseContract.LessonTable._ID));
                Date currDate = new Date(c.getLong(c.getColumnIndex(DatabaseContract.LessonTable.COLUMN_NAME_DATE)));
                String name = c.getString(c.getColumnIndex(DatabaseContract.LearnerTable.COLUMN_NAME_NAME));
                int pay = c.getInt(c.getColumnIndex(DatabaseContract.LessonTable.COLUMN_NAME_COST));
                LessonsItem lesson = new LessonsItem(id, currDate, name, pay);
                list.add(lesson);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    @Override
    public void editLesson(long lessonId, long learnerId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sqlQuery = "select s._id from study s join lesson l on l.study = s._id where l._id=" + String.valueOf(lessonId);
        Cursor c = db.rawQuery(sqlQuery, null);
        if (c.moveToFirst()) {
            long id = c.getLong(0);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseContract.StudyTable.COLUMN_NAME_LEARNER, learnerId);
            db.update(DatabaseContract.StudyTable.TABLE_NAME, cv,
                    DatabaseContract.StudyTable._ID + " = ?", new String[]{String.valueOf(id)});
        }
        c.close();
    }

    @Override
    public void editOneLesson(long lessonId, long learnerId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Lesson lesson = getLesson(lessonId);
            Study study = lesson.getStudy();
            ContentValues cvStudy = new ContentValues();
            cvStudy.put(DatabaseContract.StudyTable.COLUMN_NAME_FINISH_DATE, study.getDate().getTime());
            cvStudy.put(DatabaseContract.StudyTable.COLUMN_NAME_LEARNER, learnerId);
            long idStudy = db.insert(DatabaseContract.StudyTable.TABLE_NAME, null, cvStudy);
            ContentValues cvLesson = new ContentValues();
            cvLesson.put(DatabaseContract.LessonTable.COLUMN_NAME_STUDY, idStudy);
            db.update(DatabaseContract.LessonTable.TABLE_NAME, cvLesson,
                    DatabaseContract.LessonTable._ID + " = ?", new String[]{String.valueOf(lessonId)});

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void editManyLessons(long lessonId, long learnerId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Lesson lesson = getLesson(lessonId);
            Study study = lesson.getStudy();
            ContentValues cvStudy = new ContentValues();
            cvStudy.put(DatabaseContract.StudyTable.COLUMN_NAME_FINISH_DATE, study.getDate().getTime());
            cvStudy.put(DatabaseContract.StudyTable.COLUMN_NAME_LEARNER, learnerId);
            long idStudy = db.insert(DatabaseContract.StudyTable.TABLE_NAME, null, cvStudy);
            Cursor cursor = db.query(DatabaseContract.LessonTable.TABLE_NAME,
                    null,
                    DatabaseContract.LessonTable.COLUMN_NAME_DATE + " >= ?",
                    new String[]{String.valueOf(lesson.getDate().getTime())},
                    null,
                    null,
                    null);
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex(DatabaseContract.LessonTable._ID));
                    ContentValues cvLesson = new ContentValues();
                    cvLesson.put(DatabaseContract.LessonTable.COLUMN_NAME_STUDY, idStudy);
                    db.update(DatabaseContract.LessonTable.TABLE_NAME, cvLesson,
                            DatabaseContract.LessonTable._ID + " = ?", new String[]{String.valueOf(id)});
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void deleteLearner(long learnerId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.LearnerTable.COLUMN_NAME_DELETED, 1);
        db.update(DatabaseContract.LearnerTable.TABLE_NAME, cv,
                DatabaseContract.LearnerTable._ID + " = ?",
                new String[]{String.valueOf(learnerId)});
    }

    @Override
    public void deleteLesson(long lessonId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Lesson lesson = getLesson(lessonId);
            Study study = lesson.getStudy();
            db.delete(DatabaseContract.LessonTable.TABLE_NAME,
                    DatabaseContract.LessonTable._ID + " = ?",
                    new String[]{String.valueOf(lessonId)});
            db.delete(DatabaseContract.StudyTable.TABLE_NAME,
                    DatabaseContract.StudyTable._ID + " = ?",
                    new String[]{String.valueOf(study.getId())});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void deleteOneLessons(long lessonId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseContract.LessonTable.TABLE_NAME,
                DatabaseContract.LessonTable._ID + " = ?",
                new String[]{String.valueOf(lessonId)});
    }

    @Override
    public void deleteManyLessons(long lessonId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Lesson lesson = getLesson(lessonId);
            Study study = lesson.getStudy();
            db.delete(DatabaseContract.LessonTable.TABLE_NAME,
                    DatabaseContract.LessonTable.COLUMN_NAME_DATE + " >= ? AND " +
                            DatabaseContract.LessonTable.COLUMN_NAME_STUDY + " = ?",
                    new String[]{String.valueOf(lesson.getDate().getTime()), String.valueOf(study.getId())});
            Cursor cursor = db.query(DatabaseContract.LessonTable.TABLE_NAME,
                    null,
                    DatabaseContract.LessonTable.COLUMN_NAME_STUDY + " = ?",
                    new String[]{String.valueOf(study.getId())},
                    null,
                    null,
                    null);
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                db.delete(DatabaseContract.StudyTable.TABLE_NAME,
                        DatabaseContract.StudyTable._ID + " = ?",
                        new String[]{String.valueOf(study.getId())});
            }
            cursor.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
