package com.example.myapplication.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.PcBuild;
import com.example.myapplication.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShopViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<PcBuild> currentBuild = new MutableLiveData<>(new PcBuild());

    private final MutableLiveData<List<Product>> categoryProducts = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<PcBuild> getCurrentBuild() {
        return currentBuild;
    }

    public LiveData<List<Product>> getCategoryProducts() {
        return categoryProducts;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void setCpu(Product cpu) {
        PcBuild build = currentBuild.getValue();
        if (build != null) {
            build.setCpu(cpu);
            currentBuild.setValue(build);
        }
    }

    public void setCpuCooler(Product cpuCooler) {
        PcBuild build = currentBuild.getValue();
        if (build != null) {
            build.setCpuCooler(cpuCooler);
            currentBuild.setValue(build);
        }
    }

    public void setMotherboard(Product motherboard) {
        PcBuild build = currentBuild.getValue();
        if (build != null) {
            build.setMotherboard(motherboard);
            currentBuild.setValue(build);
        }
    }

    public void setRam(Product ram) {
        PcBuild build = currentBuild.getValue();
        if (build != null) {
            build.setRam(ram);
            currentBuild.setValue(build);
        }
    }

    public void setStorage(Product storage) {
        PcBuild build = currentBuild.getValue();
        if (build != null) {
            build.setStorage(storage);
            currentBuild.setValue(build);
        }
    }

    public void setGpu(Product gpu) {
        PcBuild build = currentBuild.getValue();
        if (build != null) {
            build.setGpu(gpu);
            currentBuild.setValue(build);
        }
    }

    public void setPsu(Product psu) {
        PcBuild build = currentBuild.getValue();
        if (build != null) {
            build.setPsu(psu);
            currentBuild.setValue(build);
        }
    }

    public void setPcCase(Product pcCase) {
        PcBuild build = currentBuild.getValue();
        if (build != null) {
            build.setPcCase(pcCase);
            currentBuild.setValue(build);
        }
    }

    public void fetchProductsByCategory(String categoryName) {
        isLoading.setValue(true);
        db.collection("products")
                .whereEqualTo("category", categoryName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Product> productsList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Product product = document.toObject(Product.class);
                        product.setId(document.getId());
                        productsList.add(product);
                    }

                    categoryProducts.setValue(productsList);
                    isLoading.setValue(false);
                })
                .addOnFailureListener(e -> {
                    Log.e("ShopViewModel", "Error fetching products from Firestore", e);
                    isLoading.setValue(false);
                });
    }
}