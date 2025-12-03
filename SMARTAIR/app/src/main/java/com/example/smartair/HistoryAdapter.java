package com.example.smartair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final List<HistoryItem> items;

    public HistoryAdapter(List<HistoryItem> items) {
        this.items = items;
    }

    @NonNull @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_history_item, parent, false);
        return new HistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItem item = items.get(position);

        if (item.type.equals(HistoryItem.TYPE_ZONE)) {
            holder.title.setText("Zone Change: " + item.detail);
        } else {
            holder.title.setText("Incident: " + item.detail);
        }
        holder.time.setText(convertTime(item.timestamp));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView title, time;
        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.historyText);
            time = itemView.findViewById(R.id.historyTime);
        }
    }

    private String convertTime(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d — h:mm a", Locale.getDefault());
        return sdf.format(new Date(millis));
    }
}
