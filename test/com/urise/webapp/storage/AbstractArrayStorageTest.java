package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(value = Parameterized.class)
public class AbstractArrayStorageTest {
    AbstractArrayStorage storage;
    Resume r1 = new Resume();
    Resume r2 = new Resume();
    Resume r3 = new Resume();
    Resume r4 = new Resume();
    Resume r5 = new Resume();
    Resume r6 = new Resume();

    public AbstractArrayStorageTest(

            AbstractArrayStorage storage) {
        this.storage = storage;
    }

    @Parameterized.Parameters
    public static Object[][] resumes() {
        return new Object[][]{{
                new ArrayStorage()},
                {new SortedArrayStorage()}};
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
        storage.save(r4);
        storage.save(r5);
    }

    // method size()
    @Test
    public void checkSizeStorage_Size() {
        Assert.assertEquals(5, storage.size());
    }

    // method clear()
    @Test
    public void storageShouldBeEmpty_Clear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    // method update()
    @Test(expected = NotExistStorageException.class)
    public void shouldReturnNotExistStorageException_Update() {
        storage.update(r6);
    }

    @Test
    public void shouldReturnUpdatedResume_Update(){
        storage.update(r1);
        Assert.assertEquals(r1,storage.get(r1.getUuid()));
    }

    // method getAll()
    @Test
    public void checkAllResumes_GetAll() {
        Resume[] resumes = new Resume[]{r1, r2, r3, r4, r5};
        if (storage instanceof SortedArrayStorage) {
            Arrays.sort(resumes);
        }
        Assert.assertArrayEquals(resumes, storage.getAll());
    }

    // method save()
    @Test
    public void shouldAddOneMoreResume_Save() {
        storage.save(r6);
        Assert.assertEquals(6, storage.size());
    }

    @Test(expected = StorageException.class)
    public void shouldReturnStorageOverFlowException_Save() {
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
    public void shouldReturnExistStorageException_Save() {
        storage.save(r1);
    }

    @Test
    public void shouldFindNewElement_Save() {
        storage.save(r6);
        Assert.assertEquals(r6, storage.get(r6.getUuid()));
    }

    // method delete()
    @Test(expected = NotExistStorageException.class)
    public void shouldDeleteElementFromStorage_Delete() {
        storage.delete(r1.getUuid());
        storage.get(r1.getUuid());
    }

    @Test
    public void shouldReduceListOneLess_Delete() {
        storage.delete(r1.getUuid());
        Assert.assertEquals(4, storage.size());
    }

    // method get()
    @Test
    public void shouldReturnTheSameResume_Get() {
        Assert.assertEquals(r1, storage.get(r1.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public void shouldReturnNotExistException_Get() {
        storage.get(r6.getUuid());
    }
}