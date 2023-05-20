package com.urise.webapp.sql;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T> T execute(String sql, SqlExecutor<T> executor) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            return executor.execute(statement);
        } catch (SQLException e) {
            throw ExceptionUtil.convertException(e);
        }
    }

    public <T> T transactionExecute(SqlTransaction<T> executor) {
        try (Connection connection = connectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                T res = executor.execute(connection);
                connection.commit();
                return res;
            } catch (SQLException e) {
                connection.rollback();
                throw ExceptionUtil.convertException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), "");
        }
    }

    public void saveUpdate(Connection connection, String sql1, String sql2, Resume resume) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql1)) {
            statement.setString(1, resume.getFullName());
            statement.setString(2, resume.getUuid());
            if (statement.executeUpdate() == 0) {
                throw new NotExistStorageException(resume.getUuid());
            }
        }
        try (PreparedStatement statement = connection.prepareStatement(sql2)) {
            for (Map.Entry<ContactType, String> map : resume.getContacts().entrySet()) {
                statement.setString(1, map.getValue());
                statement.setString(2, resume.getUuid());
                statement.setString(3, String.valueOf(map.getKey()));
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }
}
