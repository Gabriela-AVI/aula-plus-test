package com.example.aula.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.aula.data.InMemoryNoticeRepository;

public class NoticeViewModelFactory implements ViewModelProvider.Factory {

    private final InMemoryNoticeRepository repo;

    public NoticeViewModelFactory(InMemoryNoticeRepository repo) {
        this.repo = repo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NoticeViewModel.class)) {
            return (T) new NoticeViewModel(repo);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
