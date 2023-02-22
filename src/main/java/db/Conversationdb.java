package org.mmda.db;
import java.sql.*;

import java.sql.Timestamp;
public class Conversationdb {

    /**
     * Establishes a connection to the database server.
     *
     * @return A database Connection object that represents a connection to the database server.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection CreateServer() {
        Connection connection = null;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:\\C:\\Users\\redab\\IdeaProjects\\test2_conv\\SRVMMDADB", "SA", "");
            if (connection != null) {
                System.out.println("Connected to the database");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Creates a new schema in the specified database Connection.
     *
     * @param connection A Connection object that represents a connection to the database.
     * @throws SQLException if a database access error occurs or an error is encountered while creating the schema.
     */
    public static void CreateSchema(Connection connection) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("CREATE SCHEMA mmdadb");
            System.out.println("Database created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Creates a new table in the specified database Connection using the given SQL query.
     *
     * @param connection A Connection object that represents a connection to the database.
     * @param query The SQL query to execute to create the new table.
     * @throws SQLException if a database access error occurs or an error is encountered while creating the table.
     */
    public static void CreateTable(Connection connection, String query) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a table from the specified database Connection using the given SQL query.
     *
     * @param connection A Connection object that represents a connection to the database.
     * @param query The SQL query to execute to delete the table.
     * @throws SQLException if a database access error occurs or an error is encountered while deleting the table.
     */
    public static void DeleteTable(Connection connection, String query) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**

     Closes the specified database connection.
     @param connection the database connection to be closed
     */
    public static void CloseDb(Connection connection) {

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**

     Inserts a conversation into the specified database with the given parameters.
     @param connection the database connection to be used for the insertion
     @param id the id of the conversation
     @param userid the id of the user who initiated the conversation
     @param prompt the prompt text of the conversation
     @param response the response text of the conversation
     @param timestamp the timestamp of the conversation
     */
    public static void storeConversation(Connection connection, int id, int userid, String prompt, String response, Timestamp timestamp) {
        Statement stmt = null;
        try {

            stmt = connection.createStatement();

            String sql = "INSERT INTO CONVERSATIONS (id,userid,prompt,response,time_stamp) VALUES (" + Integer.toString(id) + "," + Integer.toString(userid) +",'" + prompt +"','" + response +"','"+ timestamp +"')";
            stmt.executeUpdate(sql);

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }

        }
    }

    /**
     * Retrieves all conversations stored in the database and prints them to the console.
     *
     * @param connection the Connection object representing the database connection
     */
    public static void retrieveConversations(Connection connection) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();

            String sql = "SELECT * FROM CONVERSATIONS ";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String conversation = rs.getString("id") + " ++ " + rs.getString("userid") + " ++ " + rs.getString("prompt") + " ++ " + rs.getString("response") + "++" + rs.getString("time_stamp");
                System.out.println(conversation);
            }
            rs.close();

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {

            }

        }
    }


    /**

     Retrieves all conversations for a given user ID from the "CONVERSATIONS" table in the specified database connection.
     Prints each conversation in the format "id ++ userid ++ prompt ++ response ++ time_stamp" to the console.
     @param connection the connection to the database
     @param userid the ID of the user whose conversations will be retrieved
     @throws SQLException if a database access error occurs
     */
    public static void retrieveConversationsPerUser(Connection connection,int userid) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();

            String sql = "SELECT * FROM CONVERSATIONS WHERE userid =  " + Integer.toString(userid);
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String conversation = rs.getString("id") + " ++ " + rs.getString("userid") + " ++ " + rs.getString("prompt") + " ++ " + rs.getString("response") + "++" + rs.getString("time_stamp");
                System.out.println(conversation);
            }
            rs.close();

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();

            } catch (SQLException se2) {

            }

        }
    }

    /**

     Inserts data into a table in a database using a SQL query.
     @param connection A Connection object representing a database connection.
     @param query A String containing the SQL query to execute.
     @throws SQLException If an error occurs while executing the query or closing the statement.
     */
    public static void InsertTable(Connection connection, String query) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("conversations inserted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}