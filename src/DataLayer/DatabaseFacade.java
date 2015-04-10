package DataLayer;

import java.sql.Connection;
import Domain.User;

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

    public boolean createProjectRequest(String budget, String project_body) {
        return new ProjectMapper().createProjectRequest(budget, project_body, con);
    }


    public User getUserByEmail(String email) {
        return new UserMapper().getUserByEmail(email, DatabaseConnection.getInstance().getConnection());
    }


}
