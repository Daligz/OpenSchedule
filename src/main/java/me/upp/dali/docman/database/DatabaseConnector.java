package me.upp.dali.docman.database;

import lombok.Getter;

import java.sql.*;

@Getter
public class DatabaseConnector implements Database {

    private static final String DATABASE_NAME = "docman_storage.db";
    private static final String DATABASE_URL_CONNECTION = "jdbc:sqlite:";

    public DatabaseConnector() { }

    @Override
    public void executeQuery(final IConnectionCallback callback) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL_CONNECTION + DATABASE_NAME)) {
            callback.execute(connection);
        } catch (final SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    /**
     * Create table from values
     * Usage: createTable("Table", "id integer", "name string");
     *
     * @param table table
     * @param values values
     */
    @Override
    public void createTable(final String table, final String... values) {
        executeQuery(connection -> {
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
