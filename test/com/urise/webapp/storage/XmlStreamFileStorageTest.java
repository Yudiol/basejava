package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.XmlStreamSerializer;

public class XmlStreamFileStorageTest extends AbstractStorageTest {
    public XmlStreamFileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new XmlStreamSerializer()));
    }
}