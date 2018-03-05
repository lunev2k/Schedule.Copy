package com.lunev2k.schedule.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lunev2k.schedule.App;
import com.lunev2k.schedule.R;
import com.lunev2k.schedule.database.Repository;
import com.lunev2k.schedule.fragments.dialogs.PaymentLessonFragment;
import com.lunev2k.schedule.model.Lesson;
import com.lunev2k.schedule.utils.Constants;
import com.lunev2k.schedule.utils.DateTimeUtil;
import com.lunev2k.schedule.utils.PrefsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewLessonActivity extends AppCompatActivity implements PaymentLessonFragment.PaymentLessonFragmentListener {

    @Inject
    Repository mRepository;
    @Inject
    PrefsUtils mPrefsUtils;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fabLessonCost)
    FloatingActionButton fabLessonCost;
    @BindView(R.id.tvLessonDatetime)
    TextView tvLessonDatetime;
    @BindView(R.id.tvLearnerName)
    TextView tvLearnerName;
    @BindView(R.id.tvLessonCost)
    TextView tvLessonCost;
    @BindView(R.id.tvRepeat)
    TextView tvRepeat;
    private int mPayment;
    private long mIdLesson;

    @OnClick(R.id.fabLessonCost)
    public void lessonCostClick(View view) {
        DialogFragment dialog = new PaymentLessonFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.PAYMENT_LEARNER, mPayment);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "PaymentLessonFragment");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_lesson, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_edit_lesson:
                break;
            case R.id.action_delete_lesson:
                break;
            case R.id.action_move_lesson:
                mPrefsUtils.putLong(Constants.LESSON_ID, mIdLesson);
                Intent intent = new Intent(this, MoveLessonActivity.class);
                intent.putExtra(Constants.LESSON_ID, mIdLesson);
                startActivityForResult(intent, 1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPaymentLesson(int pay) {
        mRepository.setPayment(mIdLesson, pay);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lesson);
        App.getComponent().inject(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mIdLesson = getIntent().getLongExtra(Constants.LESSON_ID, 0);
        if (mIdLesson == 0) {
            mIdLesson = mPrefsUtils.getLong(Constants.LESSON_ID);
        }
        fillData();
    }

    private void fillData() {
        Lesson lesson = mRepository.getLesson(mIdLesson);
        mPayment = lesson.getLearner().getPay();
        tvLessonDatetime.setText(DateTimeUtil.getFormatDateTime(lesson.getDate()));
        tvLearnerName.setText(lesson.getLearner().getName());
        tvLessonCost.setText(lesson.getCost() > 0 ? String.valueOf(lesson.getCost()) : "-");
        if (lesson.getStudy().getDate() != null) {
            tvRepeat.setText(String.format(getString(R.string.text_repeat), DateTimeUtil.getFormatDate(lesson.getStudy().getDate())));
        }
    }

}
