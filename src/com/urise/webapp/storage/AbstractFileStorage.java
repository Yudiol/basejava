package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.io.File;

public abstract class AbstractFileStorage {

    abstract String doRead(File file);

    abstract void doWrite(Resume resume,File file);
}
