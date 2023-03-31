package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapUuidStorage extends AbstractStorage {
    private Map<String, Resume> storage = new LinkedHashMap<>();

    @Override
    public List<Resume> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    protected Object getSearchKey(Object uuid) {
        for (String key : storage.keySet()) {
            if (key.equals(uuid)) {
                return key;
            }
        }
        return -1;
    }

    @Override
    protected boolean isExist(Object key) {
        return !(key instanceof Integer && (int) key == -1);
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