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

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSpecBuilderBinding;
import com.example.myapplication.model.BuildSlot;
import com.example.myapplication.adapter.BuildSlotAdapter;
import com.example.myapplication.model.PcBuild;
import com.example.myapplication.viewmodel.AuthViewModel;
import com.example.myapplication.viewmodel.ShopViewModel;

import java.util.ArrayList;
import java.util.List;

public class SpecBuilderFragment extends Fragment {

    private FragmentSpecBuilderBinding binding;
    private AuthViewModel authViewModel;
    private ShopViewModel shopViewModel;
    private BuildSlotAdapter adapter;
    private List<BuildSlot> slotList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSpecBuilderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);

        setupRecyclerView();
        setupObservers();
        setupListeners();

        shopViewModel.getCurrentBuild().observe(getViewLifecycleOwner(), build -> {
            if (slotList != null && !slotList.isEmpty()) {
                slotList.get(0).setSelectedProduct(build.getCpu());
                slotList.get(1).setSelectedProduct(build.getCpuCooler());
                slotList.get(2).setSelectedProduct(build.getMotherboard());
                slotList.get(3).setSelectedProduct(build.getRam());
                slotList.get(4).setSelectedProduct(build.getStorage());
                slotList.get(5).setSelectedProduct(build.getGpu());
                slotList.get(6).setSelectedProduct(build.getPcCase());
                slotList.get(7).setSelectedProduct(build.getPowerSupply());

                adapter.notifyDataSetChanged();

                binding.tvTotalPrice.setText(String.format(java.util.Locale.US, "₪%.2f", build.getTotalPrice()));
            }
        });
    }

    private void setupRecyclerView() {
        slotList = new ArrayList<>();
        slotList.add(new BuildSlot("CPU", "Processor", android.R.drawable.ic_menu_manage));
        slotList.add(new BuildSlot("CPUCooler", "Proc. Cooler", android.R.drawable.ic_menu_sort_by_size));
        slotList.add(new BuildSlot("Motherboard", "Motherboard", android.R.drawable.ic_menu_mapmode));
        slotList.add(new BuildSlot("Memory", "Memory", android.R.drawable.ic_menu_agenda));
        slotList.add(new BuildSlot("Storage", "Storage", android.R.drawable.ic_menu_save));
        slotList.add(new BuildSlot("VideoCard", "Graphics Card", android.R.drawable.ic_menu_camera));
        slotList.add(new BuildSlot("Case", "PC Case", android.R.drawable.ic_menu_gallery));
        slotList.add(new BuildSlot("PowerSupply", "Power Supply", android.R.drawable.ic_lock_power_off));

        binding.rvBuildSlots.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new BuildSlotAdapter(slotList, new BuildSlotAdapter.OnSlotClickListener() {
            @Override
            public void onSlotClick(BuildSlot slot) {
                SpecBuilderFragmentDirections.ActionSpecBuilderFragmentToProductListFragment action =
                        SpecBuilderFragmentDirections.actionSpecBuilderFragmentToProductListFragment(slot.getCategoryId());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }

            @Override
            public void onClearClick(BuildSlot slot) {
                shopViewModel.clearSlot(slot.getCategoryId());
            }
        });

        binding.rvBuildSlots.setAdapter(adapter);
    }

    private void setupObservers() {
        if (authViewModel.getCurrentUser().getValue() == null) {
            binding.layoutGuestLock.setVisibility(View.VISIBLE);
            binding.layoutBuilderContent.setVisibility(View.GONE);
        }
        else {
            binding.layoutGuestLock.setVisibility(View.GONE);
            binding.layoutBuilderContent.setVisibility(View.VISIBLE);
        }

        authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            binding.layoutGuestLock.setVisibility(View.GONE);
            binding.layoutBuilderContent.setVisibility(View.VISIBLE);

            if (user == null) {
                binding.btnReviewBuild.setAlpha(0.5f);
            }
            else {
                binding.btnReviewBuild.setAlpha(1.0f);
            }
        });
    }

    private void setupListeners() {
        binding.btnReviewBuild.setOnClickListener(v -> {
            if (authViewModel.getCurrentUser().getValue() == null) {
                Toast.makeText(getContext(), "You must be logged in in order to save builds", Toast.LENGTH_SHORT).show();
                return;
            }

            PcBuild build = shopViewModel.getCurrentBuild().getValue();
            if (build != null) {
                SpecBuilderFragmentDirections.ActionSpecBuilderFragmentToSummaryFragment action =
                        SpecBuilderFragmentDirections.actionSpecBuilderFragmentToSummaryFragment(build);
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
            else {
                Toast.makeText(getContext(), "Cannot review an empty build", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}