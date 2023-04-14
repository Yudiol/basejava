package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;
    private int size = 0;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    public void clearResume() {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            deleteResume(file);
        }
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void updateResume(File file, Resume resume) {
        try {
            doWrite(resume, file);
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
            doWrite(r, file);
            size++;
        } catch (IOException e) {
            throw new StorageException("The " + file.getName() + " was not saved.", file.getName());
        }
    }

    @Override
    protected Resume getResume(File file) {
        try {
            return doRead(file);
        } catch (IOException e) {
            throw new StorageException("No resume was received", file.getName());
        }
    }

    @Override
    protected void deleteResume(File file) {
        if (!file.delete()) {
            throw new StorageException("The " + file.getName() + " was not deleted", file.getName());
        }
        size--;
    }

    @Override
    public List<Resume> getAll() {
        List<Resume> resumes = new ArrayList<>();
        try {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                resumes.add(doRead(file));
            }
        } catch (IOException e) {
            throw new StorageException("Storage can't return all resumes", "");
        }
        return resumes;
    }

    protected abstract void doWrite(Resume r, File file) throws IOException;

    protected abstract Resume doRead(File file) throws IOException;
}