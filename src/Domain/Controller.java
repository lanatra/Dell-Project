package Domain;

import DataLayer.DatabaseFacade;

import java.util.ArrayList;

/**
 * Created by Lasse on 09-04-2015.
 */
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
    //User
    public User getUser(String user_id) { return facade.getUser(user_id); }
    //Project
    public ArrayList getProjectsByState(String state) { return  facade.getProjectsByState(state); }




    //User Login / Registration
    public User login(String email, String password) {
        User user = facade.getUserByEmail(email);
            if (Login.testPassword(password, user.password))
                return user;
            else
                return null;
    }

}
