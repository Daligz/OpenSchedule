package me.upp.dali.docman.model.database;

import lombok.Getter;
import me.upp.dali.docman.model.ConnectionCallback;
import me.upp.dali.docman.model.Connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public class DatabaseConnector implements Connector {

    private static final String DATABASE_NAME = "docman_storage.db";
    private static final String DATABASE_URL_CONNECTION = "jdbc:sqlite:";

    public DatabaseConnector() { }

    /**
     * Execute queries to database asynchronously
     *
     * @param callback {@link ConnectionCallback}
     */
    @Override
    public void executeQuery(final ConnectionCallback callback) {
        new Thread(() -> {
            try (Connection connection = DriverManager.getConnection(DATABASE_URL_CONNECTION + DATABASE_NAME)) {
                callback.execute(connection);
            } catch (final SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }).start();
    }
}
