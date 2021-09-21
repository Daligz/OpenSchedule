package me.upp.dali.docman.model.database;

import lombok.Getter;
import me.upp.dali.docman.model.ConnectionCallback;
import me.upp.dali.docman.model.Connector;

import java.sql.*;

@Getter
public class DatabaseConnector implements Connector {

    private static final String DATABASE_NAME = "docman_storage.db";
    private static final String DATABASE_URL_CONNECTION = "jdbc:sqlite:";

    public DatabaseConnector() { }

    /**
     * Execute queries to database
     *
     * @param callback {@link ConnectionCallback}
     */
    @Override
    public void executeQuery(final ConnectionCallback callback) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL_CONNECTION + DATABASE_NAME)) {
            callback.execute(connection);
        } catch (final SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
