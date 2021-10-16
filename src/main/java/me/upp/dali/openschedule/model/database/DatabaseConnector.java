package me.upp.dali.openschedule.model.database;

import lombok.Getter;
import me.upp.dali.openschedule.model.ConnectionCallback;
import me.upp.dali.openschedule.model.Connector;

import java.sql.*;

@Getter
public class DatabaseConnector implements Connector {

    private static final String DATABASE_NAME = "openschedule_storage.db";
    private static final String DATABASE_URL_CONNECTION = "jdbc:sqlite:";

    public DatabaseConnector() { }

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
