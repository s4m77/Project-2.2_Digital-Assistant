package db;
import java.sql.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Scanner;
public class Simulconversation {

    //This java code simulates a conversation between a user and a system. And thus will not be used afterwards, It is just to
    // give an overall view on how the program interacts with a database to store the conversations and retrieve them later.
    // It consists of two methods, MaxFinder and main.
    public static int MaxFinder(Connection connection) throws SQLException {
        // Execute the query to find the maximum id value
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT MAX(id) AS id FROM convers");
        resultSet.next();
        int maxId = resultSet.getInt("id");

        // Close the database connection
        resultSet.close();
        statement.close();

        return(maxId);
    }
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Connection connection = Conversationdb.CreateServer();

        //int id = MaxFinder(connection) ;
//        while (true) {
//            System.out.println("Enter the userid: (Enter 'exit' to stop)");
//            String user = scanner.nextLine();
//            if (user.equals("exit")) {
//                break;
//            }
//            System.out.println("Enter the prompt text:");
//            String prompt = scanner.nextLine();
//            System.out.println("Enter the response text:");
//            String response = scanner.nextLine();
//            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//
//            //Conversationdb.storeConversationold(connection, ++id, Integer.parseInt(user), prompt,response, timestamp);
//            Conversationdb.storeConversation(connection, prompt,response);
//        }

        System.out.println("Stored conversations:");
        Conversationdb.retrieveConversations(connection);
        Conversationdb.CloseDb(connection);
    }
}