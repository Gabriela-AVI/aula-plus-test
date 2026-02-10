package com.example.aula.data;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {

    public interface AuthCallback {
        void onSuccess();
        void onError(String message);
    }

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Nullable
    public String getUID() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    public void login(String email, String pass, AuthCallback cb) {
        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) cb.onSuccess();
                    else cb.onError(parseAuthError(task.getException()));
                });
    }

    public void register(String email, String pass, AuthCallback cb) {
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) cb.onSuccess();
                    else cb.onError(parseAuthError(task.getException()));
                });

    }

    public boolean isLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    public void logout() {
        auth.signOut();
    }

    private String parseAuthError(Exception e) {
        if (e == null) return "Error desconocido";
        String msg = e.getMessage();
        if (msg != null) {
            if (msg.contains("password") && msg.contains("invalid")) return "Credenciales inválidas";
            if (msg.contains("no user record")) return "No existe una cuenta con ese email";
            if (msg.contains("email address is already in use")) return "Ese email ya está registrado";
            if (msg.contains("network error")) return "Error de red. Revisa tu conexión";
        }
        return "No se pudo completar la operación";
    }
}
