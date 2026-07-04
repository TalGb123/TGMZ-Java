package com.example.myapplication.ui.builder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSpecBuilderBinding;
import com.example.myapplication.viewmodel.AuthViewModel;

public class SpecBuilderFragment extends Fragment {

    private FragmentSpecBuilderBinding binding;
    private AuthViewModel authViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSpecBuilderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        setupObservers();
        setupListeners();
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