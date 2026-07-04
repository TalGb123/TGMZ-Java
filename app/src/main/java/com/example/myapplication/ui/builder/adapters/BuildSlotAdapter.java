package com.example.myapplication.ui.builder.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.BuildSlot;

import java.util.List;

public class BuildSlotAdapter extends RecyclerView.Adapter<BuildSlotAdapter.SlotViewHolder> {

    private List<BuildSlot> slotList;
    private OnSlotClickListener listener;

    public interface OnSlotClickListener {
        void onSlotClick(BuildSlot slot);
    }

    public BuildSlotAdapter(List<BuildSlot> slotList, OnSlotClickListener listener) {
        this.slotList = slotList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_build_slot, parent, false);
        return new SlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewHolder holder, int position) {
        BuildSlot slot = slotList.get(position);
        holder.tvCategoryName.setText(slot.getCategoryName());

        if (slot.getSelectedProduct() != null) {
            holder.tvSelectedProduct.setText(slot.getSelectedProduct().getName());
            holder.tvSelectedProduct.setTextColor(Color.WHITE);

            Glide.with(holder.itemView.getContext())
                    .load(slot.getSelectedProduct().getImage())
                    .into(holder.ivIcon);

            holder.ivIcon.setColorFilter(null);

        }
        else {
            holder.tvSelectedProduct.setText("Tap to select a component...");
            holder.tvSelectedProduct.setTextColor(Color.parseColor("#888888"));
            holder.ivIcon.setImageResource(slot.getIconResId());
            holder.ivIcon.setColorFilter(Color.parseColor("#888888"));
        }

        holder.itemView.setOnClickListener(v -> listener.onSlotClick(slot));
    }

    @Override
    public int getItemCount() {
        return slotList.size();
    }

    public static class SlotViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName, tvSelectedProduct;
        ImageView ivIcon;

        public SlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            tvSelectedProduct = itemView.findViewById(R.id.tv_selected_product);
            ivIcon = itemView.findViewById(R.id.iv_slot_image);
        }
    }
}