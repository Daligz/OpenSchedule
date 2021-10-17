package me.upp.dali.openschedule;

import me.upp.dali.openschedule.view.ViewLoader;

/**
 * @author Dali
 *
 * Open Schedule main class
 */
public class OpenSchedule {

    public static void main(final String[] args) {
        ViewLoader.main(args);
    }

    private void createTables() {
//        this.sqLite.createTable("tbl_user", "UserId INTEGER NOT NULL", "name TEXT NOT NULL", "phone TEXT NOT NULL", "PRIMARY KEY(\"UserId\" AUTOINCREMENT)");
//        this.sqLite.createTable("tbl_user_time", "userTimeId INTEGER NOT NULL", "UserId INTEGER NOT NULL", "tiempoInicio DATE DEFAULT CURRENT_TIMESTAMP",
//                "tiempoFin DATE", "PRIMARY KEY(\"userTimeId\" AUTOINCREMENT)", "FOREIGN KEY(\"UserId\") REFERENCES tbl_user(\"UserId\")");
//        this.sqLite.createTable("tbl_config", "ConfigId INTEGER NOT NULL", "value TEXT NOT NULL", "PRIMARY KEY(\"ConfigId\" AUTOINCREMENT)");
    }
}