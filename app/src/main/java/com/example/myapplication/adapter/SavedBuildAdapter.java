package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.SavedBuild;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SavedBuildAdapter extends RecyclerView.Adapter<SavedBuildAdapter.BuildViewHolder> {

    private List<SavedBuild> buildList;
    private OnBuildClickListener listener;

    public interface OnBuildClickListener {
        void onBuildClick(SavedBuild build);
        void onDeleteClick(SavedBuild build);
    }

    public SavedBuildAdapter(List<SavedBuild> buildList, OnBuildClickListener listener) {
        this.buildList = buildList;
        this.listener = listener;
    }

    public void updateData(List<SavedBuild> newBuilds) {
        this.buildList = newBuilds;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BuildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_build, parent, false);
        return new BuildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuildViewHolder holder, int position) {
        SavedBuild build = buildList.get(position);
        holder.tvName.setText(build.getBuildName());
        Date date = new Date(build.getSavedAt());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.US);
        holder.tvDate.setText("Saved on: " + sdf.format(date));

        holder.itemView.setOnClickListener(v -> listener.onBuildClick(build));

        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(build));
    }

    @Override
    public int getItemCount() {
        return buildList != null ? buildList.size() : 0;
    }

    public static class BuildViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate;
        ImageView btnDelete;

        public BuildViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_build_name);
            tvDate = itemView.findViewById(R.id.tv_build_date);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}