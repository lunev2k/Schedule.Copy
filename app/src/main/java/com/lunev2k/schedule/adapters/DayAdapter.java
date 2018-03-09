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

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    private List<LessonsItem> list;

    public DayAdapter(List<LessonsItem> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTime.setText(DateTimeUtil.getFormatTime(list.get(position).getDate()));
        holder.tvName.setText(list.get(position).getName());
        holder.tvCost.setText(list.get(position).getCost() > 0 ? String.valueOf(list.get(position).getCost()) : "-");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvCost)
        TextView tvCost;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
