package me.upp.dali.openschedule.controller.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.upp.dali.openschedule.controller.others.AbstractDataMap;

public class ClientState extends AbstractDataMap<String, ClientState.Client> {

    private static ClientState INSTANCE;

    public ClientState() {
        INSTANCE = this;
    }

    // database.createTable("tbl_user", "userId INTEGER NOT NULL", "name TEXT NOT NULL", "phone TEXT NOT NULL", "PRIMARY KEY(\"userId\" AUTOINCREMENT)");

    @Getter
    @AllArgsConstructor
    public static class Client {
        private final String name, phone;
        private final Status status;
        private final Code code;
    }

    public static ClientState getInstance() {
        if (INSTANCE == null) INSTANCE = new ClientState();
        return INSTANCE;
    }

    public enum Status {
        NONE, REGISTER_CLIENT, REGISTER_KNOWN_CLIENT, REGISTER_CODE_EXPIRE, REGISTER_FINISHED_TIME
    }
}