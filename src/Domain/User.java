package Domain;

public class User {

    public int id;
    public String name;
    public String password;
    public String role;
    public String email;
    public int company_id;

    public User(int id1, String name1, String role1, String email1, String password1, int company_id1) {


        this.id = id1;
        this.name = name1;
        this.password = password1;
        this.role = role1;
        this.email = email1;
        this.company_id = company_id1;

    }

    public String toString()
    {
        return "IM HERE";
        /*
        return "  User_id: " + user_id
                + "  Name: " + name
                + "  Password: " + password
                + "  Role: " + role
                + "  Email: " + email
                + "  Company_id: " +  company_id;
                */
    }

}
