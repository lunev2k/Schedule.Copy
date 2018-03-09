package com.lunev2k.schedule.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.lunev2k.schedule.App;
import com.lunev2k.schedule.R;
import com.lunev2k.schedule.adapters.DayAdapter;
import com.lunev2k.schedule.database.Repository;
import com.lunev2k.schedule.model.LessonsItem;
import com.lunev2k.schedule.utils.Constants;
import com.lunev2k.schedule.utils.DateTimeUtil;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DayActivity extends AppCompatActivity {

    @Inject
    Repository mRepository;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvDay)
    RecyclerView recyclerView;
    private Date mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        ButterKnife.bind(this);
        App.getComponent().inject(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<LessonsItem> items = mRepository.getLessonsByDate(mDate);
        DayAdapter adapter = new DayAdapter(items);
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        mDate = new Date(intent.getLongExtra(Constants.DAY_ID, 0));
        setTitle(DateTimeUtil.getFormatDate(mDate));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
