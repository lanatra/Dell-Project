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

    public String message;

    //public ArrayList<Message> messages;

    //public ArrayList<POE> poes;

    /*public ArrayList<Message> messages;

    public ArrayList<POE> poes;*/

    public DisplayProject() {

    }

    public DisplayProject(int id, Timestamp start_time, Timestamp end_time, int company_id, int owner_id, String status, double budget, String body, Timestamp execution_date, Timestamp last_change_admin, Timestamp last_change_partner, boolean unread_admin, boolean unread_partner, String notification, String type) {
        super(id, start_time, end_time, company_id, owner_id, status, budget, body, execution_date, last_change_admin, last_change_partner, unread_admin, unread_partner, notification, type);

        if(start_time != null) f_start_time = start_time.getTime();
        if(end_time != null) f_end_time = end_time.getTime();
        if(execution_date != null) f_execution_date = execution_date.getTime();
        if(last_change_admin != null) f_last_change_admin = last_change_admin.getTime();
        if(last_change_partner != null) f_last_change_admin = last_change_partner.getTime();
        Company cp = new Controller().getCompanyById(company_id);

        companyName = cp.name;
        companyLogoUrl = cp.img_filename;
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

    public String getMessage() { return message; }
}
