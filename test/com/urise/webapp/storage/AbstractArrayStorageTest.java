package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public abstract class AbstractArrayStorageTest {
    final AbstractArrayStorage storage;
    final Resume r1 = new Resume();
    final Resume r2 = new Resume();
    final Resume r3 = new Resume();
    final Resume r4 = new Resume("UUID_NOT_EXIST");

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
    }

    public AbstractArrayStorageTest(AbstractArrayStorage storage) {
        this.storage = storage;
    }

    // method size()
    @Test
    public void size() {
        assertSize(3);
    }

    private void assertSize(int i) {
        Assert.assertEquals(i, storage.size());
    }

    // method clear()
    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
        Assert.assertArrayEquals(new Resume[]{}, storage.getAll());
    }

    // method update()
    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(r4);
    }

    @Test
    public void update() {
        Resume resume1 = new Resume("resume");
        storage.save(resume1);
        Resume resume2 = new Resume("resume");
        storage.update(resume2);
        Assert.assertSame(resume2, storage.get(resume1.getUuid()));
    }

    // method getAll()
    @Test
    public void checkAllResumes_GetAll() {
        Resume[] resumes = new Resume[]{r1, r2, r3};
        if (storage instanceof SortedArrayStorage) {
            Arrays.sort(resumes);
        }
        Assert.assertArrayEquals(resumes, storage.getAll());
    }

    // method save()
    @Test
    public void save() {
        storage.save(r4);
        assertGet(r4);
        assertSize(4);
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() {
        storage.clear();
        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        } catch (RuntimeException e) {
            Assert.fail("Storage overflow ahead of time");
        }
        storage.save(new Resume());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(r1);
    }

    // method delete()
    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(r1.getUuid());
        assertGet(r1);
    }

    @Test
    public void delete() {
        storage.delete(r1.getUuid());
        assertSize(2);
    }

    // method get()
    @Test
    public void get() {
        assertGet(r1);
        assertGet(r2);
        assertGet(r3);
    }

    public void assertGet(Resume resume) {
        Assert.assertEquals(resume, storage.get(resume.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(r4.getUuid());
    }
}