package com.urise.webapp.storage.Serialization;

import com.urise.webapp.model.Resume;

import java.io.*;

public class ObjectStreamStorage implements SerializationStorage {
    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(os)) {
            outputStream.writeObject(resume);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (ObjectInputStream inputStream = new ObjectInputStream(is)) {
            return (Resume) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
