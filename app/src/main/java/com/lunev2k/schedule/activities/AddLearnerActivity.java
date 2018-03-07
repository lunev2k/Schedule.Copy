package com.lunev2k.schedule.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lunev2k.schedule.App;
import com.lunev2k.schedule.R;
import com.lunev2k.schedule.database.Repository;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class AddLearnerActivity extends AppCompatActivity {

    @Inject
    Repository mRepository;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tilLearnerContact)
    TextInputLayout tilLearnerContact;
    @BindView(R.id.tilLearnerName)
    TextInputLayout tilLearnerName;
    @BindView(R.id.tilLearnerPay)
    TextInputLayout tilLearnerPay;
    private long mIdContact;

    @OnClick(R.id.fabAddLearnerDone)
    public void addLearnerDoneClick(View view) {
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
        mRepository.addLearner(mIdContact, learnerName, Integer.parseInt(learnerPay));
        finish();
    }

    @OnClick(R.id.etLearnerContact)
    public void learnerContactClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }

    @OnTextChanged(R.id.etLearnerName)
    public void changedTextOnLearnerName() {
        tilLearnerName.setError("");
    }

    @OnTextChanged(R.id.etLearnerPay)
    public void changedTextOnLearnerPay() {
        tilLearnerPay.setError("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.PhoneLookup.DISPLAY_NAME};
            Cursor cursor = getContentResolver().query(contactUri, projection,
                    null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                mIdContact = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                tilLearnerContact.getEditText().setText(name);
                tilLearnerName.getEditText().setText(name);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_learner);
        App.getComponent().inject(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
