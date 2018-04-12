package com.lunev2k.schedule.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lunev2k.schedule.R;
import com.lunev2k.schedule.database.DbHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE_WRITE = 1;
    private static final int PERMISSION_REQUEST_CODE_READ = 2;

    @BindView(R.id.buttonExport)
    Button btnExport;

    @BindView(R.id.buttonImport)
    Button btnImport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.buttonExport)
    public void exportDB(View view) {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                PERMISSION_REQUEST_CODE_WRITE);
    }

    @OnClick(R.id.buttonImport)
    public void importDB(View view) {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                PERMISSION_REQUEST_CODE_READ);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_WRITE && grantResults.length == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportData();
            }
        }
        if (requestCode == PERMISSION_REQUEST_CODE_READ && grantResults.length == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                importData();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void importData() {
        try {
            btnImport.setEnabled(false);
            btnExport.setEnabled(false);
            try {
                File file = new File(Environment.getDataDirectory() + "/data/" +
                        getApplicationContext().getPackageName() + "/databases/" + DbHelper.DATABASE_NAME);
                File fileRestore = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DbHelper.DATABASE_NAME);
                FileChannel sc = null;
                FileChannel dc = null;
                try {
                    sc = new FileInputStream(fileRestore).getChannel();
                    dc = new FileOutputStream(file).getChannel();
                    dc.transferFrom(sc, 0, sc.size());
                } finally {
                    if (sc != null) {
                        sc.close();
                    }
                    if (dc != null) {
                        dc.close();
                    }
                }
                Toast.makeText(getApplicationContext(), R.string.text_restore_successful,
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.text_restore_failed) + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        } finally {
            btnImport.setEnabled(true);
            btnExport.setEnabled(true);
        }
    }

    private void exportData() {
        Log.d(this.getClass().getName(), "exportData");
        try {
            btnImport.setEnabled(false);
            btnExport.setEnabled(false);
            try {
                File file = new File(Environment.getDataDirectory() + "/data/" +
                        getApplicationContext().getPackageName() + "/databases/" + DbHelper.DATABASE_NAME);
                File fileBackup = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DbHelper.DATABASE_NAME);
                FileChannel sc = null;
                FileChannel dc = null;
                try {
                    sc = new FileInputStream(file).getChannel();
                    dc = new FileOutputStream(fileBackup).getChannel();
                    dc.transferFrom(sc, 0, sc.size());
                } finally {
                    if (sc != null) {
                        sc.close();
                    }
                    if (dc != null) {
                        dc.close();
                    }
                }
                Toast.makeText(getApplicationContext(), R.string.text_backup_successful,
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.text_backup_failed) + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        } finally {
            btnImport.setEnabled(true);
            btnExport.setEnabled(true);
        }
    }
}
