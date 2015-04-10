package Domain;

import java.sql.Timestamp;

/**
 * Created by Lasse on 09-04-2015.
 */
public class Project {

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
    public boolean isUnread_partner() {
        return unread_partner;
    }

    public int getId() {
        return id;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    public Timestamp getEnd_time() {
        return end_time;
    }

    public int getCompany_id() {
        return company_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public String getStatus() {
        return status;
    }

    public double getBudget() {
        return budget;
    }

    public String getBody() {
        return body;
    }

    public Timestamp getExecution_date() {
        return execution_date;
    }

    public Timestamp getLast_change_admin() {
        return last_change_admin;
    }

    public Timestamp getLast_change_partner() {
        return last_change_partner;
    }

    public boolean isUnread_admin() {
        return unread_admin;
    }
    @Override
    public String toString() {
        return "" + id + ": " + body;
    }
}
