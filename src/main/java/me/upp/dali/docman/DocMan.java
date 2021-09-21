package me.upp.dali.docman;

import me.upp.dali.docman.model.Connector;
import me.upp.dali.docman.model.database.Database;
import me.upp.dali.docman.model.database.DatabaseConnector;
import me.upp.dali.docman.model.database.SQLite;

/**
 * @author Dali
 *
 * Document Management main class
 */
public class DocMan {

    public static void main(String[] args) {
        final Connector connector = new DatabaseConnector();
        final Database database = new SQLite(connector);
        database.createTable("MyTable", "id INTEGER NOT NULL", "name TEXT NOT NULL", "PRIMARY KEY(\"id\" AUTOINCREMENT)");
    }
}