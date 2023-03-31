package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {
    @Override
    protected void fillDeletedElement(int index) {
        storage[index] = storage[size - 1];
    }

    @Override
    protected void insertElement(Object searchKey, Resume r) {
        storage[size] = r;
    }

    @Override
    protected Object getSearchKey(Object uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}