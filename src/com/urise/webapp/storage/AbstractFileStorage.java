package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AbstractFileStorage extends AbstractStorage<File> {
    private final File directory;
    private final SerializationStorage serializationStorage;

    protected AbstractFileStorage(String dir, SerializationStorage serializationStorage) {
        directory = new File(dir);
        this.serializationStorage = serializationStorage;
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
    }

    @Override
    public void clearResume() {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                deleteResume(file);
            }
        }
    }

    @Override
    public int sizeResume() {
        String[] list = directory.list();
        if (list == null) {
            throw new StorageException("Something went wrong. Size", "");
        }
        return list.length;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void updateResume(File file, Resume resume) {
        try {
            serializationStorage.doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("The " + file.getName() + " was not updated.", file.getName());
        }
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected void saveResume(File file, Resume r) {
        try {
            file.createNewFile();
            updateResume(file, r);
        } catch (IOException e) {
            throw new StorageException("The " + file.getName() + " was not saved.", file.getName());
        }
    }

    @Override
    protected Resume getResume(File file) {
        try {
            return serializationStorage.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("No resume was received", file.getName());
        }
    }

    @Override
    protected void deleteResume(File file) {
        if (!file.delete()) {
            throw new StorageException("The " + file.getName() + " was not deleted", file.getName());
        }
    }

    @Override
    public List<Resume> getAll() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Directory read error", "");
        }
        List<Resume> resumes = new ArrayList<>();
        for (File file : files) {
            resumes.add(getResume(file));
        }
        return resumes;
    }
}