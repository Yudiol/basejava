package com.urise.webapp.sql;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public Resume addItems(Connection connection, String uuid, String id, String table, SqlResultSet executor) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE " + id + " = ?")) {
            statement.setString(1, uuid);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            return executor.execute(resultSet);
        }
    }

    public void addAllItems(Connection connection, String sql, SqlSelectItems executor) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                executor.execute(resultSet);
            }
        }
    }
}
