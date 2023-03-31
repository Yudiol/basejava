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

    public void update(Resume resume) {
        Object searchKey = getNotExistingSearchKey(resume.getUuid());
        updateResume(searchKey, resume);
    }

    public Resume get(Object uuid) {
        Object searchKey = getNotExistingSearchKey(uuid);
        return getResume(searchKey);
    }

    public void save(Resume r) {
        Object searchKey = getExistingSearchKey(r.getUuid());
        saveResume(searchKey, r);
    }

    public void delete(Object uuid) {
        Object searchKey = getNotExistingSearchKey(uuid);
        deleteResume(searchKey);
    }

    public int size() {
        return sizeResume();
    }

    private Object getExistingSearchKey(Object uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException((String) uuid);
        }
        return searchKey;
    }

    private Object getNotExistingSearchKey(Object uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(checkResume(uuid));
        }
        return searchKey;
    }

    protected String checkResume(Object uuid) {
        if (uuid instanceof Resume) {
            return ((Resume) uuid).getUuid();
        } else {
            return (String) uuid;
        }
    }

    abstract Object getSearchKey(Object uuid);

    abstract boolean isExist(Object uuid);

    abstract void saveResume(Object searchKey, Resume r);

    abstract void clearResume();

    abstract int sizeResume();

    abstract void deleteResume(Object searchKey);

    public abstract List<Resume> getAll();

    abstract void updateResume(Object searchKey, Resume resume);

    abstract Resume getResume(Object searchKey);


}
