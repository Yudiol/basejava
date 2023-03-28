package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ListStorage extends AbstractStorage {
    List<Resume> storage = new ArrayList<>();

    @Override
    public List<Resume> getAll() {
        return storage;
    }

    @Override
    Object isExist(String uuid) {
        return getIndex(uuid);
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
        storage.remove(getResume(searchKey));
    }

    @Override
    public int sizeResume() {
        return storage.size();
    }

    public int getIndex(String uuid) {
        int index = -1;
        for (int i = 0; i < size(); i++) {
            if (Objects.equals(uuid, storage.get(i).getUuid())) {
                index = i;
            }
        }
        return index;
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
