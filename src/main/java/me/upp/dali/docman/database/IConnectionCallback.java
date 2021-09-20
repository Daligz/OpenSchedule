package me.upp.dali.docman.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionCallback {
    void execute(final Connection connection) throws SQLException;
}
