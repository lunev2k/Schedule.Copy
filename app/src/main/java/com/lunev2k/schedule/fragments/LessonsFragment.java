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

import com.lunev2k.schedule.App;
import com.lunev2k.schedule.R;
import com.lunev2k.schedule.adapters.LessonsAdapter;
import com.lunev2k.schedule.database.DatabaseRepository;
import com.lunev2k.schedule.model.LessonsItem;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LessonsFragment extends Fragment {

    @Inject
    DatabaseRepository mRepository;

    @BindView(R.id.rvLessons)
    RecyclerView rvLessons;

    private OnLessonItemClickListener listener;

    public LessonsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);
        ButterKnife.bind(this, view);
        App.getComponent().inject(this);
        rvLessons.setLayoutManager(new LinearLayoutManager(getContext()));
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
        if (context instanceof OnLessonItemClickListener) {
            listener = (OnLessonItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLessonItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void loadData() {
        LessonsAdapter adapter = new LessonsAdapter(mRepository.getLessons(),
                item -> listener.onLessonItemClickListener(item));
        rvLessons.setAdapter(adapter);
    }

    public interface OnLessonItemClickListener {
        void onLessonItemClickListener(LessonsItem lesson);
    }
}
