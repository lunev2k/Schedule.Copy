package com.lunev2k.schedule.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lunev2k.schedule.App;
import com.lunev2k.schedule.R;
import com.lunev2k.schedule.database.DatabaseRepository;
import com.lunev2k.schedule.model.Learner;
import com.lunev2k.schedule.utils.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EditLearnerActivity extends AppCompatActivity {

    @Inject
    DatabaseRepository mRepository;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fabEditLearnerDone)
    FloatingActionButton fabEditLearnerDone;
    @BindView(R.id.tilLearnerName)
    TextInputLayout tilLearnerName;
    @BindView(R.id.tilLearnerPay)
    TextInputLayout tilLearnerPay;
    private long mId;

    @OnTextChanged(R.id.etLearnerName)
    public void changedTextOnLearnerName() {
        tilLearnerName.setError("");
    }

    @OnTextChanged(R.id.etLearnerPay)
    public void changedTextOnLearnerPay() {
        tilLearnerPay.setError("");
    }

    @OnClick(R.id.fabEditLearnerDone)
    public void editLearnerDoneClick(View view) {
        String learnerName = tilLearnerName.getEditText().getText().toString();
        if (learnerName.isEmpty()) {
            tilLearnerName.setError(getString(R.string.error_learner_name));
            return;
        }
        String learnerPay = tilLearnerPay.getEditText().getText().toString();
        if (learnerPay.isEmpty()) {
            tilLearnerPay.setError(getString(R.string.error_learner_pay));
            return;
        }
        mRepository.editLearner(mId, learnerName, Integer.valueOf(learnerPay));
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_learner);
        ButterKnife.bind(this);
        App.getComponent().inject(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mId = getIntent().getLongExtra(Constants.LEARNER_ID, 0);
        Learner learner = mRepository.getLearner(mId);
        tilLearnerName.getEditText().setText(learner.getName());
        tilLearnerPay.getEditText().setText(String.valueOf(learner.getPay()));
    }

}
