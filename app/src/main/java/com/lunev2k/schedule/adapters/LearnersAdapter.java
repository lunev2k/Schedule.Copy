package com.lunev2k.schedule.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lunev2k.schedule.R;
import com.lunev2k.schedule.model.LearnersItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LearnersAdapter extends RecyclerView.Adapter<LearnersAdapter.ViewHolder> {

    private List<LearnersItem> list;
    private OnItemClickListener listener;

    public LearnersAdapter(List<LearnersItem> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_learners, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(list.get(position), listener);
        holder.tvLearnerName.setText(list.get(position).getName());
        holder.tvLearnerCost.setText(String.valueOf(list.get(position).getPay()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(LearnersItem item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvLearnerName)
        TextView tvLearnerName;
        @BindView(R.id.tvLearnerCost)
        TextView tvLearnerCost;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(LearnersItem learner, OnItemClickListener listener) {
            itemView.setOnClickListener(view -> listener.onItemClick(learner));
        }
    }
}
