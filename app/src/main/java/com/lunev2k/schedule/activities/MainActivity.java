package com.lunev2k.schedule.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.lunev2k.schedule.App;
import com.lunev2k.schedule.R;
import com.lunev2k.schedule.fragments.LearnersFragment;
import com.lunev2k.schedule.fragments.LessonsFragment;
import com.lunev2k.schedule.fragments.TotalsFragment;
import com.lunev2k.schedule.model.LearnersItem;
import com.lunev2k.schedule.model.LessonsItem;
import com.lunev2k.schedule.model.TotalItem;
import com.lunev2k.schedule.utils.Constants;
import com.lunev2k.schedule.utils.PrefsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        LessonsFragment.OnLessonItemClickListener,
        TotalsFragment.OnTotalItemClickListener,
        LearnersFragment.OnLearnerItemClickListener {

    private static final String NAVIGATION_ID = "navigationId";
    @Inject
    PrefsUtils mPrefsUtils;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private boolean visibleAddLearner;
    private boolean visibleAddLesson;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_lessons:
                    visibleAddLearner = false;
                    visibleAddLesson = true;
                    showFragment(new LessonsFragment());
                    invalidateOptionsMenu();
                    return true;
                case R.id.navigation_totals:
                    visibleAddLearner = false;
                    visibleAddLesson = false;
                    invalidateOptionsMenu();
                    showFragment(new TotalsFragment());
                    return true;
                case R.id.navigation_learners:
                    visibleAddLearner = true;
                    visibleAddLesson = false;
                    invalidateOptionsMenu();
                    showFragment(new LearnersFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_learner:
                startActivity(new Intent(this, AddLearnerActivity.class));
                break;
            case R.id.action_add_lesson:
                startActivity(new Intent(this, AddLessonActivity.class));
                break;
            case R.id.action_range_date:
                startActivity(new Intent(this, RangeDateActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_add_learner).setVisible(visibleAddLearner);
        menu.findItem(R.id.action_add_lesson).setVisible(visibleAddLesson);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onLearnerItemClick(LearnersItem learner) {
        Intent intent = new Intent(this, ViewLearnerActivity.class);
        intent.putExtra(Constants.LEARNER_ID, learner.getId());
        startActivity(intent);
    }

    @Override
    public void onLessonItemClickListener(LessonsItem lesson) {
        Intent intent = new Intent(this, ViewLessonActivity.class);
        intent.putExtra(Constants.LESSON_ID, lesson.getId());
        startActivity(intent);
    }

    @Override
    public void onTotalItemClickListener(TotalItem total) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.getComponent().inject(this);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        int id = mPrefsUtils.getInt(NAVIGATION_ID);
        navigation.getMenu().getItem(id).setChecked(true);
        navigation.getMenu().performIdentifierAction(getNavigationId(id), 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        int id = getSelectedItem();
        mPrefsUtils.putInt(NAVIGATION_ID, id);
    }

    private void initView() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private int getSelectedItem() {
        Menu menu = navigation.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).isChecked()) {
                return i;
            }
        }
        return 0;
    }

    private int getNavigationId(int id) {
        switch (id) {
            case 0:
                return R.id.navigation_lessons;
            case 1:
                return R.id.navigation_totals;
            case 2:
                return R.id.navigation_learners;
            default:
                return R.id.navigation_lessons;
        }
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }
}
