package gui;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserApp {

        private Connection con;

        public UserApp(String url, String userN, String password) throws SQLException {
            con = DriverManager.getConnection(url, userN, password);
        }

        public void addUser(String username, String password) throws SQLException {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);
            stm.executeUpdate();
            stm.close();
        }

        public boolean retrieveUser(String username, String password) throws SQLException {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);
            ResultSet result = stm.executeQuery();
            boolean found = result.next();
            stm.close();
            return found;
        }

        public void close() throws SQLException {
            con.close();
        }
    }

