package com.example.myapplication.ui.auth;

import android.app.DatePickerDialog;
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
import com.example.myapplication.databinding.FragmentRegisterBinding;
import com.example.myapplication.utils.ValidatorUtils;
import com.example.myapplication.viewmodel.AuthViewModel;

import java.util.Calendar;
import java.util.Locale;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private AuthViewModel authViewModel;
    private String selectedBirthday = "";
    private int userAge = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        setupListeners();
        setupObservers();
    }

    private void setupListeners() {
        binding.btnBirthday.setOnClickListener(v -> showDatePicker());
        binding.btnRegister.setOnClickListener(v -> handleRegistration());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -18);

        DatePickerDialog dialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedBirthday = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    binding.btnBirthday.setText(selectedBirthday);

                    Calendar today = Calendar.getInstance();
                    userAge = today.get(Calendar.YEAR) - year;
                    if (today.get(Calendar.MONTH) < month || (today.get(Calendar.MONTH) == month && today.get(Calendar.DAY_OF_MONTH) < dayOfMonth)) {
                        userAge--;
                    }

                    if (userAge < 21) {
                        binding.tvBirthdayError.setVisibility(View.VISIBLE);
                    }
                    else {
                        binding.tvBirthdayError.setVisibility(View.GONE);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    private boolean validateForm() {
        boolean isValid = true;

        String id = binding.etId.getText().toString().trim();
        if (!ValidatorUtils.isValidIsraeliID(id)) {
            binding.idInputLayout.setError(getString(R.string.error_invalid_id));
            isValid = false;
        }
        else binding.idInputLayout.setError(null);

        String name = binding.etName.getText().toString().trim();
        if (name.length() < 2) {
            binding.nameInputLayout.setError(getString(R.string.error_invalid_name));
            isValid = false;
        }
        else binding.nameInputLayout.setError(null);

        String email = binding.etEmail.getText().toString().trim();
        if (!email.matches("^[a-zA-Z0-9._%+-]+@(walla|gmail)\\.(com|co\\.il)$")) {
            binding.emailInputLayout.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }
        else binding.emailInputLayout.setError(null);

        String phone = binding.etPhone.getText().toString().trim().replace("-", "");
        if (!phone.matches("^05\\d{8}$")) {
            binding.phoneInputLayout.setError(getString(R.string.error_invalid_phone));
            isValid = false;
        }
        else binding.phoneInputLayout.setError(null);

        if (userAge < 21 || selectedBirthday.isEmpty()) {
            binding.tvBirthdayError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        String pass = binding.etPassword.getText().toString().trim();
        if (!pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$")) {
            binding.passwordInputLayout.setError(getString(R.string.error_weak_password));
            isValid = false;
        }
        else binding.passwordInputLayout.setError(null);

        return isValid;
    }

    private void handleRegistration() {
        if (!validateForm()) return;

        authViewModel.register(
                binding.etId.getText().toString().trim(),
                binding.etName.getText().toString().trim(),
                binding.etEmail.getText().toString().trim(),
                binding.etPhone.getText().toString().trim(),
                selectedBirthday,
                binding.etPassword.getText().toString().trim()
        );
    }

    private void setupObservers() {
        authViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.btnRegister.setEnabled(!isLoading);
        });

        authViewModel.getAuthMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                if (msg.contains("בהצלחה") || msg.toLowerCase().contains("success")) {
                    Navigation.findNavController(requireView()).navigateUp();
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
