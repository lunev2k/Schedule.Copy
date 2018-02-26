package com.lunev2k.schedule.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.lunev2k.schedule.R;
import com.lunev2k.schedule.utils.Constants;

public class PaymentLessonFragment extends DialogFragment {

    PaymentLessonFragmentListener listener;
    private int mPayment;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (PaymentLessonFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PaymentLessonFragmentListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mPayment = bundle.getInt(Constants.PAYMENT_LEARNER);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_payment, null);
        TextInputLayout textInputLayout = v.findViewById(R.id.tilPaymentLesson);
        textInputLayout.getEditText().setText(String.valueOf(mPayment));
        builder.setView(v)
                .setMessage(R.string.title_payment_lesson)
                .setPositiveButton(android.R.string.ok, (dialog, id) ->
                {
                    String result = textInputLayout.getEditText().getText().toString();
                    listener.onPaymentLesson(Integer.valueOf(result));
                })
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> {
                });
        return builder.create();
    }

    public interface PaymentLessonFragmentListener {
        void onPaymentLesson(int pay);
    }
}
