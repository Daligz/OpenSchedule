package me.upp.dali.openschedule.model;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionCallback {
    void execute(final Connection connection) throws SQLException;
}

public interface ConnectionCallback {
    void execute(final Connection connection) throws SQLException;
}

public interface ConnectionCallback {
    void execute(final Connection connection) throws SQLException;
}

public interface ConnectionCallback {
    void execute(final Connection connection) throws SQLException;
}


         public void createTables(final Database database) {
        database.createTable("tbl_user", "userId INTEGER NOT NULL", "name TEXT NOT NULL", "phone TEXT NOT NULL", "PRIMARY KEY(\"userId\" AUTOINCREMENT)");
        database.createTable("tbl_user_time", "userTimeId INTEGER NOT NULL", "phone TEXT NOT NULL", "userCode INTEGER NOT NULL",
                "tiempoInicio DATE DEFAULT (datetime('now', 'localtime'))","tiempoFin DATE", "PRIMARY KEY(\"userTimeId\" AUTOINCREMENT)");
        database.createTable("tbl_config", "configId INTEGER NOT NULL", "id TEXT NOT NULL", "value TEXT NOT NULL", "PRIMARY KEY(\"configId\" AUTOINCREMENT)");
        database.createTable("tbl_inventory", "inventoryId INTEGER NOT NULL", "name TEXT NOT NULL", "admissionDate DATE DEFAULT (datetime('now', 'localtime'))", "state TEXT NOT NULL", "cost REAL NOT NULL", "PRIMARY KEY(\"inventoryId\" AUTOINCREMENT)");
    }
       
