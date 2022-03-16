package me.upp.dali.openschedule;

public class TestExecutor {

    public static void main(String[] args) {
        final DatabaseTest databaseTest = new DatabaseTest();
        databaseTest.createDatabase();
        sleep();
        databaseTest.insertValues();
        sleep();
        databaseTest.getValues();
        sleep();
        databaseTest.updateValues();
        sleep();
        databaseTest.deleteValues();
    }

    public static void sleep() {
        try {
            Thread.sleep(1500);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    public interface ConnectionCallback {
    void execute(final Connection connection) throws SQLException;
}
    
    
    public interface ConnectionCallback {
    void execute(final Connection connection) throws SQLException;
}
}
