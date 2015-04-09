package DataLayer;

import java.sql.Connection;
import Domain.User;
/**
 * Created by Lasse on 09-04-2015.
 */
public class DatabaseFacade {

    private Connection con;

    public DatabaseFacade() {
        con = DatabaseConnection.getInstance().getConnection();
    }

    public User getUser(String user_id) {
        User u;
        u = new UserMapper().getUser(user_id, con);
        return u;
    }


}
