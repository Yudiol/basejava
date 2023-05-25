package com.urise.webapp.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SqlSelectItems {
   void execute(ResultSet resultSet) throws SQLException;
}
