package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private static Logger LOG = Logger.getLogger(AbstractStorage.class.getName());
    private final ConnectionFactory connectionFactory;
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        this.connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        sqlHelper = new SqlHelper(connectionFactory);
    }

    @Override
    public void clear() {
        LOG.info("Clear");
        sqlHelper.tryCatch("DELETE FROM resume", PreparedStatement::executeUpdate);
    }

    @Override
    public void update(Resume r) {
        LOG.info("Update " + r);
        sqlHelper.tryCatch("UPDATE resume SET full_name = ? WHERE uuid = ?", statement -> {
            statement.setString(1, r.getFullName());
            statement.setString(2, r.getUuid());
            if (statement.executeUpdate() == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        LOG.info("Save " + r);
        sqlHelper.tryCatch("INSERT INTO resume (uuid,full_name) VALUES (?,?)", statement -> {
            try {
                if (Objects.equals(get(r.getUuid()).getUuid(), r.getUuid())) {
                    throw new ExistStorageException(r.getUuid());
                }
            } catch (ExistStorageException e) {
                throw new ExistStorageException(r.getUuid());
            } catch (StorageException e) {
            }
            statement.setString(1, r.getUuid());
            statement.setString(2, r.getFullName());
            int i = statement.executeUpdate();
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return (Resume) sqlHelper.tryCatch("SELECT * FROM resume WHERE uuid = ?", statement -> {
            statement.setString(1, uuid);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            if (!resultSet.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, resultSet.getString("full_name"));
        });
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        get(uuid).getUuid();
        sqlHelper.tryCatch("DELETE FROM resume WHERE uuid = ?", statement -> {
            statement.setString(1, uuid);
            statement.executeUpdate();
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("GetAllSorted");
        return (List<Resume>) sqlHelper.tryCatch("SELECT * FROM resume", statement -> {
            ResultSet resultSet = statement.executeQuery();
            List<Resume> resumes = new ArrayList<>();
            while (resultSet.next()) {
                resumes.add(new Resume(resultSet.getString("uuid"), resultSet.getString("full_name")));
            }
            resumes.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
            return resumes;
        });
    }

    @Override
    public int size() {
        return getAllSorted().size();
    }
}
