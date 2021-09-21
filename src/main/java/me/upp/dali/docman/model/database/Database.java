package me.upp.dali.docman.model.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface Database {

    /**
     * Insert values to the database
     *
     * @param table {@link String}
     * @param values {@link String}
     */
    void insert(final String table, final String... values);

    /**
     * Update values from the database
     *
     * @param table {@link String}
     * @param row {@link String}
     * @param operator {@link String}
     * @param value {@link String}
     * @param where {@link String}
     */
    void update(final String table, final String row, final String operator, final String value, final String where);

    /**
     * Delete values from the database
     *
     * @param table
     * @param where
     */
    void delete(final String table, final String where);

    /**
     * Get values from the database
     *
     * @param table {@link String}
     * @param rows {@link String}
     * @param where {@link String}
     * @param getCallback {@link GetCallback}
     */
    void get(final String table, final String rows, final String where, final GetCallback getCallback);

    /**
     * Create table from values
     * Usage: createTable("Table", "id integer", "name string");
     *
     * @param table {@link String}
     * @param values {@link String}
     */
    void createTable(final String table, final String... values);

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
