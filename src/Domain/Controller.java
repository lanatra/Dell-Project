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

}
