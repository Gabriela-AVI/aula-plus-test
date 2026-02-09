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
import com.example.aula.viewmodel.AuthViewModel;
import com.example.aula.viewmodel.AuthViewModelFactory;

/**
 * Fragment "puerta de entrada" de la aplicación.
 *
 * No muestra interfaz interactiva: su única función es decidir
 * a qué pantalla navegar según el estado de autenticación.
 *
 * Actúa como un router inicial desacoplado de Firebase y de la UI.
 */
public class AuthGateFragment extends Fragment {

    // ViewModel responsable de comprobar la sesión y emitir eventos de navegación
    private AuthViewModel vm;

    public AuthGateFragment() {
        // Asocia directamente el layout sin inflar manualmente
        super(R.layout.fragment_auth_gate);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        /* =========================
           INICIALIZACIÓN MVVM
           ========================= */

        // El repositorio encapsula la lógica de autenticación (Firebase u otra fuente)
        AuthRepository repo = new AuthRepository();

        // Factory necesaria para inyectar el repositorio en el ViewModel
        AuthViewModelFactory factory = new AuthViewModelFactory(repo);

        vm = new ViewModelProvider(this, factory)
                .get(AuthViewModel.class);

        /* =========================
           NAVEGACIÓN BASADA EN EVENTOS
           ========================= */

        // Observa un evento puntual de navegación
        // (no es estado persistente, se consume una sola vez)
        vm.getNavEvent().observe(getViewLifecycleOwner(), event -> {
            if (event == null) return;

            // Decide el destino sin conocer la lógica interna de autenticación
            if ("HOME".equals(event)) {

                // Usuario autenticado → pantalla principal
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_gate_to_home);

            } else if ("LOGIN".equals(event)) {

                // No hay sesión activa → pantalla de login
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_gate_to_login);
            }
            // Marca el evento como consumido para evitar navegación duplicada
            vm.consumeNavEvent();
        });

        /* =========================
           PUNTO DE ENTRADA LÓGICO
           ========================= */

        // Lanza la comprobación de sesión
        // El Fragment no sabe ni cómo ni con qué tecnología se valida
        vm.checkSession();
    }
}
