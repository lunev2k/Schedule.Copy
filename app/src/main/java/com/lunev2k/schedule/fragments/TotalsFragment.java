package com.lunev2k.schedule.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lunev2k.schedule.R;

public class TotalsFragment extends Fragment {

    private OnTotalsFragmentItemClickListener listener;

    public TotalsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_totals, container, false);
    }

/*    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTotalsFragmentItemClickListener) {
            listener = (OnTotalsFragmentItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTotalsFragmentItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnTotalsFragmentItemClickListener {
        // TODO: Update argument type and name
        void onTotalsFragmentItemClickListener(Uri uri);
    }
}
