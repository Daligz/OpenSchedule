package me.upp.dali.openschedule;

import me.upp.dali.openschedule.model.Connector;
import me.upp.dali.openschedule.model.database.Database;
import me.upp.dali.openschedule.model.database.DatabaseConnector;
import me.upp.dali.openschedule.model.database.SQLite;
import org.junit.Test;

import java.sql.SQLException;

public class DatabaseTest {

    private final Connector connector = new DatabaseConnector();
    private final Database database = new SQLite(this.connector);

    private static final String TBL_TEST = "tbl_test";

    @Test
    public void createDatabase() {
        this.database.createTable(TBL_TEST, "id INTEGER NOT NULL", "name TEXT NOT NULL", "PRIMARY KEY(\"id\" AUTOINCREMENT)")
                .whenComplete((aBoolean, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                        return;
                    }
                    if (aBoolean) {
                        System.out.println("Database created!");
                    } else {
                        System.out.println("Database error!");
                    }
                });
    }

    @Test
    public void insertValues() {
        this.database.insert(TBL_TEST, "(name) VALUES (\"Daligz\"), (\"Daligz2\")")
                .whenComplete((aBoolean, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                        return;
                    }
                    if (aBoolean) {
                        System.out.println("Values inserted!");
                    } else {
                        System.out.println("Insert error!");
                    }
                });
    }

    @Test
    public void getValues() {
        this.database.get(TBL_TEST, "name = \"Daligz\"")
                .whenComplete((resultSet, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                        return;
                    }
                    if (resultSet != null) {
                        System.out.println("Values:");
                        try {
                            final int id = resultSet.getInt("id");
                            final String name = resultSet.getString("name");
                            System.out.println(id + " - " + name);
                        } catch (final SQLException exception) {
                            exception.printStackTrace();
                        }
                    } else {
                        System.out.println("Resulset null!");
                    }
                });
    }

    @Test
    public void updateValues() {
        this.database.update(TBL_TEST, "name = \"DaligzUpdate\"", "name = \"Daligz\"")
                .whenComplete((aBoolean, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                        return;
                    }
                    if (aBoolean) {
                        System.out.println("Values updated!");
                    } else {
                        System.out.println("Update error!");
                    }
                });
    }

    @Test
    public void deleteValues() {
        this.database.delete(TBL_TEST, "name= \"Daligz2\"")
                .whenComplete((aBoolean, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                        return;
                    }
                    if (aBoolean) {
                        System.out.println("Values deleted!");
                    } else {
                        System.out.println("Delete error!");
                    }
                });
    }

    @Test
    public void oldDatabaseTest() {
        final Connector connector = new DatabaseConnector();
        final Database database = new SQLite(connector);
        database.createTable("MyTable", "id INTEGER NOT NULL", "name TEXT NOT NULL", "PRIMARY KEY(\"id\" AUTOINCREMENT)").whenComplete((aBoolean, throwable) -> {
            if (throwable == null && aBoolean) {
                System.out.println("Iniciando insert...");
                database.insert("MyTable", "(name) VALUES (\"Daligz\")").whenComplete((aBoolean1, throwable1) -> {
                    if (throwable1 == null && aBoolean1) {
                        System.out.println("Iniciando get..");
                        database.get("MyTable", "name = \"Daligz\"").whenComplete((resultSet, throwable2) -> {
                            if (throwable2 == null && resultSet != null) {
                                // create object
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