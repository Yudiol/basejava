package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    static final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public List<Resume> getAll() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }

    public int sizeResume() {
        return size;
    }

    public void clearResume() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public void deleteResume(Integer searchKey) {
        fillDeletedElement(searchKey);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected Resume getResume(Integer searchKey) {
        return storage[ searchKey];
    }

    @Override
    public void saveResume(Integer searchKey, Resume r) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        } else {
            insertElement(searchKey, r);
            size++;
        }
    }

    protected boolean isExist(Integer index) {
        return !((int) index < 0);
    }

    @Override
    protected void updateResume(Integer searchKey, Resume r) {
        storage[searchKey] = r;
    }

    abstract void fillDeletedElement(int index);

    abstract void insertElement(Integer searchKey, Resume r);
}