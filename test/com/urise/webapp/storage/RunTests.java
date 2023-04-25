package com.urise.webapp.storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ArrayStorageTest.class,
        SortedArrayStorageTest.class,
        ListStorageTest.class,
        MapStorageTest.class,
        MapResumeTest.class,
        ObjectStreamFileStorageTest.class,
        ObjectStreamPathStorageTest.class,
        XmlStreamFileStorageTest.class,
        XmlStreamPathStorageTest.class,
        DataStreamFileStorageTest.class,
        DataStreamPathStorageTest.class
})
public class RunTests {
}
