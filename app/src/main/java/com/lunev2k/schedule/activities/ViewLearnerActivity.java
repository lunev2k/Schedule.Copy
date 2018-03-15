package com.lunev2k.schedule.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.lunev2k.schedule.App;
import com.lunev2k.schedule.R;
import com.lunev2k.schedule.database.Repository;
import com.lunev2k.schedule.model.Learner;
import com.lunev2k.schedule.utils.Constants;
import com.lunev2k.schedule.utils.PrefsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewLearnerActivity extends AppCompatActivity {

    @Inject
    Repository mRepository;
    @Inject
    PrefsUtils mPrefsUtils;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPay)
    TextView tvPay;
    private long mLearnerId;

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
                mPrefsUtils.putLong(Constants.LEARNER_ID, mLearnerId);
                Intent intent = new Intent(this, EditLearnerActivity.class);
                intent.putExtra(Constants.LEARNER_ID, mLearnerId);
                startActivityForResult(intent, 1);
                break;
            case R.id.action_delete_learner:
                deleteLearner();
                break;
            case R.id.action_call_learner:
                callLearner();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }

    private void callLearner() {
        long contactID = mRepository.getLearner(mLearnerId).getContact();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactID));
        intent.setData(uri);
        startActivity(intent);
    }

    private void deleteLearner() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_delete_learner)
                .setMessage(R.string.message_delete_learner)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                    mRepository.deleteLearner(mLearnerId);
                    finish();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> {
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mLearnerId = getIntent().getLongExtra(Constants.LEARNER_ID, 0);
        if (mLearnerId == 0) {
            mLearnerId = mPrefsUtils.getLong(Constants.LEARNER_ID);
        }
        Learner learner = mRepository.getLearner(mLearnerId);
        tvName.setText(learner.getName());
        tvPay.setText(String.valueOf(learner.getPay()));
    }
}
