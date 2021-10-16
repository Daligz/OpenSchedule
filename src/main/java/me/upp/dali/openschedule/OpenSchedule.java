package me.upp.dali.openschedule;

import me.upp.dali.openschedule.model.Connector;
import me.upp.dali.openschedule.model.database.DatabaseConnector;
import me.upp.dali.openschedule.model.database.SQLite;

public class OpenSchedule {

    private final Connector connector = new DatabaseConnector();
    private final SQLite sqLite = new SQLite(this.connector);

    public static void main(final String[] args) {
        final OpenSchedule openSchedule = new OpenSchedule();
        openSchedule.createTables();
    }

    private void createTables() {
        this.sqLite.createTable("tbl_user", "UserId INTEGER NOT NULL", "name TEXT NOT NULL", "phone TEXT NOT NULL", "PRIMARY KEY(\"UserId\" AUTOINCREMENT)");
        this.sqLite.createTable("tbl_user_time", "userTimeId INTEGER NOT NULL", "UserId INTEGER NOT NULL", "tiempoInicio DATE DEFAULT CURRENT_TIMESTAMP",
                "tiempoFin DATE", "PRIMARY KEY(\"userTimeId\" AUTOINCREMENT)", "FOREIGN KEY(\"UserId\") REFERENCES tbl_user(\"UserId\")");
        this.sqLite.createTable("tbl_config", "ConfigId INTEGER NOT NULL", "value TEXT NOT NULL", "PRIMARY KEY(\"ConfigId\" AUTOINCREMENT)");
    }
}