package me.upp.dali.docman.model;

import java.sql.Connection;
import java.sql.SQLException;

public interface Connector {
    void executeQuery(final ConnectionCallback callback);
    void createTable(final String table, final String... values);
    boolean tableExists(final String tableName, final Connection connection) throws SQLException;
}
