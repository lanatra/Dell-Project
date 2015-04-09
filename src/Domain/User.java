package Domain;

/**
 * Created by Lasse on 09-04-2015.
 */
public class User {

    int user_id;
    String name;
    String password;
    String role;
    String email;
    int company_id;

    public User(int user_id, String name, String password, String role, String email, int company_id) {

        this.user_id = user_id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.email = email;
        this.company_id = company_id;

    }

}
