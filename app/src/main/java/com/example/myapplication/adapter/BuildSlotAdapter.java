package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemBuildSlotBinding;
import com.example.myapplication.model.BuildSlot;
import com.example.myapplication.model.Product;

import java.util.List;

public class BuildSlotAdapter extends RecyclerView.Adapter<BuildSlotAdapter.SlotViewHolder> {

    private final List<BuildSlot> slotList;
    private final OnSlotClickListener listener;

    public interface OnSlotClickListener {
        void onSlotClick(BuildSlot slot);
        void onClearClick(BuildSlot slot);
    }

    public BuildSlotAdapter(List<BuildSlot> slotList, OnSlotClickListener listener) {
        this.slotList = slotList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBuildSlotBinding binding = ItemBuildSlotBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new SlotViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewHolder holder, int position) {
        BuildSlot slot = slotList.get(position);
        holder.binding.tvCategoryName.setText(slot.getCategoryName());

        if (slot.getSelectedProduct() != null) {
            Product p = slot.getSelectedProduct();
            holder.binding.tvSelectedProduct.setText(p.getName());
            holder.binding.tvSelectedProduct.setTextColor(holder.itemView.getContext().getColor(android.R.color.tab_indicator_text));

            Glide.with(holder.itemView.getContext()).load(p.getImage()).into(holder.binding.ivSlotImage);
            holder.binding.ivSlotImage.setColorFilter(null);

            holder.binding.btnClearSlot.setVisibility(View.VISIBLE);
            holder.binding.ivActionIcon.setVisibility(View.GONE);

            if (!p.isCompatible()) {
                holder.binding.ivAlertIcon.setVisibility(View.VISIBLE);
                holder.binding.ivAlertIcon.setColorFilter(android.graphics.Color.RED);
            }
            else if (p.isWarning()) {
                holder.binding.ivAlertIcon.setVisibility(View.VISIBLE);
                holder.binding.ivAlertIcon.setColorFilter(android.graphics.Color.parseColor("#CC8400"));
            }
            else {
                holder.binding.ivAlertIcon.setVisibility(View.GONE);
            }

            holder.binding.btnClearSlot.setOnClickListener(v -> listener.onClearClick(slot));

        }
        else {
            holder.binding.tvSelectedProduct.setText(holder.itemView.getContext().getString(R.string.empty_slot_text));
            holder.binding.tvSelectedProduct.setTextColor(holder.itemView.getContext().getColor(android.R.color.darker_gray));
            holder.binding.ivSlotImage.setImageResource(slot.getIconResId());
            holder.binding.ivSlotImage.setColorFilter(holder.itemView.getContext().getColor(android.R.color.darker_gray));
            holder.binding.btnClearSlot.setVisibility(View.GONE);
            holder.binding.ivActionIcon.setVisibility(View.VISIBLE);
            holder.binding.ivAlertIcon.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onSlotClick(slot));
    }

    @Override
    public int getItemCount() {
        return slotList.size();
    }

    public static class SlotViewHolder extends RecyclerView.ViewHolder {
        final ItemBuildSlotBinding binding;

        public SlotViewHolder(ItemBuildSlotBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}