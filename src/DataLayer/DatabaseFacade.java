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
    public int createProjectRequest(int budget, String project_body, User user, String project_type, Timestamp execution_date) {
        return new ProjectMapper().createProjectRequest(budget, project_body, user, project_type, execution_date, getCon());

    }
    public ArrayList<Project> getProjectsByCompanyId(int company_id) {
        return new ProjectMapper().getProjectsByCompanyId(company_id, getCon());
    }
    public ArrayList<Project> getProjectsByUserId(int user_id) {
        return new ProjectMapper().getProjectsByCompanyId(user_id, getCon());
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
    public ArrayList getProjectsByType(String type, int companyId) {
        return new ProjectMapper().getProjectsByType(type, companyId, getCon());
    }
    public ArrayList getProjectsByCompanyName(String companyName, int companyId) {
        return new ProjectMapper().getProjectsByCompanyName(companyName, companyId, getCon());
    }

    //Search
    public ArrayList search(String q, int companyId) {
        return new ProjectMapper().search(q, companyId, getCon());
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

    public ArrayList getDistinctStatuses(String query) {
        return new ProjectMapper().getDistinctStatuses(query, getCon());
    }
    public ArrayList getDistinctTypes(String query, int companyId) {
        return new ProjectMapper().getDistinctTypes(query, companyId, getCon());
    }

    // COMPANY
    public Company getCompanyById(int id) {
        return new CompanyMapper().getCompanyById(id, getCon());
    }

    public int createCompany(String company_name, String country_code) { return new CompanyMapper().createCompany(company_name, country_code, getCon()); };

    public void updateCompanyLogo(String filename, int id) { new CompanyMapper().updateCompanyLogo(filename, id, getCon()); }

    public ArrayList<Company> getCompanies() { return new CompanyMapper().getCompanies(getCon()); }
    public ArrayList<Company> getCompanyNames(String query, int companyId) { return new CompanyMapper().getCompanyNames(query, companyId, getCon()); }
    public int getCompanyIdByName(String name) {return new CompanyMapper().getCompanyIdByName(name);}

    // USER
    public User getUserByEmail(String email) {
        return new UserMapper().getUserByEmail(email, getCon());
    }
    public int createUser(String name,String user_email, String password, int company_id) {
        return new UserMapper().createUser(name, user_email, password, company_id, getCon());
    }
    public ArrayList<User> getUsersByCompanyId(int company_id) {
        return new UserMapper().getUsersByCompanyId(company_id, getCon());
    }
    public ArrayList getUserInfoInvolvedInProjectById(int project_id, int user_id) { return new UserMapper().getUserInfoInvolvedInProjectById(project_id, user_id, getCon()); }

    public ArrayList<User> getUsers() {
        return new UserMapper().getUsers(getCon());
    }

    public boolean markUserDeleted(int user_id) {
        return new UserMapper().markUserDeleted(user_id, getCon());
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

    // BUDGET
    public boolean addBudget(int year, int quarter, int budget) {
    return new BudgetMapper().addBudget(year, quarter, budget, getCon());
    }

    public boolean modifyBudget(int new_budget, int year, int quarter) {
    return new BudgetMapper().modifyBudget(new_budget, year, quarter, getCon());
    }

    public ArrayList<Budget> getActiveBudget(int year, int quarter) {
    return new BudgetMapper().getActiveBudget(year, quarter, getCon());
    }

    public ArrayList<Budget> getAllBudgets() {
        return new BudgetMapper().getAllBudgets(getCon());
    }

    public int getAvailableFunds(int year, int quarter) {
        return new BudgetMapper().getAvailableFunds(year, quarter, getCon());
    }

    // Nonce
    public int addNonce(Nonce nonce) { return new NonceMapper().addNonce(nonce, getCon());}
    public int getUserIdByNonce(String nonce) { return new NonceMapper().getUserIdByNonce(nonce, getCon());}
    public boolean createPassword(int id, String password) { return new UserMapper().createPassword(id, password, getCon()); }
    public void deleteNonce(String nonce) { new NonceMapper().deleteNonce(nonce, getCon()); }


    // Statistics
    public ArrayList<String[]> getDistinctTypesCounts() { return StatisticsGetter.getDistinctTypesCounts(getCon()); }
}
