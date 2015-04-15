package DataLayer;

import java.sql.*;
import Domain.*;
/**
 * Created by Lasse on 09-04-2015.
 */
public class UserMapper {


    public User getUserById(int user_id, Connection con) {

        User user = null;
        String SQL = "select * from users where id=?";


        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(SQL);

            statement.setInt(1, user_id);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
            {

                user = new User(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6));

            }

        } catch (Exception e) {
            System.out.println("Error in UserMapper");
        }

        return user;
    }

    public User getUserByEmail(String email, Connection con) {
        User user = null;
        String SQL = "select * from users where email= ? ";

        PreparedStatement statement = null;
        System.out.println(SQL);

        ResultSet rs = null;
        try {
            statement = con.prepareStatement(SQL);
            statement.setString(1, email);
            rs = statement.executeQuery();
            if (rs.next())
            {
                user = new User(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6));
            }

        } catch (SQLException e) {
            System.out.println("sql error");
        }

        catch (Exception e) {
            System.out.println("Error in UserMapper");
        }
        /*
        finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {};
            try { if (statement != null) statement.close(); } catch (Exception e) {};
            try { if (con != null) con.close(); } catch (Exception e) {};
        }
    */
        return user;
    }
    
    public boolean createUser(String name, String user_role, String user_email, String password, int company_id, Connection con) {
        String SQL = "insert into users values (?, ?, ?, ?, ? ,?)";

        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(SQL);
            int nextUserId = getNextUserId(con);

            statement.setInt(1, nextUserId);
            statement.setString(2, name);
            statement.setString(3, user_role);
            statement.setString(4, user_email);
            statement.setString(5, password);
            statement.setInt(6, company_id);

            statement.executeUpdate();

            return true;
        } catch (Exception e) {
            System.out.println("error in user creation");
        }

        return false;
    }

    public int getNextUserId(Connection con) {
        PreparedStatement statement = null;
        String SQL = "select MAX(id) from users";
        int id = 0;
        try {
            statement = con.prepareStatement(SQL);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Error in UserMapper - getNextUserId()");
        }

        return id + 1;
    }




}
