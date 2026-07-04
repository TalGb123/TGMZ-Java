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
                    PcBuild current = currentBuild.getValue();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Product product = document.toObject(Product.class);
                        product.setId(document.getId());

                        // Evaluate compatibility before adding to the list
                        if (current != null) {
                            com.example.myapplication.utils.CompatibilityEngine.evaluate(product, current);
                        }

                        productsList.add(product);
                    }

                    // Sort: Compatible first, Warnings second, Errors last
                    productsList.sort((p1, p2) -> {
                        int score1 = !p1.isCompatible() ? 3 : (p1.isWarning() ? 2 : 1);
                        int score2 = !p2.isCompatible() ? 3 : (p2.isWarning() ? 2 : 1);
                        return Integer.compare(score1, score2);
                    });

                    categoryProducts.setValue(productsList);
                    isLoading.setValue(false);
                })
                .addOnFailureListener(e -> {
                    Log.e("ShopViewModel", "Error fetching products from Firestore", e);
                    isLoading.setValue(false);
                });
    }

    public void clearSlot(String categoryId) {
        PcBuild build = currentBuild.getValue();
        if (build == null) return;

        switch(categoryId) {
            case "CPU": build.setCpu(null); break;
            case "CPUCooler": build.setCpuCooler(null); break;
            case "Motherboard": build.setMotherboard(null); break;
            case "Memory": build.setRam(null); break;
            case "Storage": build.setStorage(null); break;
            case "VideoCard": build.setGpu(null); break;
            case "Case": build.setPcCase(null); break;
            case "PowerSupply": build.setPsu(null); break;
        }
        recalculateBuildCompatibility(build);
        currentBuild.setValue(build);
    }

    private void recalculateBuildCompatibility(PcBuild build) {
        // This makes selected parts check themselves against the rest of the build
        if (build.getCpu() != null) com.example.myapplication.utils.CompatibilityEngine.evaluate(build.getCpu(), build);
        if (build.getCpuCooler() != null) com.example.myapplication.utils.CompatibilityEngine.evaluate(build.getCpuCooler(), build);
        if (build.getMotherboard() != null) com.example.myapplication.utils.CompatibilityEngine.evaluate(build.getMotherboard(), build);
        if (build.getRam() != null) com.example.myapplication.utils.CompatibilityEngine.evaluate(build.getRam(), build);
        if (build.getStorage() != null) com.example.myapplication.utils.CompatibilityEngine.evaluate(build.getStorage(), build);
        if (build.getGpu() != null) com.example.myapplication.utils.CompatibilityEngine.evaluate(build.getGpu(), build);
        if (build.getPcCase() != null) com.example.myapplication.utils.CompatibilityEngine.evaluate(build.getPcCase(), build);
        if (build.getPowerSupply() != null) com.example.myapplication.utils.CompatibilityEngine.evaluate(build.getPowerSupply(), build);
    }
}