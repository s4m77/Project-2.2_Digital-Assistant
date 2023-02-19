
package org.mmda.db;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;


//This code file defines a JUnit test class called ConversationdbTest for testing a database management class called Conversationdb.
//The purpose of the 4 tests are to verify the correct behavior of the methods of Conversationdb related to storing and retrieving conversations.
class ConversationdbTest {

    private static Connection connection;

    @BeforeAll
    static void setUp() {
        connection = Conversationdb.CreateServer();
        Conversationdb.CreateSchema(connection);
        String createTableQuery = "CREATE TABLE CONVERSATIONS " +
                "(id INTEGER not NULL, " +
                " userid INTEGER not NULL, " +
                " prompt VARCHAR(255), " +
                " response VARCHAR(255), " +
                " time_stamp TIMESTAMP, " +
                " PRIMARY KEY ( id ))";
        Conversationdb.CreateTable(connection, createTableQuery);
    }

    @AfterAll
    static void tearDown() {
        String dropTableQuery = "DROP TABLE CONVERSATIONS";
        Conversationdb.DeleteTable(connection, dropTableQuery);
        Conversationdb.CloseDb(connection);
    }

    @Test
    void testStoreConversation() {
        int id = 1;
        int userId = 2;
        String prompt = "Hello!";
        String response = "Hi there!";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Conversationdb.storeConversation(connection, id, userId, prompt, response, timestamp);
        Conversationdb.retrieveConversations(connection);
    }

    @Test
    void testRetrieveConversations() {
        Conversationdb.retrieveConversations(connection);
    }

    @Test
    void testRetrieveConversationsPerUser() {
        int userId = 2;
        Conversationdb.retrieveConversationsPerUser(connection, userId);
    }

    @Test
    void testInsertTable() {
        String insertQuery = "INSERT INTO CONVERSATIONS (id,userid,prompt,response,time_stamp) VALUES (2,2,'Hello','Hi there!',CURRENT_TIMESTAMP)";
        Conversationdb.InsertTable(connection, insertQuery);
        Conversationdb.retrieveConversations(connection);
    }
}
