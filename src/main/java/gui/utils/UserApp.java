package gui.utils;
import java.sql.*;

public class UserApp {

    private Connection con;


    public CurrentUser user;

    public UserApp(Connection connection){
        con = connection;
    }

    public void addUser(String username, String password) {
        try{
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);
            stm.executeUpdate();
            stm.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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

    static class CurrentUser {
        private final String username;
        private final String password;
        public CurrentUser(String username, String password) {
            this.username = username;
            this.password = password;
        }
        public String getUsername() {
            return username;
        }
        public String getPassword() {
            return password;
        }
    }

}

