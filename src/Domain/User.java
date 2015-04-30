package Domain;

public class User {

    public int id;
    public String name;
    public String password;
    public String email;
    public int company_id;
    public boolean deleted;
    public Company company;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return email;
    }

    public int getCompany_id() { return company_id; }

    public boolean getDeleted() { return deleted; }

    public Company getCompany() { return company; }

    public User(int id1, String name1, String email1, String password1, int company_id1, boolean deleted) {

        this.id = id1;
        this.name = name1;
        this.password = password1;
        this.email = email1;
        this.company_id = company_id1;
        this.deleted = deleted;

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
