package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {
    @Override
    protected void fillDeletedElement(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(storage, index + 1, storage, index, numMoved);
        }
    }

    @Override
    protected void insertElement(Object searchKey, Resume r) {
//      http://codereview.stackexchange.com/questions/36221/binary-search-for-inserting-in-array#answer-36239
        int insertIdx = -(int) searchKey - 1;
        System.arraycopy(storage, insertIdx, storage, insertIdx + 1, size - insertIdx);
        storage[insertIdx] = r;
    }

    @Override
    Object getSearchKey(Object uuid) {
        return Arrays.binarySearch(storage, 0, size, new Resume((String) uuid));
    }
}