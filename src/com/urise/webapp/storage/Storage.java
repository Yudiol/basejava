package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.List;

public interface Storage {

    void clear();

    void update(Resume r);

    void save(Resume r);

    Resume get(Object uuid);

    void delete(Object uuid);

    List<Resume> getAllSorted();

    int size();
}