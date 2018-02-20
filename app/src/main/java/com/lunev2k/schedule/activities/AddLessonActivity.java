package com.lunev2k.schedule.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.lunev2k.schedule.R;
import com.lunev2k.schedule.database.DatabaseRepository;
import com.lunev2k.schedule.database.Repository;
import com.lunev2k.schedule.fragments.dialogs.ChoiceLearnerFragment;
import com.lunev2k.schedule.fragments.dialogs.DatePickerFragment;
import com.lunev2k.schedule.fragments.dialogs.TimePickerFragment;
import com.lunev2k.schedule.model.Learner;
import com.lunev2k.schedule.model.Lesson;
import com.lunev2k.schedule.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddLessonActivity extends AppCompatActivity implements ChoiceLearnerFragment.NoticeChoiceLearnerDialogListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fabAddLessonDone)
    FloatingActionButton fabAddLessonDone;
    @BindView(R.id.etStartDate)
    EditText etStartDate;
    @BindView(R.id.etStartTime)
    EditText etStartTime;
    @BindView(R.id.etFinishDate)
    EditText etFinishDate;
    @BindView(R.id.swRepeat)
    Switch swRepeat;
    @BindView(R.id.etLearnerName)
    EditText etLearnerName;
    @BindView(R.id.tilStartDate)
    TextInputLayout tilStartDate;
    @BindView(R.id.tilStartTime)
    TextInputLayout tilStartTime;
    @BindView(R.id.tilLearnerName)
    TextInputLayout tilLearnerName;
    @BindView(R.id.tilFinishDate)
    TextInputLayout tilFinishDate;

    private Calendar startDatetime;
    DatePickerDialog.OnDateSetListener onStartDate = (view, year, monthOfYear, dayOfMonth) ->
            setStartDate(dayOfMonth, monthOfYear, year);
    TimePickerDialog.OnTimeSetListener onStartTime = (view, hour, minute) ->
            setStartTime(hour, minute);
    private Learner selectedLearner;
    private Calendar finishDate;
    DatePickerDialog.OnDateSetListener onFinishDate = (view, year, monthOfYear, dayOfMonth) ->
            setFinishDate(dayOfMonth, monthOfYear, year);

    @OnClick(R.id.fabAddLessonDone)
    public void addLessonDoneClick(View view) {
        if (tilStartDate.getEditText().getText().toString().isEmpty()) {
            tilStartDate.setError(getString(R.string.error_start_date));
            return;
        }
        if (tilStartTime.getEditText().getText().toString().isEmpty()) {
            tilStartTime.setError(getString(R.string.error_start_time));
            return;
        }
        if (tilLearnerName.getEditText().getText().toString().isEmpty()) {
            tilLearnerName.setError(getString(R.string.error_learner_name));
            return;
        }
        if (swRepeat.isChecked()) {
            if (tilFinishDate.getEditText().getText().toString().isEmpty()) {
                tilFinishDate.setError(getString(R.string.error_finish_date));
                return;
            }
        }
        List<Lesson> lessons = new ArrayList<>();
        if (swRepeat.isChecked()) {
            Calendar calendar = (Calendar) startDatetime.clone();
            do {
                lessons.add(new Lesson(calendar.getTime(), selectedLearner));
                calendar.add(Calendar.DAY_OF_MONTH, 7);
            } while (calendar.before(finishDate));
        } else {
            lessons.add(new Lesson(startDatetime.getTime(), selectedLearner));
        }
        Repository repository = new DatabaseRepository(this);
        repository.addLessons(swRepeat.isChecked() ? finishDate.getTime() : null, selectedLearner, lessons);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesson);
        ButterKnife.bind(this);

        initView();
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

    @OnClick(R.id.etFinishDate)
    public void etFinishDateClick(View view) {
        DatePickerFragment date = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt("year", finishDate.get(Calendar.YEAR));
        args.putInt("month", finishDate.get(Calendar.MONTH));
        args.putInt("day", finishDate.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(onFinishDate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    @OnClick(R.id.etStartTime)
    public void setEtStartTimeClick(View view) {
        TimePickerFragment time = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt("hour", startDatetime.get(Calendar.HOUR_OF_DAY));
        args.putInt("minute", startDatetime.get(Calendar.MINUTE));
        time.setArguments(args);
        time.setCallBack(onStartTime);
        time.show(getSupportFragmentManager(), "Time Picker");
    }

    @OnClick(R.id.etLearnerName)
    public void etLearnerNameClick(View view) {
        DialogFragment dlg = new ChoiceLearnerFragment();
        dlg.show(getSupportFragmentManager(), "dlg");
    }

    @OnClick(R.id.swRepeat)
    public void swRepeatClick(View view) {
        tilFinishDate.setVisibility(swRepeat.isChecked() ? View.VISIBLE : View.INVISIBLE);
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startDatetime = Calendar.getInstance();
        finishDate = Calendar.getInstance();
    }

    private void setStartTime(int hour, int minute) {
        startDatetime.set(Calendar.HOUR_OF_DAY, hour);
        startDatetime.set(Calendar.MINUTE, minute);
        etStartTime.setText(DateTimeUtil.getFormatTime(startDatetime.getTime()));
        tilStartTime.setError("");
    }

    private void setFinishDate(int dayOfMonth, int monthOfYear, int year) {
        finishDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        finishDate.set(Calendar.MONTH, monthOfYear);
        finishDate.set(Calendar.YEAR, year);
        etFinishDate.setText(DateTimeUtil.getFormatDate(finishDate.getTime()));
        tilFinishDate.setError("");
    }

    private void setStartDate(int dayOfMonth, int monthOfYear, int year) {
        startDatetime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        startDatetime.set(Calendar.MONTH, monthOfYear);
        startDatetime.set(Calendar.YEAR, year);
        etStartDate.setText(DateTimeUtil.getFormatDate(startDatetime.getTime()));
        tilStartDate.setError("");
    }

    @Override
    public void onChoiceLearnerDialog(long id) {
        Repository repository = new DatabaseRepository(this);
        selectedLearner = repository.getLearner(id);
        tilLearnerName.getEditText().setText(selectedLearner.getName());
        tilLearnerName.setError("");
    }
}
