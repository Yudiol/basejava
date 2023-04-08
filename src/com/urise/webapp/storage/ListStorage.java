package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    private final List<Resume> storage = new ArrayList<>();

    @Override
    public List<Resume> getAll() {
        return storage;
    }

    @Override
    protected boolean isExist(Integer index) {
        return !(index < 0);
    }

    @Override
    protected void saveResume(Integer searchKey, Resume r) {
        storage.add(r);
    }

    @Override
    protected void clearResume() {
        storage.clear();
    }

    @Override
    protected void deleteResume(Integer searchKey) {
        storage.remove((int)searchKey);
    }

    @Override
    protected int sizeResume() {
        return storage.size();
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void updateResume(Integer searchKey, Resume resume) {
        storage.set(searchKey, resume);
    }

    @Override
    protected Resume getResume(Integer searchKey) {
        return storage.get(searchKey);
    }
}
