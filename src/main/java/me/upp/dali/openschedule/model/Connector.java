package me.upp.dali.openschedule.model;

public interface Connector {
    void executeQuery(final ConnectionCallback connectionCallback);
}
