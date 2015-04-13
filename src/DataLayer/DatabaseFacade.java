package DataLayer;

import java.sql.Connection;
import java.util.ArrayList;

import Domain.Company;
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

    // PROJECT
    public boolean createProjectRequest(String budget, String project_body) {
        return new ProjectMapper().createProjectRequest(budget, project_body, con);
    }
    public ArrayList getProjectsByState(String state, int companyId) {
        return new ProjectMapper().getProjectsByState(state, companyId, DatabaseConnection.getInstance().getConnection());
    }
    public boolean verifyProjectRequest(String project_id) {
        return new ProjectMapper().verifyProjectRequest(project_id, con);
    }

    // COMPANY
    public Company getCompanyById(int id) {
        return new CompanyMapper().getCompanyById(id, con);
    }

    // USER
    public User getUserByEmail(String email) {
        return new UserMapper().getUserByEmail(email, DatabaseConnection.getInstance().getConnection());
    }




}
