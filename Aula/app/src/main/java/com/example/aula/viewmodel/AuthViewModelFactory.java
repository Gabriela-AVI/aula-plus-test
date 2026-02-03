package com.example.aula.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.aula.data.AuthRepository;

public class AuthViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepository repo;

    public AuthViewModelFactory(AuthRepository repo) {
        this.repo = repo;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AuthViewModel.class)) {
            return (T) new AuthViewModel(repo);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}