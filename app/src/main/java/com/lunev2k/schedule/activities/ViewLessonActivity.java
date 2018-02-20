package com.lunev2k.schedule.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.lunev2k.schedule.R;
import com.lunev2k.schedule.database.DatabaseRepository;
import com.lunev2k.schedule.database.Repository;
import com.lunev2k.schedule.model.Lesson;
import com.lunev2k.schedule.utils.DateTimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewLessonActivity extends AppCompatActivity {

    public static final String LESSON_ID = "lesson_id";
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

    @OnClick(R.id.fabLessonCost)
    public void lessonCostClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lesson);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        long id = intent.getLongExtra(LESSON_ID, 0);
        if (id == 0) {
            finish();
        }
        fillData(id);
    }

    private void fillData(long id) {
        Repository repository = new DatabaseRepository(this);
        Lesson lesson = repository.getLesson(id);
        tvLessonDatetime.setText(DateTimeUtil.getFormatDateTime(lesson.getDate()));
        tvLearnerName.setText(lesson.getLearner().getName());
        tvLessonCost.setText(lesson.getCost() > 0 ? String.valueOf(lesson.getCost()) : "-");
        if (lesson.getStudy().getDate() != null) {
            tvRepeat.setText(String.format("Повторение до %s", DateTimeUtil.getFormatDate(lesson.getStudy().getDate())));
        }
    }

}
