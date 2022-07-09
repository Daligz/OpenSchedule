package me.upp.dali.openschedule.controller.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.upp.dali.openschedule.controller.others.AbstractDataMap;

import java.sql.Date;
import java.sql.Timestamp;
import me.upp.dali.openschedule.model.database.Database;

public class ClientState extends AbstractDataMap<String, ClientState.Client> {

    private static ClientState INSTANCE;

    public ClientState() {
        INSTANCE = this;
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class Client {
        private String name, phone;
        private Status status;
        private final Code code;
        private final Date date;
        private final String jid;
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class ClientTable {
        private String phone, code;
        private Timestamp start, end;
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class ItemsTable {
        private String id, name;
    }

    public static ClientState getInstance() {
        if (INSTANCE == null) INSTANCE = new ClientState();
        return INSTANCE;
    }

    public enum Status {
        NONE, REGISTER_CLIENT, REGISTER_KNOWN_CLIENT, REGISTER_CODE_EXPIRE, REGISTER_FINISHED_TIME, REGISTER_NAME_REQUEST, YES_NO_REQUEST
    }
    
     public void createTables(final Database database) {
        database.createTable("tbl_user", "userId INTEGER NOT NULL", "name TEXT NOT NULL", "phone TEXT NOT NULL", "PRIMARY KEY(\"userId\" AUTOINCREMENT)");
        database.createTable("tbl_user_time", "userTimeId INTEGER NOT NULL", "phone TEXT NOT NULL", "userCode INTEGER NOT NULL",
                "tiempoInicio DATE DEFAULT (datetime('now', 'localtime'))","tiempoFin DATE", "PRIMARY KEY(\"userTimeId\" AUTOINCREMENT)");
        database.createTable("tbl_config", "configId INTEGER NOT NULL", "id TEXT NOT NULL", "value TEXT NOT NULL", "PRIMARY KEY(\"configId\" AUTOINCREMENT)");
        database.createTable("tbl_inventory", "inventoryId INTEGER NOT NULL", "name TEXT NOT NULL", "admissionDate DATE DEFAULT (datetime('now', 'localtime'))", "state TEXT NOT NULL", "cost REAL NOT NULL", "PRIMARY KEY(\"inventoryId\" AUTOINCREMENT)");
    }
}