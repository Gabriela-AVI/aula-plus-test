package com.example.aula.data;


import java.util.ArrayList;
import java.util.List;

public class InMemoryNoticeRepository {


    private static InMemoryNoticeRepository instance;
    private final List<String> database = new ArrayList<>();

    public InMemoryNoticeRepository() {
        database.add("DI | Entrega README");
        database.add("SI | Repasar permisos Linux");
    }

    public List<String> getAll() {
        return new ArrayList<>(database);
    }
    public static synchronized InMemoryNoticeRepository getInstance() {
        if (instance == null) instance = new InMemoryNoticeRepository();
        return instance;
    }
    public void add(String noticeText) {
        database.add(0, noticeText);
    }

    public boolean removeLast() {
        if (database.isEmpty()) return false;
        database.remove(database.size() - 1);
        return true;
    }
}

