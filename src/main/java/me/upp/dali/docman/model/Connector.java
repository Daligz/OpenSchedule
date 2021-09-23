package me.upp.dali.docman.model;

public interface Connector {
    void executeQuery(final ConnectionCallback connectionCallback);
}
