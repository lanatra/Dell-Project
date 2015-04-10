package Domain;

import DataLayer.DatabaseFacade;

import java.util.ArrayList;

public class Controller {

    DatabaseFacade facade;

    public Controller() {

        this.facade = new DatabaseFacade();

    }
    // Writers
    public boolean createProjectRequest(String budget, String project_body) {
        return facade.createProjectRequest(budget, project_body);
    }


   //Readers
    //User related
    public User getUser(String user_id) { return facade.getUser(user_id); }


    //Project related
    public ArrayList getProjectsByState(String state) { return  facade.getProjectsByState(state); }
    public boolean verifyProjectRequest(String project_id) { return facade.verifyProjectRequest(project_id); }

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
