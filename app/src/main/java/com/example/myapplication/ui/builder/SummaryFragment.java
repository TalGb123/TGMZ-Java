package com.example.myapplication.ui.builder;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentBuildSummaryBinding;
import com.example.myapplication.model.PcBuild;
import com.example.myapplication.model.Product;
import com.example.myapplication.viewmodel.AuthViewModel;
import com.example.myapplication.viewmodel.ShopViewModel;

public class SummaryFragment extends Fragment {

    private FragmentBuildSummaryBinding binding;
    private AuthViewModel authViewModel;
    private ShopViewModel shopViewModel;
    private PcBuild currentBuild;
    private String pendingBuildName = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBuildSummaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);

        if (getArguments() != null) {
            currentBuild = SummaryFragmentArgs.fromBundle(getArguments()).getCurrentBuild();
        }

        if (currentBuild == null) {
            Toast.makeText(getContext(), "Error loading build data", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
            return;
        }

        populateBuildList();
        setupListeners();
        setupObservers();
    }

    private void populateBuildList() {
        binding.layoutPartsContainer.removeAllViews();
        double totalCost = 0;

        totalCost += addPartRowIfNotNull("CPU", currentBuild.getCpu());
        totalCost += addPartRowIfNotNull("CPU Cooler", currentBuild.getCpuCooler());
        totalCost += addPartRowIfNotNull("Motherboard", currentBuild.getMotherboard());
        totalCost += addPartRowIfNotNull("Memory (RAM)", currentBuild.getRam());
        totalCost += addPartRowIfNotNull("Storage", currentBuild.getStorage());
        totalCost += addPartRowIfNotNull("Video Card", currentBuild.getGpu());
        totalCost += addPartRowIfNotNull("Power Supply", currentBuild.getPowerSupply());
        totalCost += addPartRowIfNotNull("PC Case", currentBuild.getPcCase());

        binding.tvSummaryTotal.setText(String.format(java.util.Locale.US, "₪%.2f", totalCost));
    }

    private double addPartRowIfNotNull(String category, Product part) {
        if (part == null) return 0.0;

        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(0, 16, 0, 16);

        TextView tvCategory = new TextView(getContext());
        tvCategory.setText(category);
        tvCategory.setTextSize(14);
        tvCategory.setTextColor(Color.GRAY);

        TextView tvName = new TextView(getContext());
        tvName.setText(part.getName());
        tvName.setTextSize(16);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);
        tvName.setTextColor(getContext().getColor(android.R.color.tab_indicator_text));

        TextView tvPrice = new TextView(getContext());
        tvPrice.setText(String.format(java.util.Locale.US, "₪%.2f", part.getPrice()));
        tvPrice.setTextColor(getContext().getColor(android.R.color.holo_green_dark));

        row.addView(tvCategory);
        row.addView(tvName);
        row.addView(tvPrice);

        binding.layoutPartsContainer.addView(row);
        return part.getPrice();
    }

    private void setupListeners() {
        binding.btnEditBuild.setOnClickListener(v -> {
            shopViewModel.loadBuildIntoEditor(currentBuild);

            Navigation.findNavController(v).popBackStack();

            com.google.android.material.bottomnavigation.BottomNavigationView bottomNav =
                    requireActivity().findViewById(R.id.bottom_nav);

            if (bottomNav != null) {
                bottomNav.setSelectedItemId(R.id.specBuilderFragment);
            }
        });

        binding.btnSaveToProfile.setOnClickListener(v -> showNameBuildDialog());
    }

    private void showNameBuildDialog() {
        EditText input = new EditText(getContext());
        input.setHint("Enter a name for this build (e.g. Gaming Rig)");

        new AlertDialog.Builder(requireContext())
                .setTitle("Save Build")
                .setMessage("Name your custom PC build:")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (!name.isEmpty()) {
                        pendingBuildName = name;
                        shopViewModel.saveBuildToDatabase(currentBuild);
                    }
                    else {
                        Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void setupObservers() {
        shopViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressSaving.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.btnSaveToProfile.setEnabled(!isLoading);
        });

        shopViewModel.getNewlySavedBuildId().observe(getViewLifecycleOwner(), newBuildId -> {
            if (newBuildId != null) {
                shopViewModel.resetNewlySavedBuildId();
                authViewModel.saveBuildToProfile(newBuildId, pendingBuildName);
            }
        });

        authViewModel.getAuthMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && msg.equals("Build saved to your profile successfully!")) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                authViewModel.clearMessage();
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}