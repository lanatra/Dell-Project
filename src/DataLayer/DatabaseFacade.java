package DataLayer;

import java.sql.Connection;
import java.util.ArrayList;

import Domain.Company;
import Domain.DisplayProject;
import Domain.User;
import Domain.Message;

public class DatabaseFacade {

    private Connection con;

    public DatabaseFacade() {
        con = DatabaseConnection.getInstance().getConnection();
    }

    public User getUserById(int user_id) { return new UserMapper().getUserById(user_id, con); }

    // PROJECT
    public boolean createProjectRequest(String budget, String project_body) {
        return new ProjectMapper().createProjectRequest(budget, project_body, con);
    }
    public DisplayProject getProjectById(int id, int companyId) {
        return new ProjectMapper().getProjectById(id, companyId, con);
    }
    public ArrayList getMessagesByProjectId(int projId) { return new MessageMapper().getMessagesByProjectId(projId, con); }
    public Message postMessage(int userId, int projId, String body) { return new MessageMapper().postMessage(userId, projId, body, con);}
    public void markRead(int id, int companyId) {
        new ProjectMapper().markRead(id, companyId, con);
    }
    public ArrayList getProjectsByState(String state, int companyId) {
        return new ProjectMapper().getProjectsByState(state, companyId, DatabaseConnection.getInstance().getConnection());
    }
    public boolean verifyProjectRequest(String project_id) {
        return new ProjectMapper().verifyProjectRequest(project_id, con);
    }
    public int[] getStatusCounts(int companyId) {
        return new ProjectMapper().getStatusCounts(companyId, con);
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
