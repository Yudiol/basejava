package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapStorageTest extends AbstractStorageTest {

    public MapStorageTest() {
        super(new MapStorage());
    }

    @Test
    public void getAll() {
        Map<String, Resume> resumes = new LinkedHashMap<>();
        resumes.put("UUID_1", r1);
        resumes.put("UUID_2", r2);
        resumes.put("UUID_3", r3);
        Assert.assertEquals(resumes, storage.getAll());
    }
}