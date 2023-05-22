package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    public void update(Resume resume) {
        LOG.info("Update " + resume);
        sqlHelper.transactionExecute(connection -> {
            saveUpdate(connection, resume, "UPDATE resume " +
                                                "   SET full_name = ? " +
                                                " WHERE uuid = ? ");
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM resume " +
                                                                               " WHERE uuid = ? ")) {
                statement.setString(1, resume.getFullName());
                statement.executeUpdate();
            }
            saveResume(connection, resume);
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume);
        sqlHelper.transactionExecute(connection -> {
            saveUpdate(connection, resume, "INSERT INTO resume (full_name, uuid) " +
                                                "VALUES (?, ?) ");
            saveResume(connection, resume);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return sqlHelper.execute("   SELECT * FROM resume r " +
                                     "LEFT JOIN contact c " +
                                     "       ON r.uuid = c.resume_uuid " +
                                     "    WHERE r.uuid = ? ", statement -> {
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
        sqlHelper.execute("DELETE FROM resume " +
                              " WHERE uuid = ? ", statement -> {
            statement.setString(1, uuid);
            if (statement.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("GetAllSorted");
        Map<String, Resume> resumes = new HashMap<>();
        return sqlHelper.transactionExecute(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("  SELECT * FROM resume " +
                                                                               "ORDER BY full_name ");
            ) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String uuid = resultSet.getString("uuid");
                    resumes.put(uuid, new Resume(uuid, resultSet.getString("full_name")));
                }
            }
            try (PreparedStatement contacts = connection.prepareStatement("SELECT * FROM contact")) {
                ResultSet resContact = contacts.executeQuery();
                while (resContact.next()) {
                    String uuid = resContact.getString("resume_uuid");
                    resumes.get(uuid).setContact(ContactType.valueOf(resContact.getString("type")), resContact.getString("value"));
                }
            }
            return resumes.values().stream()
                    .sorted(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid))
                    .collect(Collectors.toList());
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

    public void saveUpdate(Connection connection, Resume resume, String sql1) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql1)) {
            statement.setString(1, resume.getFullName());
            statement.setString(2, resume.getUuid());
            if (statement.executeUpdate() == 0) {
                throw new NotExistStorageException(resume.getUuid());
            }
        }
    }

    public void saveResume(Connection connection, Resume resume) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO contact (type, value, resume_uuid) " +
                                                                           "VALUES (?, ?, ?) ")) {
            for (Map.Entry<ContactType, String> map : resume.getContacts().entrySet()) {
                statement.setString(1, String.valueOf(map.getKey()));
                statement.setString(2, map.getValue());
                statement.setString(3, resume.getUuid());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }
}
