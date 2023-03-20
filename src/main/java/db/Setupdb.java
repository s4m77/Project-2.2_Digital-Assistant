package db;
import java.sql.*;
public class Setupdb {

    public static void displayTableColumns(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getColumns(null, null, tableName, null);
        while (rs.next()) {
            String columnName = rs.getString("COLUMN_NAME");
            System.out.println(columnName);
        }
    }

    public static void main(String[] args) throws SQLException {
        Conversationdb.DeleteTable(Conversationdb.CreateServer(),"DROP SCHEMA IF EXISTS mmdadb CASCADE");
        Connection connection = Conversationdb.CreateServer();
        Conversationdb.CreateSchema(connection);
        //Conversationdb.DeleteTable(connection,"DROP TABLE IF EXISTS conversations");
        Conversationdb.DeleteTable(connection,"DROP TABLE IF EXISTS convers");
        //Conversationdb.CreateTable(connection,"CREATE TABLE conversations " + "(id INTEGER not NULL, " + " userid INTEGER not NULL, " + " prompt VARCHAR(255), " + "response VARCHAR(255), " +  " time_stamp TIMESTAMP, " + " PRIMARY KEY ( id ))");
        Conversationdb.CreateTable(connection,"CREATE TABLE convers " + "(prompt VARCHAR(255), " + "response VARCHAR(255) )");
        displayTableColumns(connection, "convers");
        Conversationdb.CloseDb(connection);
    }

}
