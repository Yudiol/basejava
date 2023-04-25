package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializer.SerializationStorage;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileStorage extends AbstractStorage<File> {
    private final File directory;
    private final SerializationStorage serializationStorage;

    public FileStorage(String dir, SerializationStorage serializationStorage) {
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
        Arrays.stream(getListFiles("")).forEach(this::deleteResume);
    }

    @Override
    public int sizeResume() {
        return getListFiles("Something went wrong. Size").length;
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
        } catch (IOException e) {
            throw new StorageException("The " + file.getName() + " was not saved.", file.getName());
        }
        updateResume(file, r);
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
        return Arrays.stream(getListFiles("Directory read error")).map(this::getResume).collect(Collectors.toList());
    }

    private File[] getListFiles(String error) {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException(error, "");
        }
        return files;
    }
}