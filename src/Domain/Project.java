package Domain;

import java.sql.Timestamp;

/**
 * Created by Lasse on 09-04-2015.
 */
public class Project {

    int id;
    Timestamp start_time;
    Timestamp end_time;
    int company_id;
    int owner_id;
    String status;
    double budget;
    String body;
    Timestamp execution_date;
    Timestamp last_change_admin;
    Timestamp last_change_partner;
    boolean unread_admin;
    boolean unread_partner;

    public Project(int id, Timestamp start_time, Timestamp end_time, int company_id, int owner_id, String status, double budget, String body, Timestamp execution_date, Timestamp last_change_admin, Timestamp last_change_partner, boolean unread_admin, boolean unread_partner) {

        this.id = id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.company_id = company_id;
        this.owner_id = owner_id;
        this.status = status;
        this.budget = budget;
        this.body = body;
        this.execution_date = execution_date;
        this.last_change_admin = last_change_admin;
        this.last_change_partner = last_change_partner;
        this.unread_admin = unread_admin;
        this.unread_partner = unread_partner;

    }
}
