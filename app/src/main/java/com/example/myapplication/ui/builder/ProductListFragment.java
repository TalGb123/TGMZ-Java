package com.example.myapplication.ui.builder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.FragmentProductListBinding;
import com.example.myapplication.ui.builder.adapters.ProductAdapter;
import com.example.myapplication.viewmodel.ShopViewModel;

import java.util.ArrayList;

public class ProductListFragment extends Fragment {

    private FragmentProductListBinding binding;
    private ShopViewModel shopViewModel;
    private ProductAdapter adapter;
    private String categoryName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            categoryName = getArguments().getString("categoryName");
            binding.tvCategoryTitle.setText("Select " + categoryName);
        }

        binding.btnBack.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });

        shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);

        setupRecyclerView();
        setupObservers();

        if (categoryName != null) {
            binding.progressBar.setVisibility(View.VISIBLE);
            shopViewModel.fetchProductsByCategory(categoryName);
        }
    }

    private void setupRecyclerView() {
        binding.rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ProductAdapter(new ArrayList<>(), product -> {
            switch(categoryName) {
                case "CPU": shopViewModel.setCpu(product); break;
                case "CPUCooler": shopViewModel.setCpuCooler(product); break;
                case "Motherboard": shopViewModel.setMotherboard(product); break;
                case "Memory": shopViewModel.setRam(product); break;
                case "Storage": shopViewModel.setStorage(product); break;
                case "VideoCard": shopViewModel.setGpu(product); break;
                case "Case": shopViewModel.setPcCase(product); break;
                case "PowerSupply": shopViewModel.setPsu(product); break;
            }
            Navigation.findNavController(requireView()).popBackStack();
        });

        binding.rvProducts.setAdapter(adapter);
    }

    private void setupObservers() {
        shopViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        shopViewModel.getCategoryProducts().observe(getViewLifecycleOwner(), products -> {
            if (products != null && !products.isEmpty()) {
                adapter.updateData(products);
            }
            else {
                Boolean isLoading = shopViewModel.getIsLoading().getValue();
                if (isLoading != null && !isLoading) {
                    Toast.makeText(getContext(), "No components found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}