package Domain;

import java.sql.Timestamp;
import java.util.ArrayList;

public class DisplayProject extends Project{
        public long f_start_time;
    public long f_end_time;
    public long f_execution_date;
    public long f_last_change_admin;
    public long f_last_change_partner;

    public String companyName;
    public String companyLogoUrl;

    //public ArrayList<Message> messages;

    //public ArrayList<POE> poes;

    /*public ArrayList<Message> messages;

    public ArrayList<POE> poes;*/


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


        if(project.start_time != null) DP.f_start_time = project.start_time.getTime();
        if(project.end_time != null) DP.f_end_time = project.end_time.getTime();
        if(project.execution_date != null) DP.f_execution_date = project.execution_date.getTime();
        if(project.last_change_admin != null) DP.f_last_change_admin = project.last_change_admin.getTime();
        if(project.last_change_partner != null) DP.f_last_change_admin = project.last_change_partner.getTime();
        Company cp = new Controller().getCompanyById(DP.company_id);

        DP.companyName = cp.name;
        DP.companyLogoUrl = cp.img_filename;

        return DP;
    }

    public long getF_start_time() {
        return f_start_time;
    }

    public long getF_end_time() {
        return f_end_time;
    }

    public long getF_execution_date() {
        return f_execution_date;
    }

    public long getF_last_change_admin() {
        return f_last_change_admin;
    }

    public long getF_last_change_partner() {
        return f_last_change_partner;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }
}
