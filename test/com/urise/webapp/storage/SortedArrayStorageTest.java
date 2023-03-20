package com.urise.webapp.storage;

import org.junit.Assert;
import org.junit.Test;

public class SortedArrayStorageTest extends AbstractArrayStorageTest {
    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }

    // method getIndex()
    @Test
    public void shouldReturnIndexGreaterThanMinusOneAndSmallerThanSize_GetIndex() {
        Assert.assertTrue(storage.getIndex(r1.getUuid()) >= 0 &&
                storage.getIndex(r1.getUuid()) < storage.size);
    }

    @Test
    public void shouldReturnIndexEqualToMinusOne_GetIndex() {
        Assert.assertTrue(storage.getIndex(r4.getUuid()) < 0);
    }

    // method fillDeletedElement()
    @Test
    public void shouldReturnNotNull() {
        storage.fillDeletedElement(0);
        Assert.assertNotNull(storage.getAll()[0]);
    }
}