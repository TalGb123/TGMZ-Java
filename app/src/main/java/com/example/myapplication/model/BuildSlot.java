package com.example.myapplication.model;

public class BuildSlot {
    private String categoryId;
    private String categoryName;
    private int iconResId;
    private Product selectedProduct;

    public BuildSlot(String categoryId, String categoryName, int iconResId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.iconResId = iconResId;
    }

    public String getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public int getIconResId() { return iconResId; }

    public Product getSelectedProduct() { return selectedProduct; }
    public void setSelectedProduct(Product selectedProduct) { this.selectedProduct = selectedProduct; }
}