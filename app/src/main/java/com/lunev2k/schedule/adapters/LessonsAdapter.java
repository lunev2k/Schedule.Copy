package com.lunev2k.schedule.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lunev2k.schedule.R;
import com.lunev2k.schedule.model.LessonsItem;
import com.lunev2k.schedule.utils.DateTimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.ViewHolder> {

    private List<LessonsItem> list;
    private OnItemClickListener listener;

    public LessonsAdapter(List<LessonsItem> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lessons, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(list.get(position), listener);
        holder.tvLessonDate.setText(DateTimeUtil.getFormatDate(list.get(position).getDate()));
        holder.tvLearnerName.setText(list.get(position).getName());
        holder.tvLessonCost.setText(String.valueOf(list.get(position).getCost()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(LessonsItem item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvLessonDate)
        TextView tvLessonDate;
        @BindView(R.id.tvLearnerName)
        TextView tvLearnerName;
        @BindView(R.id.tvLessonCost)
        TextView tvLessonCost;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(LessonsItem lesson, OnItemClickListener listener) {
            itemView.setOnClickListener(view -> listener.onItemClick(lesson));
        }
    }
}
