package com.urise.webapp.sql;

import com.urise.webapp.model.Resume;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SqlResultSet {
    Resume execute(ResultSet resultSet) throws SQLException;
}
