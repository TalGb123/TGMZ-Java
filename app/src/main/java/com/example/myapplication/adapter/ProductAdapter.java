package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.databinding.ItemProductBinding;
import com.example.myapplication.model.Product;
import java.util.List;
import android.widget.Filter;
import android.widget.Filterable;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {

    private List<Product> productList;
    private List<Product> productListFull;
    private final OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.productListFull = new ArrayList<>(productList);
        this.listener = listener;
    }

    public void updateData(List<Product> newProducts) {
        this.productList = newProducts;
        this.productListFull = new ArrayList<>(newProducts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.binding.tvProductName.setText(product.getName());
        holder.binding.tvProductPrice.setText(String.format(java.util.Locale.US, "₪%.2f", product.getPrice()));

        Glide.with(holder.itemView.getContext())
                .load(product.getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.binding.ivProductImage);

        android.util.TypedValue typedValue = new android.util.TypedValue();
        holder.itemView.getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorSurfaceVariant, typedValue, true);
        int defaultColor = typedValue.data;

        holder.itemView.getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnSurfaceVariant, typedValue, true);
        int defaultTextColor = typedValue.data;

        if (!product.isCompatible()) {
            holder.binding.getRoot().setCardBackgroundColor(android.graphics.Color.argb(128, 255, 0, 0));
            holder.binding.getRoot().setAlpha(0.5f);

            holder.binding.tvCompatibilityReason.setVisibility(android.view.View.VISIBLE);
            holder.binding.tvCompatibilityReason.setText(product.getCompatibilityReason());
            holder.binding.tvCompatibilityReason.setTextColor(android.graphics.Color.RED);
        }
        else if (product.isWarning()) {
            holder.binding.getRoot().setCardBackgroundColor(android.graphics.Color.argb(217, 255, 165, 0));
            holder.binding.getRoot().setAlpha(0.85f);
            holder.binding.tvCompatibilityReason.setVisibility(android.view.View.VISIBLE);
            holder.binding.tvCompatibilityReason.setText(product.getCompatibilityReason());
            holder.binding.tvCompatibilityReason.setTextColor(android.graphics.Color.parseColor("#CC8400"));
        }
        else {
            holder.binding.getRoot().setCardBackgroundColor(defaultColor);
            holder.binding.getRoot().setAlpha(1.0f);
            holder.binding.tvCompatibilityReason.setVisibility(android.view.View.GONE);
            holder.binding.tvCompatibilityReason.setTextColor(defaultTextColor);
        }

        holder.itemView.setOnClickListener(v -> listener.onProductClick(product));
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    private final Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(productListFull);
            }
            else {
                String pattern = constraint.toString().toLowerCase().trim();
                for (Product item : productListFull) {
                    if (item.getName().toLowerCase().contains(pattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productList.clear();
            productList.addAll((List<Product>) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        final ItemProductBinding binding;

        public ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}