package com.example.aula.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.aula.R;
import com.example.aula.data.InMemoryNoticeRepository;
import com.example.aula.viewmodel.NoticeViewModel;
import com.example.aula.viewmodel.NoticeViewModelFactory;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class UwuFragment extends Fragment {

    public UwuFragment() {
        super(R.layout.fragment_uwu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // PARA VISUALIZAR LA LISTA DE AVISOS ***
        TextView uwuText = view.findViewById(R.id.uwuText);

        InMemoryNoticeRepository repo = InMemoryNoticeRepository.getInstance();

        NoticeViewModelFactory noticeFactory =
                new NoticeViewModelFactory(repo);

        NoticeViewModel noticeVm =
                new ViewModelProvider(this, noticeFactory)
                        .get(NoticeViewModel.class);

        noticeVm.getListado().observe(getViewLifecycleOwner(), uwuText::setText);

        noticeVm.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) {
                Snackbar.make(view, err, Snackbar.LENGTH_LONG).show();
            }
        });
        //***

        //BOTÓN BORRAR TODOS LOS AVISOS

        MaterialButton btnUwu = view.findViewById(R.id.btnUwu);

        btnUwu.setOnClickListener(v -> noticeVm.deleteAll());


    }


}
