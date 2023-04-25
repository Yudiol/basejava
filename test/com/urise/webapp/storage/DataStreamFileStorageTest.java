package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.DataStreamSerializer;

public class DataStreamFileStorageTest extends AbstractStorageTest {
    public DataStreamFileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new DataStreamSerializer()));
    }
}