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
import com.example.myapplication.ui.builder.adapters.BuildSlotAdapter;
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

        adapter = new BuildSlotAdapter(slotList, slot -> {
            Bundle bundle = new Bundle();
            bundle.putString("categoryName", slot.getCategoryId());
            Navigation.findNavController(requireView()).navigate(R.id.action_specBuilderFragment_to_productListFragment, bundle);
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
            if (user == null) {
                binding.layoutGuestLock.setVisibility(View.VISIBLE);
                binding.layoutBuilderContent.setVisibility(View.GONE);
            }
            else {
                binding.layoutGuestLock.setVisibility(View.GONE);
                binding.layoutBuilderContent.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupListeners() {
        binding.btnGoToLogin.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.loginFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}