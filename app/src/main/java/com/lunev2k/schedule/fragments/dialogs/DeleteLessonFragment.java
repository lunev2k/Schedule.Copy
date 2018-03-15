package com.lunev2k.schedule.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.lunev2k.schedule.App;
import com.lunev2k.schedule.R;
import com.lunev2k.schedule.database.Repository;

import javax.inject.Inject;

public class DeleteLessonFragment extends DialogFragment {

    @Inject
    Repository mRepository;

    NoticeDeteleLessonDialogListener listener;
    private long id;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (NoticeDeteleLessonDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDeteleLessonDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        App.getComponent().inject(this);
        final CharSequence[] charSequence = new CharSequence[]{
                getString(R.string.title_current_lesson),
                getString(R.string.title_all_lessons)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_delete_lesson)
                .setSingleChoiceItems(charSequence, 0, (dialog, which) -> id = which)
                .setPositiveButton(android.R.string.ok, (dialogInterface, item) -> {
                    if (id >= 0) {
                        listener.onDeteleLessonDialogListener(id);
                    }
                })
                .setNegativeButton(android.R.string.cancel, (dialogInterface, item) -> {

                });
        return builder.create();
    }

    public interface NoticeDeteleLessonDialogListener {
        void onDeteleLessonDialogListener(long id);
    }
}

