package com.urise.webapp.storage;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlInterface {
    Object sqlInter(PreparedStatement statement) throws SQLException;
}
