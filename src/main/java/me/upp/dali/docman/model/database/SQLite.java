package me.upp.dali.docman.model.database;

import lombok.AllArgsConstructor;
import me.upp.dali.docman.model.Connector;
import me.upp.dali.docman.model.database.recods.FileDataObject;

import java.sql.*;

@AllArgsConstructor
public class SQLite implements Database {

    private final Connector connector;

    private static final String DB_INSERT_TEMPLATE = "INSERT INTO %s %s";
    private static final String DB_UPDATE_TEMPLATE = "UPDATE %s SET %s WHERE %s";
    private static final String DB_DELETE_TEMPLATE = "DELETE FROM %s WHERE %s";

    /**
     * Insert values
     *
     * @param table {@link String}
     * @param values {@link String} Example: (test1, test2) VALUES ('value1', 'value2')
     */
    @Override
    public void insert(final String table, final String values) {
        this.connector.executeQuery(connection -> {
            final Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            final String query = String.format(DB_INSERT_TEMPLATE, table, values);
            statement.executeUpdate(query);
        });
    }

    /**
     * Update values
     *
     * @param table {@link String}
     * @param value {@link String}
     * @param where {@link String}
     */
    @Override
    public void update(final String table, final String value, final String where) {
        this.connector.executeQuery(connection -> {
            final Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            final String query = String.format(DB_UPDATE_TEMPLATE, table, value, where);
            statement.executeUpdate(query);
        });
    }

    /**
     * Delete values
     *
     * @param table {@link String}
     * @param where{@link String}
     */
    @Override
    public void delete(final String table, final String where) {
        this.connector.executeQuery(connection -> {
            final Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            final String query = String.format(DB_DELETE_TEMPLATE, table, where);
            statement.executeUpdate(query);
        });
    }

    @Override
    public void get(final String table, final String rows, final String where, final GetCallback getCallback) {
        getCallback.execute(new FileDataObject(1, "title", "Description"));
    }

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
