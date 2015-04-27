package DataLayer;

import java.sql.*;
import java.util.ArrayList;

import Domain.*;

public class UserMapper {


    public User getUserById(int user_id, Connection con) {

        User user = null;
        String SQL = "select * from users where id=?";


        PreparedStatement statement = null;

        ResultSet rs = null;
        try {
            statement = con.prepareStatement(SQL);

            statement.setInt(1, user_id);
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

        } catch (Exception e) {
            System.out.println("Error in UserMapper");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
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


        } catch (Exception e) {
            System.out.println("Error in UserMapper");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }


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
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return false;
    }

    public int getNextUserId(Connection con) {
        PreparedStatement statement = null;
        String SQL = "select MAX(id) from users";
        int id = 0;

        ResultSet rs = null;
        try {
            statement = con.prepareStatement(SQL);
            rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Error in UserMapper - getNextUserId()");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return id + 1;
    }


    public ArrayList<User> getUserByCompanyId(int company_id, Connection con) {

            String SQL = "select * from users where company_id = ?";
            PreparedStatement statement = null;
            ResultSet rs = null;
            ArrayList<User> UserCollection = new ArrayList<>();


            try {
                statement = con.prepareStatement(SQL);

                statement.setInt(1, company_id);

                rs = statement.executeQuery();

                while (rs.next()) {
                    UserCollection.add(new User(rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getInt(6)
                    ));
                }



            } catch (Exception e) {
                System.out.println("error in budgetmapperrerr");
            }finally {
                if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
                if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
                if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
            }


            return UserCollection;
        }





}
