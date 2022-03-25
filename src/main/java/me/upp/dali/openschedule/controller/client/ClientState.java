package me.upp.dali.openschedule.controller.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.upp.dali.openschedule.controller.others.AbstractDataMap;

import java.sql.Date;
import java.sql.Timestamp;

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
        private String date, name, state, cost, notes;
    }

    public static ClientState getInstance() {
        if (INSTANCE == null) INSTANCE = new ClientState();
        return INSTANCE;
    }

    public enum Status {
        NONE, REGISTER_CLIENT, REGISTER_KNOWN_CLIENT, REGISTER_CODE_EXPIRE, REGISTER_FINISHED_TIME, REGISTER_NAME_REQUEST, YES_NO_REQUEST
    }
}