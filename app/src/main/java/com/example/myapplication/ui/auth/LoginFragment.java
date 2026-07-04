package com.example.myapplication.ui.auth;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentLoginBinding;
import com.example.myapplication.viewmodel.AuthViewModel;

public class LoginFragment extends Fragment{
    private FragmentLoginBinding binding;
    private AuthViewModel authViewModel;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        sharedPreferences = requireActivity().getSharedPreferences("TGMZ_PREFS", Context.MODE_PRIVATE);

        // GUARANTEE NO GHOST SESSIONS
        authViewModel.signOut();

        loadSavedCredentials();
        setupObservers();
        setupListeners();
    }

    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> handleLogin());

        binding.btnGuest.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_categoryListFragment);
        });

        binding.tvRegisterLink.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment);
        });
    }

    private void handleLogin() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        binding.emailInputLayout.setError(null);
        binding.passwordInputLayout.setError(null);

        if (email.isEmpty()) {
            binding.emailInputLayout.setError(getString(R.string.error_required));
            return;
        }
        if (password.isEmpty()) {
            binding.passwordInputLayout.setError(getString(R.string.error_required));
            return;
        }

        if (binding.cbRememberMe.isChecked()) {
            sharedPreferences.edit()
                    .putString("saved_email", email)
                    .putString("saved_password", password)
                    .apply();
        }
        else {
            sharedPreferences.edit().clear().apply();
        }

        authViewModel.login(email, password);
    }

    private void loadSavedCredentials() {
        String savedEmail = sharedPreferences.getString("saved_email", null);
        String savedPass = sharedPreferences.getString("saved_password", null);
        if (savedEmail != null && savedPass != null) {
            binding.etEmail.setText(savedEmail);
            binding.etPassword.setText(savedPass);
            binding.cbRememberMe.setChecked(true);
        }
    }

    private void setupObservers() {
        authViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.btnLogin.setEnabled(!isLoading);
        });

        authViewModel.getAuthMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_categoryListFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}