package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private static Logger LOG = Logger.getLogger(AbstractStorage.class.getName());
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        LOG.info("Clear");
        sqlHelper.execute("DELETE FROM resume", PreparedStatement::executeUpdate);
    }

    @Override
    public void update(Resume resume) throws SQLException {
        LOG.info("Update " + resume);
        sqlHelper.transactionExecute(connection -> {
            sqlHelper.saveUpdate(connection, "UPDATE resume SET full_name = ? WHERE uuid = ?",
                    "UPDATE contact SET  value = ? WHERE resume_uuid = ? AND type = ?", resume);
            return null;
        });
    }

    @Override
    public void save(Resume resume) throws SQLException {
        LOG.info("Save " + resume);
        sqlHelper.transactionExecute(connection -> {
            sqlHelper.saveUpdate(connection, "INSERT INTO resume (full_name, uuid) VALUES (?,?)",
                    "INSERT INTO contact (value,resume_uuid,type) VALUES (?,?,?)", resume);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return sqlHelper.execute("SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid=c.resume_uuid " +
                "WHERE r.uuid = ? ", statement -> {
            statement.setString(1, uuid);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            if (!resultSet.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume resume = new Resume(uuid, resultSet.getString("full_name"));
            do {
                resume.setContact(ContactType.valueOf(resultSet.getString("type")), resultSet.getString("value"));
            } while (resultSet.next());
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        sqlHelper.execute("DELETE FROM resume WHERE uuid = ?", statement -> {
            statement.setString(1, uuid);
            if (statement.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() throws SQLException {
        LOG.info("GetAllSorted");
        List<Resume> resumes = new ArrayList<>();
        return sqlHelper.transactionExecute(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM resume ORDER BY full_name")) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Resume resume;
                    resumes.add(resume = new Resume(resultSet.getString("uuid"), resultSet.getString("full_name")));
                    try (PreparedStatement contacts = connection.prepareStatement("SELECT * FROM contact WHERE resume_uuid = ?");) {
                        contacts.setString(1, resume.getUuid());
                        ResultSet result = contacts.executeQuery();
                        while (result.next()) {
                            resume.setContact(ContactType.valueOf(result.getString("type")), result.getString("value"));
                        }
                    }
                }
            }
            return resumes;
        });
    }

    @Override
    public int size() {
        LOG.info("Size");
        return sqlHelper.execute("SELECT count(*) FROM resume", statement -> {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("count");
        });
    }
}
