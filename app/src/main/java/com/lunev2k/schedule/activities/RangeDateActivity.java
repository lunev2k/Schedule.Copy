package com.lunev2k.schedule.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.lunev2k.schedule.R;
import com.lunev2k.schedule.fragments.dialogs.DatePickerFragment;
import com.lunev2k.schedule.utils.DateTimeUtil;
import com.lunev2k.schedule.utils.PrefsUtils;
import com.lunev2k.schedule.utils.RangeDateUtil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lunev2k.schedule.utils.Constants.FINISH_DATE;
import static com.lunev2k.schedule.utils.Constants.START_DATE;

public class RangeDateActivity extends AppCompatActivity {

    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tilStartDate)
    TextInputLayout tilStartDate;
    @BindView(R.id.tilFinishDate)
    TextInputLayout tilFinishDate;

    private Calendar startDatetime;
    DatePickerDialog.OnDateSetListener onStartDate = (view, year, monthOfYear, dayOfMonth) ->
            setStartDate(dayOfMonth, monthOfYear, year);
    private Calendar finishDate;
    DatePickerDialog.OnDateSetListener onFinishDate = (view, year, monthOfYear, dayOfMonth) ->
            setFinishDate(dayOfMonth, monthOfYear, year);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_date);
        ButterKnife.bind(this);

        initView();
    }

    @OnClick(R.id.etStartDate)
    public void etStartDateClick(View view) {
        DatePickerFragment date = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt(YEAR, startDatetime.get(Calendar.YEAR));
        args.putInt(MONTH, startDatetime.get(Calendar.MONTH));
        args.putInt(DAY, startDatetime.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(onStartDate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    @OnClick(R.id.etFinishDate)
    public void etFinishDateClick(View view) {
        DatePickerFragment date = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt(YEAR, finishDate.get(Calendar.YEAR));
        args.putInt(MONTH, finishDate.get(Calendar.MONTH));
        args.putInt(DAY, finishDate.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(onFinishDate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    @OnClick(R.id.fabRangeDateDone)
    public void rangeDateDoneClick(View view) {
        if (startDatetime.after(finishDate)) {
            Toast.makeText(view.getContext(), R.string.error_range_date, Toast.LENGTH_SHORT).show();
            return;
        }
        PrefsUtils.getInstance(this).putLong(START_DATE, startDatetime.getTimeInMillis());
        PrefsUtils.getInstance(this).putLong(FINISH_DATE, finishDate.getTimeInMillis());
        finish();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startDatetime = Calendar.getInstance();
        startDatetime.setTime(RangeDateUtil.getStartDate(this));
        finishDate = Calendar.getInstance();
        finishDate.setTime(RangeDateUtil.getFinishDate(this));
        tilStartDate.getEditText().setText(DateTimeUtil.getFormatDate(startDatetime.getTime()));
        tilFinishDate.getEditText().setText(DateTimeUtil.getFormatDate(finishDate.getTime()));
    }

    private void setStartDate(int dayOfMonth, int monthOfYear, int year) {
        startDatetime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        startDatetime.set(Calendar.MONTH, monthOfYear);
        startDatetime.set(Calendar.YEAR, year);
        tilStartDate.getEditText().setText(DateTimeUtil.getFormatDate(startDatetime.getTime()));
        tilStartDate.setError("");
    }

    private void setFinishDate(int dayOfMonth, int monthOfYear, int year) {
        finishDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        finishDate.set(Calendar.MONTH, monthOfYear);
        finishDate.set(Calendar.YEAR, year);
        tilFinishDate.getEditText().setText(DateTimeUtil.getFormatDate(finishDate.getTime()));
        tilFinishDate.setError("");
    }
}
