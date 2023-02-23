package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Objects;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final int STORAGE_LIMIT = 10000;
    private final Resume[] STORAGE = new Resume[STORAGE_LIMIT];
    private int size;

    public void clear() {
        Arrays.fill(STORAGE, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        if (getIndex(r.getUuid()) > -1) {
            System.out.println("Error method save : this resume " + '"' + r.getUuid() + '"' + " exist into storage already.");
        } else if (size >= STORAGE.length) {
            System.out.println("Error method save : the storage is filled. Resume is " + '"' + r.getUuid() + '"');
        } else {
            STORAGE[size++] = r;
        }
    }

    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index > -1) {
            STORAGE[index] = r;
        } else {
            System.out.println("Error method update : method can't find resume " + '"' + r.getUuid() + '"' + " into storage.");
        }
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index > -1) {
            return STORAGE[index];
        } else {
            System.out.println("Error method get : method can't find resume " + '"' + uuid + '"' + " into storage.");
            return null;
        }
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index > -1) {
            STORAGE[index] = STORAGE[--size];
            STORAGE[size] = null;
        } else {
            System.out.println("Error method delete : method can't delete resume " + '"' + uuid + '"' + " because it didn't find it.");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(STORAGE, size);
    }

    public int size() {
        return size;
    }

    private int getIndex(String uuid) {
        for (int i = 0; true; i++) {
            if (i == size) {
                break;
            } else if (Objects.equals(STORAGE[i].getUuid(), uuid)) {
                return i;
            }
        }
        return -1;
    }
}
