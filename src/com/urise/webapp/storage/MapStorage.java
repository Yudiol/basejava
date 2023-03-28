package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    Map<String, Resume> storage = new LinkedHashMap<>();

    @Override
    public Map<String, Resume> getAll() {
        return storage;
    }

    @Override
    Object isExist(String uuid) {
        for (String key : storage.keySet()) {
            if (key.equals(uuid)) {
                return key;
            }
        }
        return -1;
    }

    @Override
    public void saveResume(Object searchKey, Resume r) {
        storage.put(r.getUuid(), r);
    }

    @Override
    void clearResume() {
        storage.clear();
    }

    @Override
    void deleteResume(Object searchKey) {
        storage.remove((String) searchKey);
    }

    @Override
    public int sizeResume() {
        return storage.size();
    }

    @Override
    protected void updateResume(Object searchKey, Resume resume) {
        storage.put((String) searchKey, resume);
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return storage.get((String) searchKey);
    }
}
