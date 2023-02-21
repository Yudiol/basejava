package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Objects;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        if (checkResume(r) > -1) {
            System.out.println("Error method save : this resume " + '"' + r.getUuid() + '"' + " exist into storage already.");
        } else if (size >= storage.length) {
            System.out.println("Error method save : the storage is filled. Resume is " + '"' + r.getUuid() + '"');
        } else {
            storage[size++] = r;
        }
    }

    public void update(Resume r) {
        if (checkResume(r) > -1) {
            storage[checkResume(r)] = r;
        } else {
            System.out.println("Error method update : method can't find resume " + '"' + r.getUuid() + '"' + " into storage.");
        }
    }

    public Resume get(String uuid) {
        if (checkString(uuid) > -1) {
            return storage[checkString(uuid)];
        } else {
            System.out.println("Error method get : method can't find resume " + '"' + uuid + '"' + " into storage.");
            return null;
        }
    }

    public void delete(String uuid) {
        if (checkString(uuid) > -1) {
            storage[checkString(uuid)] = storage[--size];
            storage[size] = null;
        } else {
            System.out.println("Error method delete : method can't delete resume " + '"' + uuid + '"' + " because it didn't find it.");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    private int checkResume(Resume resume) {
        for (int i = 0; true; i++) {
            if (i == size) {
                break;
            } else if (storage[i] == resume) {
                return i;
            }
        }
        return -1;
    }

    private int checkString(String uuid) {
        for (int i = 0; true; i++) {
            if (i == size) {
                break;
            } else if (Objects.equals(storage[i].getUuid(), uuid)) {
                return checkResume(storage[i]);
            }
        }
        return -1;
    }
}
