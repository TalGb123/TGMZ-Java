package com.example.myapplication.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentProfileBinding;
import com.example.myapplication.model.SavedBuild;
import com.example.myapplication.adapter.SavedBuildAdapter;
import com.example.myapplication.viewmodel.AuthViewModel;
import com.example.myapplication.viewmodel.ShopViewModel;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private AuthViewModel authViewModel;
    private ShopViewModel shopViewModel;
    private SavedBuildAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
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
    }

    private void setupRecyclerView() {
        binding.rvSavedBuilds.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new SavedBuildAdapter(new ArrayList<>(), new SavedBuildAdapter.OnBuildClickListener() {
            @Override
            public void onBuildClick(SavedBuild savedBuild) {
                String[] options = {"View Summary", "Rename Build"};
                new AlertDialog.Builder(requireContext())
                        .setTitle(savedBuild.getBuildName())
                        .setItems(options, (dialog, which) -> {
                            if (which == 0) {
                                shopViewModel.fetchBuildById(savedBuild.getBuildRef());
                            }
                            else if (which == 1) {
                                showRenameDialog(savedBuild);
                            }
                        }).show();
            }

            @Override
            public void onDeleteClick(SavedBuild build) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete Build")
                        .setMessage("Are you sure you want to delete this build?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            authViewModel.deleteSavedBuild(build.getBuildRef());
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        binding.rvSavedBuilds.setAdapter(adapter);
    }

    private void showRenameDialog(SavedBuild savedBuild) {
        EditText input = new EditText(getContext());
        input.setText(savedBuild.getBuildName());

        new AlertDialog.Builder(requireContext())
                .setTitle("Rename Build")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty() && !newName.equals(savedBuild.getBuildName())) {
                        authViewModel.renameSavedBuild(savedBuild.getBuildRef(), newName);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void setupObservers() {
        authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.layoutGuestLock.setVisibility(View.GONE);
                binding.layoutProfileContent.setVisibility(View.VISIBLE);
                binding.tvUserEmail.setText(user.getEmail());

                if (user.getSavedBuilds() != null && !user.getSavedBuilds().isEmpty()) {
                    adapter.updateData(user.getSavedBuilds());
                } else {
                    adapter.updateData(new ArrayList<>());
                }
            } else {
                binding.layoutGuestLock.setVisibility(View.VISIBLE);
                binding.layoutProfileContent.setVisibility(View.GONE);
                adapter.updateData(new ArrayList<>());
            }
        });

        shopViewModel.getViewedBuild().observe(getViewLifecycleOwner(), pcBuild -> {
            if (pcBuild != null) {
                ProfileFragmentDirections.ActionProfileFragmentToSummaryFragment action =
                        ProfileFragmentDirections.actionProfileFragmentToSummaryFragment(pcBuild);
                Navigation.findNavController(requireView()).navigate(action);
                shopViewModel.clearViewedBuild();
            }
        });
    }

    private void setupListeners() {
        binding.btnLogout.setOnClickListener(v -> authViewModel.signOut());
        binding.btnGoToLogin.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.loginFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
