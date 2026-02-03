package com.example.aula.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.aula.data.SettingsRepository;

public class SettingsViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public SettingsViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(new SettingsRepository(context));
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

