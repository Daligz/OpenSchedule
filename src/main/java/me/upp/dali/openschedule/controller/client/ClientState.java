package me.upp.dali.openschedule.controller.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.upp.dali.openschedule.controller.others.AbstractDataMap;

public class ClientState extends AbstractDataMap<ClientState, Integer> {

    private static ClientState INSTANCE;

    public ClientState() {
        INSTANCE = this;
    }

    @Getter
    @AllArgsConstructor
    public static class Client {
        private final int id;
        private final String name, phone;
        private final Status status;
    }

    public static ClientState getInstance() {
        if (INSTANCE == null) INSTANCE = new ClientState();
        return INSTANCE;
    }

    public enum Status {
        NONE, REGISTER_CLIENT, REGISTER_KNOWN_CLIENT, REGISTER_CODE_EXPIRE, REGISTER_FINISHED_TIME
    }
}