package db;
import java.sql.*;

import java.sql.Timestamp;
public class Conversationdb {

    /**
     * Establishes a connection to the database server.
     *
     * @return A database Connection object that represents a connection to the database server.
     */
    public static Connection CreateServer() {
        Connection connection = null;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            // current folder
            String gdbserver = "jdbc:hsqldb:file:" + System.getProperty("user.dir") + "/src/main/resources/dbserver/SRVMMDADB";
            connection = DriverManager.getConnection(gdbserver, "SA", "");

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
     @param prompt the prompt text of the conversation
     @param response the response text of the conversation
     @param userid the userid of the user who participated in the conversation
     */
    public static void storeConversation(Connection connection, String prompt, String response, int userid) {
        Statement stmt = null;
        try {


            String sql = "INSERT INTO CONVERS (PROMPT, RESPONSE, USERID) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, prompt);
            pstmt.setString(2, response);
            pstmt.setInt(3, userid);
            pstmt.executeUpdate();


        } catch (Exception se) {
            se.printStackTrace();
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

            String sql = "SELECT * FROM CONVERS ";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String conversation = rs.getString("prompt") + " ++ " + rs.getString("response") + " ++ " + rs.getInt("userid");
                System.out.println(conversation);
            }
            rs.close();

        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException ignored) {

            }

        }
    }


    /**

     Retrieves all conversations for a given user ID from the "CONVERSATIONS" table in the specified database connection.
     Prints each conversation in the format "id ++ userid ++ prompt ++ response ++ time_stamp" to the console.
     @param connection the connection to the database
     @param userid the ID of the user whose conversations will be retrieved
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

        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();

            } catch (SQLException ignored) {

            }

        }
    }

    /**

     Inserts data into a table in a database using a SQL query.
     @param connection A Connection object representing a database connection.
     @param query A String containing the SQL query to execute.
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



    public static void addUser(Connection connection, String username, String password) throws SQLException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setString(1, username);
        stm.setString(2, password);
        stm.executeUpdate();
        stm.close();
    }

    public static boolean retrieveUser(Connection connection, String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setString(1, username);
        stm.setString(2, password);
        ResultSet result = stm.executeQuery();
        boolean found = result.next();
        stm.close();
        return found;
    }

    public static void retrieveAllUsers(Connection connection) throws SQLException {
        String sql = "SELECT * FROM users ";
        PreparedStatement stm = connection.prepareStatement(sql);
        ResultSet result = stm.executeQuery();
        while (result.next()) {
            String user = result.getString("username") + " ++ " + result.getString("password");
            System.out.println(user);
        }
        stm.close();
    }

    public static int getCurrentUserId(Connection connection, String username, String password) {

        int userid = 0;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();

            String sql = "SELECT ID FROM users WHERE username =  " + "'" + username + "'" + " AND password = " + "'" + password + "'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                userid = rs.getInt("id");
            }
            rs.close();

        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();

            } catch (SQLException ignored) {

            }

        }
        return userid;
    }




}