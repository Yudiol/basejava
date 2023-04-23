package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AbstractPathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final SerializationStorage serializationStorage;

    protected AbstractPathStorage(String dir, SerializationStorage serializationStorage) {
        directory = Paths.get(dir);
        this.serializationStorage = serializationStorage;
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    public void clearResume() throws IOException {
        try {
            Files.list(directory).forEach(this::deleteResume);
        } catch (IOException e) {
            throw new StorageException("Path delete error", "");
        }
    }

    @Override
    public int sizeResume() {
        try {
            return (int) Files.list(directory).count();
        } catch (IOException e) {
            throw new StorageException("Something went wrong. Size", "");
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return Paths.get(String.valueOf(directory), uuid);
    }

    @Override
    protected void updateResume(Path path, Resume resume) {
        try {
            serializationStorage.doWrite(resume, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("The " + path.getFileName() + " was not updated.", "");
        }
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void saveResume(Path path, Resume r) {
        try {
            Files.createFile(path);
            updateResume(path, r);
        } catch (IOException e) {
            throw new StorageException("The " + path.getFileName() + " was not saved.", "");
        }
    }

    @Override
    protected Resume getResume(Path path) {
        try {
            return serializationStorage.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("No resume was received", "");
        }
    }

    @Override
    protected void deleteResume(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("The " + path.getFileName() + " was not deleted", path.getFileName().toString());
        }
    }

    @Override
    public List<Resume> getAll() {
        try {
            return Files.walk(directory).filter(Files::isRegularFile).map(this::getResume).collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("Directory read error", "");
        }
    }
}