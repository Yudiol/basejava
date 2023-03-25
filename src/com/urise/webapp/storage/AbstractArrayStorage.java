package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
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

    public void deleteResume(int index) {
        fillDeletedElement(index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected Resume getResume(int index) {
        return storage[index];
    }

    public void saveResume(Resume r, int index) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        } else {
            insertElement(r, index);
            size++;
        }
    }

    @Override
    protected void updateResume(int index, Resume r) {
        storage[index] = r;
    }

    abstract void fillDeletedElement(int index);

    abstract void insertElement(Resume r, int index);

    abstract int getIndex(String uuid);
}