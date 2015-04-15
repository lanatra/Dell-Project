package DataLayer;

import java.sql.Connection;
import java.util.ArrayList;

import Domain.Company;
import Domain.DisplayProject;
import Domain.User;
import Domain.Message;

public class DatabaseFacade {

    private Connection getCon() {
        try {
            return DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {};
        return null;
    }

    public User getUserById(int user_id) { return new UserMapper().getUserById(user_id, getCon()); }

    // PROJECT
    public boolean createProjectRequest(String budget, String project_body, User user, String project_type, String execution_date) {
        return new ProjectMapper().createProjectRequest(budget, project_body, user, project_type, execution_date, getCon());

    }
    public boolean changeProjectStatus(String project_id, String new_status, User user) {
        return new ProjectMapper().changeProjectStatus(project_id, new_status, user, getCon());
    }

    public DisplayProject getProjectById(int id, int companyId) {
        return new ProjectMapper().getProjectById(id, companyId, getCon());
    }
    public ArrayList getMessagesByProjectId(int projId) { return new MessageMapper().getMessagesByProjectId(projId, getCon()); }
    public Message postMessage(int userId, int projId, String body) { return new MessageMapper().postMessage(userId, projId, body, getCon());}
    public void markRead(int id, int companyId) {
        new ProjectMapper().markRead(id, companyId, getCon());
    }
    public ArrayList getProjectsByState(String state, int companyId) {
        return new ProjectMapper().getProjectsByState(state, companyId, getCon());
    }
    public int[] getStatusCounts(int companyId) {
        return new ProjectMapper().getStatusCounts(companyId, getCon());
    }
    public ArrayList getStagesByProjectId(int project_id) {
        return  new ProjectMapper().getStagesByProjectId(project_id, getCon());
    }

    // COMPANY
    public Company getCompanyById(int id) {
        return new CompanyMapper().getCompanyById(id, getCon());
    }

    public boolean createCompany(String company_name) { return new CompanyMapper().createCompany(company_name, getCon()); };

    // USER
    public User getUserByEmail(String email) {
        return new UserMapper().getUserByEmail(email, getCon());
    }
    public boolean createUser(String name, String user_role, String user_email, String password, int company_id) {
        return new UserMapper().createUser(name, user_role, user_email, password, company_id, getCon());
    }




}
