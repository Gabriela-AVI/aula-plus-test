package com.example.aula.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aula.R;
import com.example.aula.data.AuthRepository;
import com.example.aula.viewmodel.AuthViewModel;
import com.example.aula.viewmodel.AuthViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;


public class AuthGateFragment extends Fragment {
    private AuthViewModel vm;
    public AuthGateFragment() {
        super(R.layout.fragment_auth_gate);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AuthRepository repo = new AuthRepository();
        AuthViewModelFactory factory = new AuthViewModelFactory(repo);
        vm = new ViewModelProvider(this, factory).get(AuthViewModel.class);

        vm.getNavEvent().observe(getViewLifecycleOwner(), event -> {
            if (event == null) return;

            if ("HOME".equals(event)) {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_gate_to_home);

            } else if ("LOGIN".equals(event)) {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_gate_to_login);
            }

            vm.consumeNavEvent();
        });

        vm.checkSession();
    }
}