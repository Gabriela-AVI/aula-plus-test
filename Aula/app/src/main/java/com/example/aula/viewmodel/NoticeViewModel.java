package com.example.aula.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aula.data.InMemoryNoticeRepository;

import java.util.List;

public class NoticeViewModel extends ViewModel {

    private final InMemoryNoticeRepository repo;

    private final MutableLiveData<String> _listado = new MutableLiveData<>();
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    private final MutableLiveData<String> _eventoToast = new MutableLiveData<>(null);

    public NoticeViewModel(InMemoryNoticeRepository repo) {
        this.repo = repo;
        refresh();
    }

    public LiveData<String> getListado() { return _listado; }
    public LiveData<String> getError() { return _error; }
    public LiveData<String> getEventoToast() { return _eventoToast; }

    public void consumirEventoToast() {
        _eventoToast.setValue(null);
    }

    public void addNotice(String title, String subject) {
        if (title == null || title.trim().isEmpty()) {
            _error.setValue("El título no puede estar vacío");
            return;
        }
        if (subject == null || subject.trim().isEmpty()) subject = "General";

        repo.add(subject.trim() + " | " + title.trim());
        refresh();
        _eventoToast.setValue("Aviso añadido");
    }

    public void deleteLast() {
        boolean ok = repo.removeLast();
        if (!ok) {
            _error.setValue("No hay avisos para borrar");
            return;
        }
        refresh();
        _eventoToast.setValue("Último aviso borrado");
    }

    private void refresh() {
        List<String> data = repo.getAll();
        if (data.isEmpty()) {
            _listado.setValue("Crea un aviso para que se muestre aquí");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String s : data) sb.append("• ").append(s).append("\n");
        _listado.setValue(sb.toString());
    }
}

