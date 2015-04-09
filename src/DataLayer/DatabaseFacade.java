package DataLayer;

import Domain.User;

/**
 * Created by Lasse on 09-04-2015.
 */
public class DatabaseFacade {

    public User getUserByEmail(String email) {
        return UserMapper.getUserByEmail(email);
    }

}
