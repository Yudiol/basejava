package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    static final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public int sizeResume() {
        return size;
    }

    public void clearResume() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public void deleteResume(Object searchKey) {
        fillDeletedElement((int) searchKey);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return storage[(int) searchKey];
    }

    @Override
    public void saveResume(Object searchKey, Resume r) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        } else {
            insertElement(searchKey, r);
            size++;
        }
    }

    public Object isExist(String uuid) {
        return getIndex(uuid);
    }

    @Override
    protected void updateResume(Object searchKey, Resume r) {
        storage[(Integer) searchKey] = r;
    }

    abstract void fillDeletedElement(int index);

    abstract void insertElement(Object searchKey, Resume r);

    abstract int getIndex(String uuid);
}