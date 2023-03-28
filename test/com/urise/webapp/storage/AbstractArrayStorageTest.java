package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    public AbstractArrayStorageTest(AbstractArrayStorage storage) {
        super(storage);
    }
    // method getAll()
    @Test
    public void getAll() {
        Resume[] resumes = new Resume[]{r1, r2, r3};
        if (storage instanceof SortedArrayStorage) {
            Arrays.sort(resumes);
        }
        Assert.assertArrayEquals(resumes, (Resume[]) storage.getAll());
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


}