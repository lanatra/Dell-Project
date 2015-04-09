package Domain;

import DataLayer.DatabaseFacade;

/**
 * Created by Lasse on 09-04-2015.
 */
public class Controller {

    DatabaseFacade facade;

    public Controller() {

        this.facade = new DatabaseFacade();

    };






    //User Login / Registration
    public User login(String email, String password) {
        User user = facade.getUserByEmail(email);
        if(Login.testPassword(password, user.password))
            return user;
        else
            return user
    }
}
