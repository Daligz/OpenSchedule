package me.upp.dali.docman.model;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionCallback {
    void execute(final Connection connection) throws SQLException;
}
