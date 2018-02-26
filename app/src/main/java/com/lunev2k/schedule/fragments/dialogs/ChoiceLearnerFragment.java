package com.lunev2k.schedule.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import com.lunev2k.schedule.App;
import com.lunev2k.schedule.R;
import com.lunev2k.schedule.database.Repository;
import com.lunev2k.schedule.model.Learner;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ChoiceLearnerFragment extends DialogFragment {

    @Inject
    Repository mRepository;

    NoticeChoiceLearnerDialogListener listener;
    private long id;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (NoticeChoiceLearnerDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeChoiceLearnerDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        App.getComponent().inject(this);
        final List<Learner>[] list = new List[]{new ArrayList<>()};
        list[0] = mRepository.getLearners();
        ArrayAdapter<Learner> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_singlechoice, list[0]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_choice_learner)
                .setSingleChoiceItems(adapter, -1, (dialogInterface, item) -> {
                    id = adapter.getItem(item).getId();
                })
                .setPositiveButton(android.R.string.ok, (dialogInterface, item) -> {
                    listener.onChoiceLearnerDialog(id);
                })
                .setNegativeButton(android.R.string.cancel, (dialogInterface, item) -> {

                });
        return builder.create();
    }

    public interface NoticeChoiceLearnerDialogListener {
        void onChoiceLearnerDialog(long id);
    }
}

