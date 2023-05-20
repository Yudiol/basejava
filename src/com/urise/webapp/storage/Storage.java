package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface Storage {

    void clear() throws IOException, SQLException;

    void update(Resume r) throws SQLException;

    void save(Resume r) throws SQLException;

    Resume get(String uuid);

    void delete(String uuid);

    List<Resume> getAllSorted() throws SQLException;

    int size();
}