package DataLayer;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;

import Domain.*;

public class DatabaseFacade {

    private Connection getCon() {
        try {
            return DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {};
        return null;
    }

    public User getUserById(int user_id) { return new UserMapper().getUserById(user_id, getCon()); }

    // PROJECT
    public int createProjectRequest(String budget, String project_body, User user, String project_type, Timestamp execution_date) {
        return new ProjectMapper().createProjectRequest(budget, project_body, user, project_type, execution_date, getCon());

    }
    public boolean changeProjectStatus(int project_id, String new_status, int companyId, int userId) {
        return new ProjectMapper().changeProjectStatus(project_id, new_status, companyId, userId, getCon());
    }

    public DisplayProject getProjectById(int id, int companyId) {
        return new ProjectMapper().getProjectById(id, companyId, getCon());
    }
    public ArrayList getMessagesByProjectId(int projId) { return new MessageMapper().getMessagesByProjectId(projId, getCon()); }
    public Message postMessage(int userId, int projId, String body, int companyId) { return new MessageMapper().postMessage(userId, projId, body, companyId, getCon());}
    public void markRead(int id, int companyId) {
        new ProjectMapper().changeReadStatus(0, id, companyId, getCon());
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
    public void updateChangeDate(int project_id, int company_id) {
        new ProjectMapper().updateChangeDate(project_id, company_id);
    }
    public void updateNotification(int project_id, String notification) {
        new ProjectMapper().updateNotification(project_id, notification);
    }
    public void markUnread(int project_id, int company_id) {
        new ProjectMapper().changeReadStatus(1, project_id, company_id, getCon());
    }

    // COMPANY
    public Company getCompanyById(int id) {
        return new CompanyMapper().getCompanyById(id, getCon());
    }

    public int createCompany(String company_name, String country_code) { return new CompanyMapper().createCompany(company_name, country_code, getCon()); };

    public void updateCompanyLogo(String filename, int id) { new CompanyMapper().updateCompanyLogo(filename, id, getCon()); }


    // USER
    public User getUserByEmail(String email) {
        return new UserMapper().getUserByEmail(email, getCon());
    }
    public boolean createUser(String name, String user_role, String user_email, String password, int company_id) {
        return new UserMapper().createUser(name, user_role, user_email, password, company_id, getCon());
    }

    // POE
    public boolean addPoeFile(int project_id, String filename, int user_id, String filetype, int stage) {
        return new PoeMapper().addPoeFile(project_id, filename, user_id, filetype, stage, getCon());
    }

    public ArrayList<Poe> getPoe(int project_id) {
        return new PoeMapper().getPoe(project_id, getCon());
    }

    public boolean deletePoe(int fileId) {
        return new PoeMapper().deletePoe(fileId, getCon());
    }
    public boolean markDeletePoe(int fileId) {
        return new PoeMapper().markDeletePoe(fileId, getCon());
    }



}
