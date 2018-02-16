package com.lunev2k.schedule.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lunev2k.schedule.R;
import com.lunev2k.schedule.adapters.LearnersAdapter;
import com.lunev2k.schedule.database.DatabaseRepository;
import com.lunev2k.schedule.database.Repository;
import com.lunev2k.schedule.model.Learner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LearnersFragment extends Fragment {

    @BindView(R.id.rvLearners)
    RecyclerView rvLearners;

    private OnLearnerItemClickListener listener;

    public LearnersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learners, container, false);
        ButterKnife.bind(this, view);
        rvLearners.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLearnerItemClickListener) {
            listener = (OnLearnerItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLearnerItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void loadData() {
        Repository repository = new DatabaseRepository(getContext());
        LearnersAdapter adapter = new LearnersAdapter(repository.getLearners(),
                item -> listener.onLearnerItemClick(item));
        rvLearners.setAdapter(adapter);
    }

    public interface OnLearnerItemClickListener {
        void onLearnerItemClick(Learner learner);
    }
}
