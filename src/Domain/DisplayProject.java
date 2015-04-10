package Domain;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Andreas Poulsen on 10-Apr-15.
 */
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

    public int f_start_time;
    public int f_end_time;
    public int f_execution_date;
    public int f_last_change_admin;
    public int f_last_change_partner;

    public String f_end_time;
    public String f_start_time;
    public String f_start_time;
    public String f_start_time;
    public String f_start_time;
    public String f_start_time;


    public String companyName;
    public String companyLogoUrl;

    public ArrayList<Message> messages;

    public ArrayList<POE> poes;




    public static DisplayProject projectToDisplay(Project project) {
        DisplayProject DP = new DisplayProject();
        DP.id = project.id;
        DP.start_time = project.start_time;
        DP.end_time = project.end_time;
        DP.company_id = project.company_id;
        DP.owner_id = project.owner_id;
        DP.status = project.status;
        DP.budget = project.budget;
        DP.body = project.body;
        DP.execution_date = project.execution_date;
        DP.last_change_admin = project.last_change_admin;
        DP.last_change_partner = project.last_change_partner;
        DP.unread_admin = project.unread_admin;
        DP.unread_partner = project.unread_partner;

        f_start_time
        f_end_time
        f_execution_date
        f_last_change_admin
        f_last_change_partner
    }

}
