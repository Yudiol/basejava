package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {
    private static final Comparator<Resume> RESUME_COMPARATOR = (o1, o2) -> o1.getUuid().compareTo(o2.getUuid());

    @Override
    protected void fillDeletedElement(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(storage, index + 1, storage, index, numMoved);
        }
    }

    @Override
    protected void insertElement(Integer searchKey, Resume r) {
//      http://codereview.stackexchange.com/questions/36221/binary-search-for-inserting-in-array#answer-36239
        int insertIdx = -(int) searchKey - 1;
        System.arraycopy(storage, insertIdx, storage, insertIdx + 1, size - insertIdx);
        storage[insertIdx] = r;
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        return Arrays.binarySearch(storage, 0, size, new Resume(uuid, ""), RESUME_COMPARATOR);
    }
}