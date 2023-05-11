package gui.utils;
import java.sql.*;

public class UserApp {

        private Connection con;

        public UserApp(Connection connection){
            con = connection;
        }

        public boolean addUser(String username, String password) {
            try{
                String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
                PreparedStatement stm = con.prepareStatement(sql);
                stm.setString(1, username);
                stm.setString(2, password);
                stm.executeUpdate();
                stm.close();
                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

        public boolean retrieveUser(String username, String password) {

            try {
                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement stm = con.prepareStatement(sql);
                stm.setString(1, username);
                stm.setString(2, password);
                ResultSet result = stm.executeQuery();
                boolean found = result.next();
                stm.close();
                return found;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

        public void close() {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        public static void main(String[] args) {

        }
    }

