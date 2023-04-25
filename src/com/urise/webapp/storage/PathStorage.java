package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializer.SerializationStorage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final SerializationStorage serializationStorage;

    protected PathStorage(String dir, SerializationStorage serializationStorage) {
        directory = Paths.get(dir);
        this.serializationStorage = serializationStorage;
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    public void clearResume() {
        getListFiles("Path delete error").forEach(this::deleteResume);
    }

    @Override
    public int sizeResume() {
        return (int) getListFiles("Something went wrong. Size").count();
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
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
        } catch (IOException e) {
            throw new StorageException("The " + path.getFileName() + " was not saved.", "");
        }
        updateResume(path, r);
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
        return getListFiles("Directory read error").filter(Files::isRegularFile).map(this::getResume).collect(Collectors.toList());
    }

    private Stream<Path> getListFiles(String error) {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException(error, "");
        }
    }
}