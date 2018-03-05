package com.lunev2k.schedule.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lunev2k.schedule.App;
import com.lunev2k.schedule.R;
import com.lunev2k.schedule.database.Repository;
import com.lunev2k.schedule.fragments.dialogs.DatePickerFragment;
import com.lunev2k.schedule.fragments.dialogs.TimePickerFragment;
import com.lunev2k.schedule.model.Lesson;
import com.lunev2k.schedule.utils.Constants;
import com.lunev2k.schedule.utils.DateTimeUtil;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoveLessonActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fabMoveLessonDone)
    FloatingActionButton fabMoveLessonDone;
    @BindView(R.id.tilStartDate)
    TextInputLayout tilStartDate;
    @BindView(R.id.tilStartTime)
    TextInputLayout tilStartTime;
    @BindView(R.id.rgRepeat)
    RadioGroup rgRepeat;
    @BindView(R.id.rbOne)
    RadioButton rbOne;
    @BindView(R.id.rbAll)
    RadioButton rbAll;
    @Inject
    Repository mRepository;
    private long mLessonId;
    private Calendar startDatetime;
    DatePickerDialog.OnDateSetListener onStartDate = (view, year, monthOfYear, dayOfMonth) ->
            setStartDate(dayOfMonth, monthOfYear, year);
    TimePickerDialog.OnTimeSetListener onStartTime = (view, hour, minute) ->
            setStartTime(hour, minute);

    @OnClick(R.id.fabMoveLessonDone)
    public void moveLessonDoneClick(View view) {
        if (rgRepeat.getVisibility() == View.VISIBLE) {
            moveManyLessons();
        } else {
            moveOneLesson();
        }
        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.etStartDate)
    public void etStartDateClick(View view) {
        DatePickerFragment date = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt("year", startDatetime.get(Calendar.YEAR));
        args.putInt("month", startDatetime.get(Calendar.MONTH));
        args.putInt("day", startDatetime.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(onStartDate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    @OnClick(R.id.etStartTime)
    public void etStartTimeClick(View view) {
        TimePickerFragment time = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt("hour", startDatetime.get(Calendar.HOUR_OF_DAY));
        args.putInt("minute", startDatetime.get(Calendar.MINUTE));
        time.setArguments(args);
        time.setCallBack(onStartTime);
        time.show(getSupportFragmentManager(), "Time Picker");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_lesson);
        App.getComponent().inject(this);
        ButterKnife.bind(this);
        initView();
    }

    private void moveManyLessons() {

    }

    private void moveOneLesson() {
        mRepository.moveLesson(mLessonId, startDatetime);
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mLessonId = getIntent().getLongExtra(Constants.LESSON_ID, 0);
        if (mLessonId == 0) {
            finish();
        }
        fillData();
    }

    private void fillData() {
        Lesson lesson = mRepository.getLesson(mLessonId);
        startDatetime = Calendar.getInstance();
        startDatetime.setTime(lesson.getDate());
        tilStartDate.getEditText().setText(DateTimeUtil.getFormatDate(lesson.getDate()));
        tilStartTime.getEditText().setText(DateTimeUtil.getFormatTime(lesson.getDate()));
        rgRepeat.setVisibility(lesson.getStudy().getDate() == null ? View.GONE : View.VISIBLE);
        rbOne.setChecked(true);
    }

    private void setStartDate(int dayOfMonth, int monthOfYear, int year) {
        startDatetime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        startDatetime.set(Calendar.MONTH, monthOfYear);
        startDatetime.set(Calendar.YEAR, year);
        tilStartDate.getEditText().setText(DateTimeUtil.getFormatDate(startDatetime.getTime()));
        tilStartDate.setError("");
    }

    private void setStartTime(int hour, int minute) {
        startDatetime.set(Calendar.HOUR_OF_DAY, hour);
        startDatetime.set(Calendar.MINUTE, minute);
        tilStartTime.getEditText().setText(DateTimeUtil.getFormatTime(startDatetime.getTime()));
        tilStartTime.setError("");
    }
}
