package com.example.aula.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aula.data.SettingsRepository;

public class SettingsViewModel extends ViewModel {

    private final SettingsRepository repo;
    private final MutableLiveData<Boolean> _darkMode = new MutableLiveData<>();

    public SettingsViewModel(SettingsRepository repo) {
        this.repo = repo;
        _darkMode.setValue(repo.isDarkModeEnabled());
    }

    public LiveData<Boolean> getDarkMode() {
        return _darkMode;
    }

    public void setDarkMode(boolean enabled) {
        repo.setDarkMode(enabled);
        _darkMode.setValue(enabled);
    }
}


