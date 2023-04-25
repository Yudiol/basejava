package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.JsonStreamSerializer;

public class JsonStreamFileStorageTest extends AbstractStorageTest {
    public JsonStreamFileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new JsonStreamSerializer()));
    }
}