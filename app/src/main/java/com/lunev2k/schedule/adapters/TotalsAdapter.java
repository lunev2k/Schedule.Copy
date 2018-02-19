package com.lunev2k.schedule.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lunev2k.schedule.R;
import com.lunev2k.schedule.model.TotalItem;
import com.lunev2k.schedule.utils.DateTimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TotalsAdapter extends RecyclerView.Adapter<TotalsAdapter.ViewHolder> {

    private List<TotalItem> list;
    private OnItemClickListener listener;

    public TotalsAdapter(List<TotalItem> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_totals, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(list.get(position), listener);
        holder.tvDate.setText(DateTimeUtil.getFormatDate(list.get(position).getDate()));
        holder.tvCount.setText(list.get(position).getCount() > 0 ? String.valueOf(list.get(position).getCount()) : "");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(TotalItem item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvCount)
        TextView tvCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(TotalItem total, OnItemClickListener listener) {
            itemView.setOnClickListener(view -> listener.onItemClick(total));
        }
    }
}
