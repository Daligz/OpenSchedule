package me.upp.dali.docman;

import me.upp.dali.docman.model.database.DatabaseConnector;

/**
 * @author Dali
 *
 * Document Management main class
 */
public class DocMan {

    public static void main(String[] args) {
        final DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.createTable("MyTable", "id INTEGER NOT NULL", "name TEXT NOT NULL", "PRIMARY KEY(\"id\" AUTOINCREMENT)");
    }
}