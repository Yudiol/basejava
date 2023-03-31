package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;


public class ListStorage extends AbstractStorage {
    private List<Resume> storage = new ArrayList<>();

    @Override
    public List<Resume> getAll() {
        return storage;
    }

    @Override
    protected boolean isExist(Object index) {
        return !((int) index < 0);
    }

    @Override
    public void saveResume(Object searchKey, Resume r) {
        storage.add(r);
    }

    @Override
    void clearResume() {
        storage.clear();
    }

    @Override
    void deleteResume(Object searchKey) {
        storage.remove((int) searchKey);
    }

    @Override
    public int sizeResume() {
        return storage.size();
    }

    @Override
    protected Object getSearchKey(Object uuid) {
        for (int i = 0; i < size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void updateResume(Object searchKey, Resume resume) {
        storage.set((int) searchKey, resume);
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return storage.get((int) searchKey);
    }
}
