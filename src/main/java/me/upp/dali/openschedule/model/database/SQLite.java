package me.upp.dali.openschedule.model.database;

import lombok.AllArgsConstructor;
import me.upp.dali.openschedule.model.Connector;

import java.sql.*;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class SQLite {

    private final Connector connector;

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
}
