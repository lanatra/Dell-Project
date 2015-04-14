package Domain;

import DataLayer.DatabaseFacade;

import java.util.ArrayList;

public class Controller {

    DatabaseFacade facade;

    public Controller() {

        this.facade = new DatabaseFacade();

    }
    // Writers
    public boolean createProjectRequest(String budget, String project_body, int user_id) {
        return facade.createProjectRequest(budget, project_body, user_id);
    }
    public boolean createCompany(String company_name) {
        return facade.createCompany(company_name);
    }

    //User related
    public boolean createUser(String name, String user_role, String user_email, String password, int company_id) {
        return facade.createUser(name, user_role, user_email, password, company_id);
    }

   //Readers



    //Project related
    public ArrayList getProjectsByState(String state, int companyId) { return  facade.getProjectsByState(state, companyId); }
    public boolean changeProjectStatus(String project_id, String new_status, String usertype) { return facade.verifyProjectRequest(project_id, new_status, usertype); }
    public int[] getStatusCounts(int companyId) { return facade.getStatusCounts(companyId); }

    public Company getCompanyById(int id) { return facade.getCompanyById(id); }

    //User Login / Registration
    public User login(String email, String password) {
        User user = facade.getUserByEmail(email);
            if (Login.testPassword(password, user.password))
                return user;
            else
                return null;
    }

}
