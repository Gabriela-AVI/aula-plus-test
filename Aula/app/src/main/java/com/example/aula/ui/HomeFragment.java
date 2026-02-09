package com.example.aula.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.aula.R;
import com.example.aula.data.InMemoryNoticeRepository;
import com.example.aula.viewmodel.NoticeViewModel;
import com.example.aula.viewmodel.NoticeViewModelFactory;
import com.example.aula.viewmodel.SettingsViewModel;
import com.example.aula.viewmodel.SettingsViewModelFactory;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Fragment principal de la aplicación.
 * Se encarga de:
 *  - Gestionar avisos (añadir, borrar y mostrar)
 *  - Controlar el modo oscuro
 *  - Cerrar sesión del usuario
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Campos de entrada encapsulados en TextInputLayout
        // (permite mostrar errores de validación)
        TextInputLayout tilTitle = view.findViewById(R.id.tilTitle);
        TextInputLayout tilSubject = view.findViewById(R.id.tilSubject);

        // Botones de acciones principales
        MaterialButton btnAdd = view.findViewById(R.id.btnAdd);
        MaterialButton btnDeleteLast = view.findViewById(R.id.btnDeleteLast);
        MaterialButton btnLogout = view.findViewById(R.id.btnLogout);

        // TextView donde se muestra el listado de avisos
        TextView tvList = view.findViewById(R.id.tvList);

        // Interruptor para activar/desactivar el modo oscuro
        MaterialSwitch switchDark = view.findViewById(R.id.switchDarkMode);

        MaterialButton btnUwu = view.findViewById(R.id.uwuButton);

        /* =========================
           SETTINGS VIEWMODEL
           ========================= */

        // Factory necesaria porque el ViewModel usa Context (DataStore / SharedPreferences)
        SettingsViewModelFactory settingsFactory =
                new SettingsViewModelFactory(requireContext());

        SettingsViewModel settingsVm =
                new ViewModelProvider(this, settingsFactory)
                        .get(SettingsViewModel.class);

        // Observa el estado del modo oscuro almacenado
        settingsVm.getDarkMode().observe(getViewLifecycleOwner(), enabled -> {
            if (enabled == null) return;

            // Evita bucles infinitos al sincronizar Switch ↔ ViewModel
            if (switchDark.isChecked() != enabled) {
                switchDark.setChecked(enabled);
            }

            // Aplica el modo oscuro/claro a toda la aplicación
            AppCompatDelegate.setDefaultNightMode(
                    enabled
                            ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        // Cada cambio del switch se guarda en el ViewModel
        switchDark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsVm.setDarkMode(isChecked);
        });

        /* =========================
           NOTICE VIEWMODEL
           ========================= */

        // Repositorio en memoria (singleton)
        // Se usa para simular persistencia sin base de datos
        InMemoryNoticeRepository repo = InMemoryNoticeRepository.getInstance();

        NoticeViewModelFactory noticeFactory =
                new NoticeViewModelFactory(repo);

        NoticeViewModel noticeVm =
                new ViewModelProvider(this, noticeFactory)
                        .get(NoticeViewModel.class);

        // Observa el listado de avisos ya formateado como texto
        noticeVm.getListado().observe(getViewLifecycleOwner(), tvList::setText);

        // Observa errores "persistentes" (no son eventos)
        noticeVm.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) {
                Snackbar.make(view, err, Snackbar.LENGTH_LONG).show();
            }
        });

        // Observa eventos de UI (mensajes que deben mostrarse solo una vez)
        noticeVm.getEventoToast().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
                // Marca el evento como consumido para evitar repetirlo
                noticeVm.consumirEventoToast();
            }
        });

        /* =========================
           ACCIONES DE USUARIO
           ========================= */

        btnAdd.setOnClickListener(v -> {

            // Limpia errores anteriores antes de validar
            tilTitle.setError(null);

            // Comprobación defensiva:
            // el TextInputLayout DEBE contener un EditText
            if (tilTitle.getEditText() == null || tilSubject.getEditText() == null) {
                Snackbar.make(
                        view,
                        "Error en el layout: falta EditText dentro del TextInputLayout",
                        Snackbar.LENGTH_LONG
                ).show();
                return;
            }

            // Se obtienen y limpian los valores introducidos por el usuario
            String title = tilTitle.getEditText().getText().toString().trim();
            String subject = tilSubject.getEditText().getText().toString().trim();

            // La lógica de validación y negocio se delega al ViewModel
            noticeVm.addNotice(title, subject);

            // Limpia solo el título para facilitar la introducción rápida
            tilTitle.getEditText().setText("");
        });

        // Elimina el último aviso desde el ViewModel
        btnDeleteLast.setOnClickListener(v -> noticeVm.deleteLast());

        // Cierra sesión y redirige al flujo de autenticación
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_home_to_authGate);
        });

        btnUwu.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_homeFragment_to_uwuFragment);
        });

    }
}
