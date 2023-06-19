package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            deleteItems(connection, resume.getUuid(), "contact");
            deleteItems(connection, resume.getUuid(), "section");
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
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM resume WHERE uuid = ? ")) {
                statement.setString(1, uuid);
                if (statement.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
                return null;
            }
        });
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return sqlHelper.transactionExecute(connection -> {
//          add full_name  
            Resume resume = sqlHelper.addItems(connection, uuid, "uuid", "resume", resultSet -> {
                if (!resultSet.next()) {
                    throw new NotExistStorageException(uuid);
                }
                return new Resume(uuid, resultSet.getString("full_name"));
            });
//          add contacts  
            sqlHelper.addItems(connection, uuid, "resume_uuid", "contact", resultSet -> {
                while (resultSet.next()) {
                    resume.setContact(ContactType.valueOf(resultSet.getString("type")), resultSet.getString("value"));
                }
                return null;
            });
//          add sections  
            sqlHelper.addItems(connection, uuid, "resume_uuid", "section", resultSet -> {
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
                SectionType sectionType = map.getKey();
                StringBuilder title = new StringBuilder();
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE:
                        title.append(map.getValue());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        title.append(String.join("\n", ((ListSection) map.getValue()).getItems()));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        for (Organization organization : ((OrganizationSection) map.getValue()).getOrganizations()) {
                            title.append('\u2207');
                            title.append('\u2021');
                            title.append(organization.getHomePage().getName());
                            title.append('\u2021');
                            title.append(organization.getHomePage().getUrl());
                            for (Period period : organization.getPosts()) {
                                title.append('\u2021');
                                title.append(period.getStartDate().toString());
                                title.append('\u2021');
                                title.append(period.getEndDate().toString());
                                title.append('\u2021');
                                title.append(period.getTitle());
                                title.append('\u2021');
                                title.append(period.getDescription());
                            }
                        }
                        break;
                }
                statement.setString(1, String.valueOf(map.getKey()));
                statement.setString(2, String.valueOf(title));
                statement.setString(3, resume.getUuid());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void deleteItems(Connection connection, String uuid, String table) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " + table + " WHERE resume_uuid =?")) {
            statement.setString(1, uuid);
            statement.executeUpdate();
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
                List<Organization> organizations = new ArrayList<>();
                for (String org : title.split("∇")) {
                    List<Period> periods = new ArrayList<>();
                    if (Objects.equals(org, "")) {
                        continue;
                    }
                    String[] organisation = org.split("‡");
                    int counter = 0;
                    for (int k = 3; k < organisation.length; k += 4) {
                        String start = organisation[3 + (4 * counter)];
                        String end = organisation[4 + (4 * counter)];
                        String position = organisation[5 + (4 * counter)];
                        String description = organisation[6 + (4 * counter)];
                        counter++;
                        periods.add(new Period(convertStringToLocalDate(start), convertStringToLocalDate(end), position, description));
                    }
                    organizations.add(new Organization(organisation[1], organisation[2], periods));
                }
                resume.setSection(sectionType, new OrganizationSection(organizations));
                break;
        }
    }

    private LocalDate convertStringToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);

    }
}
