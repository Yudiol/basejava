package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {
    public List<Resume> getAllSorted() {
        List<Resume> resumes = getAll();
        resumes.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
        return resumes;
    }

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
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getNotExistingSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    abstract Object getSearchKey(String uuid);

    abstract boolean isExist(Object uuid);

    abstract void saveResume(Object searchKey, Resume r);

    abstract void clearResume();

    abstract int sizeResume();

    abstract void deleteResume(Object searchKey);

    public abstract List<Resume> getAll();

    abstract void updateResume(Object searchKey, Resume resume);

    abstract Resume getResume(Object searchKey);
}
