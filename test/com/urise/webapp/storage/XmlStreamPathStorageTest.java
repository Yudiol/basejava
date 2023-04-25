package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.XmlStreamSerializer;

public class XmlStreamPathStorageTest extends AbstractStorageTest {
    public XmlStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR, new XmlStreamSerializer()));
    }
}