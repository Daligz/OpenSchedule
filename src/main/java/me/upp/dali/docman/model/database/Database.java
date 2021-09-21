package me.upp.dali.docman.model.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface Database {
    void createTable(final String table, final String... values);
    boolean tableExists(final String tableName, final Connection connection) throws SQLException;
}
