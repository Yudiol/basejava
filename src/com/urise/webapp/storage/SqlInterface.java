package com.urise.webapp.storage;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlInterface<T> {
   T sqlInter(PreparedStatement statement) throws SQLException;
}
