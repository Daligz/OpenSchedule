package me.upp.dali.openschedule;

import me.upp.dali.openschedule.model.Connector;
import me.upp.dali.openschedule.model.database.Database;
import me.upp.dali.openschedule.model.database.DatabaseConnector;
import me.upp.dali.openschedule.model.database.SQLite;

/**
 * @author Dali
 *
 * Open Schedule main class
 */
public class OpenSchedule {

    public static void main(final String[] args) {

    }

    private void testDatabase() {
        final Connector connector = new DatabaseConnector();
        final Database database = new SQLite(connector);
        database.createTable("MyTable", "id INTEGER NOT NULL", "name TEXT NOT NULL", "PRIMARY KEY(\"id\" AUTOINCREMENT)").whenComplete((aBoolean, throwable) -> {
            if (throwable == null && aBoolean) {
                System.out.println("Iniciando insert...");
                database.insert("MyTable", "(name) VALUES (\"Daligz\")").whenComplete((aBoolean1, throwable1) -> {
                    if (throwable1 == null && aBoolean1) {
                        System.out.println("Iniciando get..");
                        database.get("MyTable", "name = \"Daligz\"").whenComplete((o, throwable2) -> {
                            if (throwable2 == null && o != null) {
                                System.out.println(o.toString());
                                System.out.println("Iniciando update..");
                                database.update("MyTable", "name = \"Garcia\"", "name = \"Daligz\"").whenComplete((aBoolean2, throwable3) -> {
                                    System.out.println("Iniciando get..");
                                    database.get("MyTable", "name = \"Garcia\"").whenComplete((o1, throwable4) -> {
                                        if (throwable4 == null && o1 != null) {
                                            System.out.println(o1.toString());
                                            System.out.println("Iniciando delete...");
                                            database.delete("MyTable", "name = \"Garcia\"").whenComplete((aBoolean3, throwable5) -> {
                                                if (aBoolean3 && throwable5 == null) {
                                                    System.out.println("LISTO!");
                                                }
                                            });
                                        }
                                    });
                                });
                            }
                        });
                    }
                });
            }
        });
    }
}