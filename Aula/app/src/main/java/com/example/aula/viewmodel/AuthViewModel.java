package com.example.aula.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aula.data.AuthRepository;

public class AuthViewModel extends ViewModel {

    private final AuthRepository repo;

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>(null);
    private final MutableLiveData<String> _navEvent = new MutableLiveData<>(null);

    public AuthViewModel(AuthRepository repo) {
        this.repo = repo;
    }

    public LiveData<Boolean> getLoading() { return _loading; }
    public LiveData<String> getErrorMessage() { return _errorMessage; }
    public LiveData<String> getNavEvent() { return _navEvent; }

    public void consumeNavEvent() { _navEvent.setValue(null); }

    public void login(String email, String pass) {
        _errorMessage.setValue(null);
        _loading.setValue(true);

        repo.login(email, pass, new AuthRepository.AuthCallback() {
            @Override public void onSuccess() {
                _loading.postValue(false);
                _navEvent.postValue("HOME");
            }

            @Override public void onError(String message) {
                _loading.postValue(false);
                _errorMessage.postValue(message);
            }
        });
    }

    public void register(String email, String pass) {
        _errorMessage.setValue(null);
        _loading.setValue(true);

        repo.register(email, pass, new AuthRepository.AuthCallback() {
            @Override public void onSuccess() {
                _loading.postValue(false);
                _navEvent.postValue("HOME");
            }

            @Override public void onError(String message) {
                _loading.postValue(false);
                _errorMessage.postValue(message);
            }
        });
    }

    public void checkSession() {
        _navEvent.setValue(repo.isLoggedIn() ? "HOME" : "LOGIN");
    }

    public void logout() {
        repo.logout();
        _navEvent.setValue("LOGIN");
    }
}
