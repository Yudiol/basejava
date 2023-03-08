package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {
    @Override
    void deleteResume(int index) {
        for (int i = index; i < size; i++) {
            storage[i] = storage[i + 1];
        }
        storage[size - 1] = null;
        size--;
    }

    @Override
    void saveResume(int index, Resume r) {
        index = Math.abs(index) - 1;
        for (int i = size; i > index; i--) {
            storage[i] = storage[i - 1];
        }
        storage[index] = r;
        size++;
    }

    public int updateResume(Resume r) {
        int lastIndex = -1;
        for (int i = 0; i < size; i++) {
            if (r.getUuid().equals(storage[i].toString())) {
                lastIndex = i;
            }
        }
        if (storage[lastIndex + 1] != null && storage[lastIndex].compareTo(storage[lastIndex + 1]) > 0) {
            for (int i = lastIndex; i < size - 1; i++) {
                if (storage[i].compareTo(storage[i + 1]) > 0) {
                    Resume replace = storage[i];
                    storage[i] = storage[i + 1];
                    storage[i + 1] = replace;
                }else {
                    break;
                }
            }
        } else if (lastIndex != 0 && storage[lastIndex].compareTo(storage[lastIndex - 1]) < 0) {
            for (int i = lastIndex; i > 0; i--) {
                if (storage[i].compareTo(storage[i - 1]) < 0) {
                    Resume replace = storage[i];
                    storage[i] = storage[i-1];
                    storage[i-1] = replace;
                }else {
                    break;
                }
            }
        }
        return getIndex(r.getUuid());
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}