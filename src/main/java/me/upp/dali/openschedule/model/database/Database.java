package me.upp.dali.openschedule.model.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public interface Database {

    /**
     * Insert values to the database
     *
     * @param table {@link String}
     * @param values {@link String}
     * return {@link CompletableFuture<Boolean>}
     */
    CompletableFuture<Boolean> insert(final String table, final String values);

    /**
     * Update values from the database
     *
     * @param table {@link String}
     * @param value {@link String}
     * @param where {@link String}
     * return {@link CompletableFuture<Boolean>}
     */
    CompletableFuture<Boolean> update(final String table, final String value, final String where);

    /**
     * Delete values from the database
     *
     * @param table {@link String}
     * @param where{@link String}
     * return {@link CompletableFuture<Boolean>}
     */
    CompletableFuture<Boolean> delete(final String table, final String where);


    /**
     * Get values from the database
     *
     * @param table {@link String}
     * @param where {@link String}
     * return {@link CompletableFuture<Object>}
     */
    CompletableFuture<Object> get(final String table, final String where);

    /**
     * Create table from values
     * Usage: createTable("Table", "id integer", "name string");
     *
     * @param table {@link String}
     * @param values {@link String}
     * return {@link CompletableFuture<Boolean>}
     */
    CompletableFuture<Boolean> createTable(final String table, final String... values);

    /**
     * Check if table exists in the database
     *
     * @param table {@link String}
     * @param connection {@link Connection}
     * @return {@link Boolean}
     * @throws SQLException {@link SQLException} SQLException
     */
    boolean tableExists(final String table, final Connection connection) throws SQLException;
}
