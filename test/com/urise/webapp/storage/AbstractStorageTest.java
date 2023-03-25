package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractStorageTest {
    AbstractStorage storage;
    final Resume r1 = new Resume("UUID_1");
    final Resume r2 = new Resume("UUID_2");
    final Resume r3 = new Resume("UUID_3");
    final Resume r4 = new Resume("UUID_NOT_EXIST");

    @Before
    public void setUp(){
        storage.clear();
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
    }

    public AbstractStorageTest(AbstractStorage storage) {
        this.storage = storage;
    }

    // method clear()
    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    //method update()
    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(r4);
    }

    @Test
    public void update() {
        Resume resume = new Resume("UUID_1");
        storage.update(resume);
        Assert.assertSame(resume, storage.get(r1.getUuid()));
    }

    // method get()
    @Test
    public void get() {
        assertGet(r1);
        assertGet(r2);
        assertGet(r3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(r4.getUuid());
    }


    // method save()
    @Test
    public void save() {
        storage.save(r4);
        assertGet(r4);
        assertSize(4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(r1);
    }

    // method size()
    @Test
    public void size() {
        assertSize(3);
    }

    // method delete()
    @Test
    public void delete() {
        storage.delete(r1.getUuid());
        assertSize(2);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(r1.getUuid());
        assertGet(r1);
    }

    void assertGet(Resume resume) {
        Assert.assertEquals(resume, storage.get(resume.getUuid()));
    }

    void assertSize(int i) {
        Assert.assertEquals(i, storage.size());
    }
}
