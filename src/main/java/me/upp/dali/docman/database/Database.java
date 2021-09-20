package me.upp.dali.docman.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface Database {
    void executeQuery(final IConnectionCallback callback);
    void createTable(final String table, final String... values);
    boolean tableExists(final String tableName, final Connection connection) throws SQLException;
}
