package com.example.aula.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.aula.R;
import com.example.aula.data.AuthRepository;
import com.example.aula.data.SettingsRepository;
import com.example.aula.viewmodel.AuthViewModel;
import com.example.aula.viewmodel.AuthViewModelFactory;
import com.example.aula.viewmodel.SettingsViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

    private AuthViewModel vm;


    public LoginFragment() { super(R.layout.fragment_login); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TextInputLayout tilEmail = view.findViewById(R.id.tilEmail);
        TextInputLayout tilPass = view.findViewById(R.id.tilPass);
        TextInputEditText etEmail = view.findViewById(R.id.etEmail);
        TextInputEditText etPass = view.findViewById(R.id.etPass);

        MaterialButton btnLogin = view.findViewById(R.id.btnLogin);
        MaterialButton btnGoRegister = view.findViewById(R.id.btnGoRegister);
        CircularProgressIndicator progress = view.findViewById(R.id.progress);

        AuthRepository repo = new AuthRepository();
        AuthViewModelFactory factory = new AuthViewModelFactory(repo);
        vm = new ViewModelProvider(requireActivity(), factory).get(AuthViewModel.class);

        vm.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            boolean loading = Boolean.TRUE.equals(isLoading);
            btnLogin.setEnabled(!loading);
            btnGoRegister.setEnabled(!loading);
            if (loading) {
                progress.show();
            } else {
                progress.hide();
            }
        });

        vm.getErrorMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
            }
        });

        vm.getNavEvent().observe(getViewLifecycleOwner(), event -> {
            if (event == null) return;
            if (event.equals("HOME")) {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_login_to_home);
            }
            vm.consumeNavEvent();
        });

        btnGoRegister.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_login_to_register)
        );

        btnLogin.setOnClickListener(v -> {
            tilEmail.setError(null);
            tilPass.setError(null);

            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String pass = etPass.getText() != null ? etPass.getText().toString() : "";

            boolean ok = true;
            if (email.isEmpty()) { tilEmail.setError("Email obligatorio"); ok = false; }
            else if (!email.contains("@")) { tilEmail.setError("Email no válido"); ok = false; }

            if (pass.isEmpty()) { tilPass.setError("Contraseña obligatoria"); ok = false; }
            else if (pass.length() < 6) { tilPass.setError("Mínimo 6 caracteres"); ok = false; }

            if (!ok) return;

            vm.login(email, pass);
        });
    }
}