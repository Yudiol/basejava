package com.urise.webapp.storage;

import org.junit.Assert;
import org.junit.Test;

public class ArrayStorageTest extends AbstractArrayStorageTest {
    public ArrayStorageTest(AbstractArrayStorage storage) {
        super(storage);
        this.storage = new ArrayStorage();
    }

    // method getIndex()
    @Test
    public void shouldReturnIndexGreaterThanMinusOneAndSmallerThanSize_GetIndex() {
        Assert.assertTrue(storage.getIndex(r1.getUuid()) >= 0 &&
                storage.getIndex(r1.getUuid()) < storage.size);
    }

    @Test
    public void shouldReturnIndexEqualToMinusOne_GetIndex() {
        Assert.assertEquals(-1, storage.getIndex(r6.getUuid()));
    }

    // method fillDeletedElement()
    @Test
    public void shouldReturnNotNull() {
        storage.fillDeletedElement(0);
        Assert.assertNotNull(storage.getAll()[0]);
    }
}