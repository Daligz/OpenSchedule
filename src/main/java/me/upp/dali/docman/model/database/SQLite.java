package me.upp.dali.docman.model.database;

import lombok.AllArgsConstructor;
import me.upp.dali.docman.model.Connector;

import java.sql.*;

@AllArgsConstructor
public class SQLite implements Database {

    private final Connector connector;

    /**
     * Create table from values
     * Usage: createTable("Table", "id integer", "name string");
     *
     * @param table {@link String}
     * @param values {@link String}
     */
    @Override
    public void createTable(final String table, final String... values) {
        connector.executeQuery(connection -> {
            final Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            final StringBuilder queryBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS " + table + " (");
            for (int i = 0; i < values.length; i++) {
                final String value = values[i];
                queryBuilder.append(value);
                if (i == (values.length - 1)) {
                    queryBuilder.append(")");
                } else {
                    queryBuilder.append(", ");
                }
            }
            System.out.println(queryBuilder);
            statement.executeUpdate(queryBuilder.toString());
        });
    }

    @Override
    public boolean tableExists(final String tableName, final Connection connection) throws SQLException {
        final DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getTables(null, null, tableName, null);
        resultSet.last();
        return (resultSet.getRow() > 0);
    }
}
