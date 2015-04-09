package DataLayer;

import java.sql.*;
import Domain.*;
/**
 * Created by Lasse on 09-04-2015.
 */
public class UserMapper {


    public User getUser(String user_id, Connection con) {

        User user = null;
        String SQL = "select * from intellij where user_id=?";


        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(SQL);

            statement.setString(1, user_id);
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

        try {
            statement = con.prepareStatement(SQL);
            statement.setString(1, email);
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

}
