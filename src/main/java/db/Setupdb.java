package db;
import java.sql.*;
public class Setupdb {
    // Setup the database
    public static void main(String[] args) {
        Connection connection = Conversationdb.CreateServer();
        Conversationdb.CreateSchema(connection);
        Conversationdb.DeleteTable(connection,"DROP TABLE IF EXISTS conversations");
        Conversationdb.CreateTable(connection,"CREATE TABLE conversations " + "(id INTEGER not NULL, " + " userid INTEGER not NULL, " + " prompt VARCHAR(255), " + "response VARCHAR(255), " +  " time_stamp TIMESTAMP, " + " PRIMARY KEY ( id ))");
        Conversationdb.CloseDb(connection);
    }

}
