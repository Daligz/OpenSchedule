package me.upp.dali.openschedule;

import it.auties.whatsapp4j.manager.WhatsappDataManager;
import it.auties.whatsapp4j.whatsapp.WhatsappAPI;
import javafx.application.Application;
import me.upp.dali.openschedule.controller.MessagesAPI;
import me.upp.dali.openschedule.controller.handlers.MessagesHandler;
import me.upp.dali.openschedule.model.Connector;
import me.upp.dali.openschedule.model.database.Database;
import me.upp.dali.openschedule.model.database.DatabaseConnector;
import me.upp.dali.openschedule.model.database.SQLite;
import me.upp.dali.openschedule.view.ViewLoader;

/**
 * @author Dali
 *
 * Open Schedule main class
 */
public class OpenSchedule {

    public static void main(final String[] args) {
        final OpenSchedule openSchedule = new OpenSchedule();

        // Interfaces loader
        new Thread(() -> Application.launch(ViewLoader.class, args)).start();
        System.out.println("Loading...");
        final MessagesAPI messagesAPI = new MessagesAPI();
        final WhatsappAPI whatsappAPI = messagesAPI.getWhatsappAPI();
        final WhatsappDataManager manager = whatsappAPI.manager();
        whatsappAPI.registerListener(new MessagesHandler(manager, whatsappAPI));
        whatsappAPI.connect();

        // Database creation
        final Connector connector = new DatabaseConnector();
        final Database database = new SQLite(connector);
        openSchedule.createTables(database);
    }

    public void createTables(final Database database) {
        database.createTable("tbl_user", "UserId INTEGER NOT NULL", "name TEXT NOT NULL", "phone TEXT NOT NULL", "PRIMARY KEY(\"UserId\" AUTOINCREMENT)");
        database.createTable("tbl_user_time", "userTimeId INTEGER NOT NULL", "UserId INTEGER NOT NULL", "UserCode INTEGER NOT NULL",
                "tiempoInicio DATE DEFAULT CURRENT_TIMESTAMP", "tiempoFin DATE", "PRIMARY KEY(\"userTimeId\" AUTOINCREMENT)",
                "FOREIGN KEY(\"UserId\") REFERENCES tbl_user(\"UserId\")");
        database.createTable("tbl_config", "ConfigId INTEGER NOT NULL", "value TEXT NOT NULL", "PRIMARY KEY(\"ConfigId\" AUTOINCREMENT)");
    }
}