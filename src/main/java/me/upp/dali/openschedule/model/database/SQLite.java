package me.upp.dali.openschedule.model.database;

import lombok.AllArgsConstructor;
import me.upp.dali.openschedule.model.Connector;
import me.upp.dali.openschedule.model.database.recods.FileDataObject;

import java.sql.*;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class SQLite implements Database {

    private final Connector connector;

    private static final String DB_INSERT_TEMPLATE = "INSERT INTO %s %s";
    private static final String DB_UPDATE_TEMPLATE = "UPDATE %s SET %s WHERE %s";
    private static final String DB_DELETE_TEMPLATE = "DELETE FROM %s WHERE %s";
    private static final String DB_SELECT_TEMPLATE = "SELECT * FROM %s WHERE %s";

    /**
     * Insert values
     *
     * @param table {@link String}
     * @param values {@link String} Example: (test1, test2) VALUES ('value1', 'value2')
     */
    @Override
    public CompletableFuture<Boolean> insert(final String table, final String values) {
        final CompletableFuture<Boolean> future = new CompletableFuture<>();
        this.connector.executeQuery(connection -> {
            final Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            final String query = String.format(DB_INSERT_TEMPLATE, table, values);
            System.out.println(query);
            statement.executeUpdate(query);
            future.complete(true);
        });
        return future;
    }

    /**
     * Update values
     *
     * @param table {@link String}
     * @param value {@link String}
     * @param where {@link String}
     */
    @Override
    public CompletableFuture<Boolean> update(final String table, final String value, final String where) {
        final CompletableFuture<Boolean> future = new CompletableFuture<>();
        this.connector.executeQuery(connection -> {
            final Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            final String query = String.format(DB_UPDATE_TEMPLATE, table, value, where);
            System.out.println(query);
            try {
                statement.executeUpdate(query);
            } catch (final SQLException sqlException) {
                future.complete(false);
            } finally {
                future.complete(true);
            }
        });
        return future;
    }

    /**
     * Delete values
     *
     * @param table {@link String}
     * @param where {@link String}
     */
    @Override
    public CompletableFuture<Boolean> delete(final String table, final String where) {
        final CompletableFuture<Boolean> future = new CompletableFuture<>();
        this.connector.executeQuery(connection -> {
            final Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            final String query = String.format(DB_DELETE_TEMPLATE, table, where);
            System.out.println(query);
            try {
                statement.executeUpdate(query);
            } catch (final SQLException sqlException) {
                future.complete(false);
            } finally {
                future.complete(true);
            }
        });
        return future;
    }

    /**
     * Get values
     *
     * @param table {@link String}
     * @param where {@link String}
     */
    @Override
    public CompletableFuture<Object> get(final String table, final String where) {
        final CompletableFuture<Object> future = new CompletableFuture<>();
        this.connector.executeQuery(connection -> {
            final Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            final String query = String.format(DB_SELECT_TEMPLATE, table, where);
            System.out.println(query);
            final ResultSet resultSet = statement.executeQuery(query);
            FileDataObject fileDataObject = null;
            if (resultSet.next()) {
                fileDataObject= new FileDataObject(
                        resultSet.getString(1),
                        resultSet.getString(2)
                );
            }
            future.complete(fileDataObject);
        });
        return future;
    }

    @Override
    public CompletableFuture<Boolean> createTable(final String table, final String... values) {
        final CompletableFuture<Boolean> future = new CompletableFuture<>();
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
            try {
                statement.executeUpdate(queryBuilder.toString());
            } catch (final SQLException sqlException) {
                future.complete(false);
            } finally {
                future.complete(true);
            }
        });
        return future;
    }

    @Override
    public boolean tableExists(final String tableName, final Connection connection) throws SQLException {
        final DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getTables(null, null, tableName, null);
        resultSet.last();
        return (resultSet.getRow() > 0);
    }
}
