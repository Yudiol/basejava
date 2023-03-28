package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    public void clear() {
        clearResume();
    }

    public void update(Resume r) {
        Object searchKey = getNotExistingSearchKey(r.getUuid());
        updateResume(searchKey, r);
    }

    public Resume get(String uuid) {
        Object searchKey = getNotExistingSearchKey(uuid);
        return getResume(searchKey);
    }

    public void save(Resume r) {
        Object searchKey = getExistingSearchKey(r.getUuid());
        saveResume(searchKey, r);
    }

    public void delete(String uuid) {
        Object searchKey = getNotExistingSearchKey(uuid);
        deleteResume(searchKey);
    }

    public int size() {
        return sizeResume();
    }

    private Object getExistingSearchKey(String uuid) {
        Object searchKey = isExist(uuid);
        if (searchKey instanceof Integer && (Integer) searchKey >= 0 || searchKey instanceof String) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getNotExistingSearchKey(String uuid) {
        Object searchKey = isExist(uuid);
        if (searchKey instanceof Integer && (Integer) searchKey < 0) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    abstract Object isExist(String uuid);

    abstract void saveResume(Object searchKey, Resume r);

    abstract void clearResume();

    abstract int sizeResume();

    abstract void deleteResume(Object searchKey);

    public abstract Object getAll();

    abstract void updateResume(Object searchKey, Resume resume);

    abstract Resume getResume(Object searchKey);
}
