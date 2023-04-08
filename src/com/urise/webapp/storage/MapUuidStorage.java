package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapUuidStorage extends AbstractStorage<String> {
    private final Map<String, Resume> storage = new LinkedHashMap<>();

    @Override
    public List<Resume> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    protected String getSearchKey(String uuid) {
        for (String key : storage.keySet()) {
            if (key.equals(uuid)) {
                return key;
            }
        }
        return null;
    }

    @Override
    protected boolean isExist(String key) {
        return !(key == null);
    }

    @Override
    protected void saveResume(String searchKey, Resume r) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void clearResume() {
        storage.clear();
    }

    @Override
    protected void deleteResume(String searchKey) {
        storage.remove(searchKey);
    }

    @Override
    protected int sizeResume() {
        return storage.size();
    }

    @Override
    protected void updateResume(String searchKey, Resume resume) {
        storage.put(searchKey, resume);
    }

    @Override
    protected Resume getResume(String searchKey) {
        return storage.get(searchKey);
    }
}
