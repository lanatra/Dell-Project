package Domain;

import sun.plugin2.message.Message;

import java.sql.Timestamp;
import java.util.ArrayList;

public class DisplayProject {

    public int id;
    public Timestamp start_time;
    public Timestamp end_time;
    public int company_id;
    public int owner_id;
    public String status;
    public double budget;
    public String body;
    public Timestamp execution_date;
    public Timestamp last_change_admin;
    public Timestamp last_change_partner;
    public boolean unread_admin;
    public boolean unread_partner;

    public String companyName;
    public String companyLogoUrl;

    public ArrayList<Message> messages;

}
