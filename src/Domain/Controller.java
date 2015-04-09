package Domain;

import DataLayer.DatabaseFacade;

/**
 * Created by Lasse on 09-04-2015.
 */
public class Controller {

    DatabaseFacade facade;

    public Controller() {

        this.facade = new DatabaseFacade();

    }

    // Readers
    public User getUser(String user_id) {
        return facade.getUser(user_id);
    }

    ;


    //User Login / Registration
    public User login(String email, String password) {
        User user = facade.getUserByEmail(email);
            if (Login.testPassword(password, user.password))
                return user;
            else
                return null;
    }

}
