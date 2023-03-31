package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Test;

public class MapResumeTest extends AbstractStorageTest {
    public MapResumeTest() {
        super(new MapResumeStorage());
    }

    //method delete()
    @Test
    public void delete() {
        storage.delete(r1);
        assertSize(2);
    }

    //method update()
    @Test
    public void update() {
        Resume resume = new Resume("UUID_3", "1");
        storage.update(resume);
        Assert.assertSame(resume, storage.get(r1));
    }

    void assertGet(Resume resume) {
        Assert.assertSame(resume, storage.get(resume));
    }

}