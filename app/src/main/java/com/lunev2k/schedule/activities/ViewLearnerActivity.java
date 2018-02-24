package com.lunev2k.schedule.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.lunev2k.schedule.App;
import com.lunev2k.schedule.R;
import com.lunev2k.schedule.database.DatabaseRepository;
import com.lunev2k.schedule.model.Learner;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewLearnerActivity extends AppCompatActivity {

    public static final String LEARNER_ID = "learner_id";
    @Inject
    DatabaseRepository mRepository;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPay)
    TextView tvPay;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_learner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_edit_learner:
                break;
            case R.id.action_delete_learner:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_learner);
        App.getComponent().inject(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        long id = getIntent().getLongExtra(LEARNER_ID, 0);
        if (id == 0) {
            finish();
        }
        Learner learner = mRepository.getLearner(id);
        tvName.setText(learner.getName());
        tvPay.setText(String.valueOf(learner.getPay()));
    }

}
