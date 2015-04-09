package DataLayer;

import java.sql.*;
import Domain.*;
/**
 * Created by Lasse on 09-04-2015.
 */
public class UserMapper {

    public User getUser(int user_id, DatabaseConnection con) {
        User user = null;
        String SQL = "select * from users where user_id= ? ";

        PreparedStatement statement = null;

        try {

            statement = con.prepareStatement(SQL);
            statement.setInt(1, user_id);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
            {
                user = new User(user_id,
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
