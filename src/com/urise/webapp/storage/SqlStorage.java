package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.*;
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
    public void update(Resume resume) {
        LOG.info("Update " + resume);
        sqlHelper.transactionExecute(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE resume " +
                                                                               "   SET full_name = ? " +
                                                                               " WHERE uuid = ? ")) {
                statement.setString(1, resume.getFullName());
                statement.setString(2, resume.getUuid());
                if (statement.executeUpdate() == 0) {
                    throw new NotExistStorageException(resume.getUuid());
                }
            }
            deleteItems(connection, resume.getUuid(), "resume_uuid","contact");
            deleteItems(connection, resume.getUuid(), "resume_uuid","section");
            insertContacts(connection, resume);
            insertSections(connection, resume);
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume);
        sqlHelper.transactionExecute(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO resume (full_name, uuid) " +
                    "VALUES (?, ?) ")) {
                statement.setString(1, resume.getFullName());
                statement.setString(2, resume.getUuid());
                statement.execute();
            }
            insertContacts(connection, resume);
            insertSections(connection, resume);
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        sqlHelper.transactionExecute(connection -> {
            deleteItems(connection, uuid, "uuid","resume");
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return sqlHelper.transactionExecute(connection -> {
//          add full_name  
            Resume resume = sqlHelper.addItems(connection,  uuid,"uuid","resume", resultSet -> {
                if (!resultSet.next()) {
                    throw new NotExistStorageException(uuid);
                }
                return new Resume(uuid, resultSet.getString("full_name"));
            });
//          add contacts  
            sqlHelper.addItems(connection,  uuid,"resume_uuid","contact", resultSet -> {
                while (resultSet.next()) {
                    resume.setContact(ContactType.valueOf(resultSet.getString("type")), resultSet.getString("value"));
                }
                return null;
            });
//          add sections  
            sqlHelper.addItems(connection,  uuid,"resume_uuid","section", resultSet -> {
                while (resultSet.next()) {
                    getSections(resultSet, resume);
                }
                return null;
            });
            return resume;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("GetAllSorted");
        Map<String, Resume> resumes = new LinkedHashMap<>();
        return sqlHelper.transactionExecute(connection -> {
//          add all full_name  
            sqlHelper.addAllItems(connection, "  SELECT * FROM resume " +
                                                  "ORDER BY full_name", res -> {
                String uuid = res.getString("uuid");
                resumes.put(uuid, new Resume(uuid, res.getString("full_name")));
            });
//          add all contacts  
            sqlHelper.addAllItems(connection, "SELECT * FROM contact", res -> {
                resumes.get(res.getString("resume_uuid")).setContact(ContactType.valueOf(res.getString("type")), res.getString("value"));
            });
//          add all sections  
            sqlHelper.addAllItems(connection, "SELECT * FROM section", resultSet -> {
                getSections(resultSet, resumes.get(resultSet.getString("resume_uuid")));
            });
            return new ArrayList<>(resumes.values());
        });
    }

    @Override
    public int size() {
        LOG.info("Size");
        return sqlHelper.execute("SELECT count(*) " +
                                     "  FROM resume", statement -> {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("count");
        });
    }

    public void insertContacts(Connection connection, Resume resume) throws SQLException {
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

    public void insertSections(Connection connection, Resume resume) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO section (type, title, resume_uuid) " +
                "VALUES (?, ?, ?) ")) {
            for (Map.Entry<SectionType, Section> map : resume.getSections().entrySet()) {
                Section section = map.getValue();
                String title = section instanceof ListSection ? String.join("\n", ((ListSection) section).getItems()): section.toString();
                statement.setString(1, String.valueOf(map.getKey()));
                statement.setString(2, title);
                statement.setString(3, resume.getUuid());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void deleteItems(Connection connection, String uuid, String id,String table) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " +table +
                                                                           " WHERE " + id + " = ? ")) {
            statement.setString(1, uuid);
            if (statement.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
        }
    }

    private void getSections(ResultSet resultSet, Resume resume) throws SQLException {
        SectionType sectionType = SectionType.valueOf(resultSet.getString("type"));
        String title = resultSet.getString("title");
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                resume.setSection(sectionType, new TextSection(title));
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                resume.setSection(sectionType, new ListSection(Arrays.asList(title.split("\n"))));
                break;
            case EXPERIENCE:
            case EDUCATION:
                break;
        }
    }
}
