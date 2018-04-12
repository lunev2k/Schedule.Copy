package com.lunev2k.schedule.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lunev2k.schedule.App;
import com.lunev2k.schedule.R;
import com.lunev2k.schedule.database.Repository;
import com.lunev2k.schedule.fragments.dialogs.ChoiceLearnerFragment;
import com.lunev2k.schedule.model.Learner;
import com.lunev2k.schedule.model.Lesson;
import com.lunev2k.schedule.utils.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditLessonActivity extends AppCompatActivity implements ChoiceLearnerFragment.NoticeChoiceLearnerDialogListener {

    @Inject
    Repository mRepository;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tilLearnerName)
    TextInputLayout tilLearnerName;
    @BindView(R.id.rgRepeat)
    RadioGroup rgRepeat;
    @BindView(R.id.rbOne)
    RadioButton rbOne;
    @BindView(R.id.rbAll)
    RadioButton rbAll;

    private long mLessonId;
    private Learner mLearner;

    @OnClick(R.id.fabEditLessonDone)
    public void editLessonDoneClick(View view) {
        if (rgRepeat.getVisibility() == View.VISIBLE) {
            editManyLessons();
        } else {
            editOneLesson();
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onChoiceLearnerDialog(long id) {
        mLearner = mRepository.getLearner(id);
        tilLearnerName.getEditText().setText(mLearner.getName());
        tilLearnerName.setError("");
    }

    @OnClick(R.id.etLearnerName)
    public void learnerNameClick(View view) {
        DialogFragment dlg = new ChoiceLearnerFragment();
        dlg.show(getSupportFragmentManager(), "dlg");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lesson);
        initView();
    }

    private void editOneLesson() {
        mRepository.editLesson(mLessonId, mLearner.getId());
    }

    private void editManyLessons() {
        if (rbOne.isChecked()) {
            mRepository.editOneLesson(mLessonId, mLearner.getId());
        } else {
            mRepository.editManyLessons(mLessonId, mLearner.getId());
        }
    }

    private void initView() {
        App.getComponent().inject(this);
        ButterKnife.bind(this);
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
        mLearner = lesson.getLearner();
        tilLearnerName.getEditText().setText(mLearner.getName());
        rgRepeat.setVisibility(lesson.getStudy().getDate() == null ? View.GONE : View.VISIBLE);
        rbOne.setChecked(true);
    }

}
