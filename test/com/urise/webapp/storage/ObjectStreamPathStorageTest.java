package com.urise.webapp.storage;

import com.urise.webapp.storage.Serialization.ObjectStreamStorage;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {
    public ObjectStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR, new ObjectStreamStorage()));
    }
}