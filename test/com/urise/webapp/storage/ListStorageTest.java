package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListStorageTest extends AbstractStorageTest {

    public ListStorageTest() {
        super(new ListStorage());
    }

    @Test
    public void getAll() {
        List<Resume> resumes = new ArrayList<>(Arrays.asList(r1, r2, r3));
        Assert.assertEquals(resumes, storage.getAll());
    }
}