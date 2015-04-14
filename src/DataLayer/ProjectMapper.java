package DataLayer;

import Domain.DisplayProject;
import Domain.Project;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;


/**
 * Created by Lasse on 09-04-2015.
 */
public class ProjectMapper {

    public boolean createProjectRequest(String budget, String project_body, int user_id, Connection con) {

        String SQL = "insert into projects values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        double parsedBudget;
        try {
            parsedBudget = Double.parseDouble(budget);
        } catch (NumberFormatException e) {
            return false;
        }
        PreparedStatement statement = null;
        int companyId = getCompanyIdByUserId(user_id, con);

        try {
            int nextProjectID = getNextProjectId(con);
            statement = con.prepareStatement(SQL);

            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            Timestamp timestamp = new Timestamp(date.getTime());


            statement.setInt(1, nextProjectID);
            statement.setTimestamp(2, timestamp);
            statement.setTimestamp(3, null);
            statement.setInt(4, companyId);
            statement.setInt(5, user_id);
            statement.setString(6, "Pending");
            statement.setDouble(7, parsedBudget);
            statement.setString(8, project_body);
            statement.setTimestamp(9, null);
            statement.setTimestamp(10, null);
            statement.setTimestamp(11, timestamp);
            statement.setBoolean(12, true);
            statement.setBoolean(13, false);
            statement.setString(14, null);
            statement.setString(15, null);

            statement.executeUpdate();


            return true;

        } catch (SQLException t) {
            System.out.println("SQLException in createProjectRequest()");
        }
        catch (Exception e) {
            System.out.println("Error in ProjectMapper - createProjectRequest()");
        }

        return false;
    }

    public int getNextProjectId(Connection con) {
        PreparedStatement statement = null;
        String SQL = "select MAX(id) from projects";
        int id = 0;
        try {
            statement = con.prepareStatement(SQL);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Error in ProjectMapper - getNextProjectId()");
        }

        return id + 1;
    }

    // Will make following function more generic; should be able to change status depending on parameter to reduce amount of methods needed
    public boolean changeProjectStatus(String project_id, String new_status, String user_role, Connection con) {
        PreparedStatement statement = null;
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        Timestamp timestamp = new Timestamp(date.getTime());
        String SQL = "UPDATE projects SET status = '+ newStatus +' where id = ?";

        int parsedId;
        try {
            parsedId = Integer.parseInt(project_id);
        } catch (NumberFormatException e) {
            return false;
        }


        try {
            statement = con.prepareStatement(SQL);

            statement.setInt(1, parsedId);
            statement.executeUpdate();

            updateChangeDate(parsedId, user_role, con);

            return true;
        } catch (Exception e) {
            System.out.println("Zzzzz");
        }
        return false;

    }

    public DisplayProject getProjectById(int id, int companyId, Connection con) {
        String SQL = "select * from projects where id=?";
        DisplayProject project = null;
        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(SQL);
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                project = DisplayProject.projectToDisplay(new Project(rs.getInt(1),
                        rs.getTimestamp(2),
                        rs.getTimestamp(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getString(6),
                        rs.getInt(7),
                        rs.getString(8),
                        rs.getTimestamp(9),
                        rs.getTimestamp(10),
                        rs.getTimestamp(11),
                        rs.getBoolean(12),
                        rs.getBoolean(13),
                        rs.getString(14),
                        rs.getString(15)
                ));
            } else {
                project.message = "A project with the id " + id + "doesn't exist.";
            }

        } catch (Exception e) {
            System.out.println("Error in UserMapper");
        }

        if(project != null)
            if(companyId != 1)
                if(project.getCompany_id() != companyId) { //unauthorized access
                    project = new DisplayProject();
                    project.message = "You do not have permission to view this project";
                }
        return project;
    }

    public void markRead(int id, int companyId, Connection con) {
        String SQL = "";
        if(companyId == 1)
             SQL = "update projects set unread_admin=0 where id=?";
        else
            SQL = "update projects set unread_partner=0 where id=? and company_id=?";

        PreparedStatement statement = null;
        int res = 0;
        try {
            statement = con.prepareStatement(SQL);
            statement.setInt(1, id);
            if(companyId != 1)
                statement.setInt(2, companyId);

            res = statement.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error in markRead");
        }

    }

    public ArrayList getProjectsByState(String state, int companyId, Connection con) {
        ArrayList<Project> projects = new ArrayList<>();

        String SQL;
        if(companyId == 1) { // Request from Dell user
            if(state.equals("waitingForAction"))
                SQL = "select * from projects where status='Waiting Project Verification' or status='Waiting Claim Verification'";
            else
                SQL = "select * from projects where status= ?";
        } else {
            if(state.equals("waitingForAction"))
                SQL = "select * from projects where status='Project Verified' and company_id=?";
            else
                SQL = "select * from projects where status= ? and company_id=?";
        }


        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(SQL);
            if(companyId != 1) {
                if(state.equals("waitingForAction"))
                    statement.setInt(1, companyId);
                else {
                    statement.setString(1, state);
                    statement.setInt(2, companyId);
                }
            } else if(!state.equals("waitingForAction"))
                statement.setString(1, state);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                projects.add(new Project(rs.getInt(1),
                        rs.getTimestamp(2),
                        rs.getTimestamp(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getString(6),
                        rs.getInt(7),
                        rs.getString(8),
                        rs.getTimestamp(9),
                        rs.getTimestamp(10),
                        rs.getTimestamp(11),
                        rs.getBoolean(12),
                        rs.getBoolean(13),
                        rs.getString(14),
                        rs.getString(15)
                        ));
            }

        } catch (Exception e) {
            System.out.println("Error in UserMapper");
        }

        ArrayList<DisplayProject> DisplayProjects = new ArrayList<>();
        for (Project proj : projects) {
            DisplayProjects.add(DisplayProject.projectToDisplay(proj));
        }

        return DisplayProjects;
    }

    public int[] getStatusCounts(int companyId, Connection con) {
        PreparedStatement statement = null;
        String SQL = "";
        if(companyId == 1) {
            SQL = "select " +
                    "  sum(case when status='Waiting Project Verification' or status='Waiting Claim Verification' then 1 else 0 end) WaitingForAction," +
                    "  sum(case when status='In Execution' then 1 else 0 end) InExecution," +
                    "  sum(case when status='Finished' then 1 else 0 end) Finished" +
                    " from projects";
        } else {
            SQL = "select \n" +
                    "  sum(case when (status='Waiting Project Verification' or status='Waiting Claim Verification') and company_id=" + companyId + " then 1 else 0 end) WaitingForAction,\n" +
                    "  sum(case when status='In Execution' and company_id=" + companyId + "  then 1 else 0 end) InExecution,\n" +
                    "  sum(case when status='Finished' and company_id=" + companyId + "   then 1 else 0 end) Finished\n" +
                    " from projects";
        }

        int[] res = new int[3];
        System.out.println(SQL);

        try {
            statement = con.prepareStatement(SQL);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                res[0] = rs.getInt(1);
                res[1] = rs.getInt(2);
                res[2] = rs.getInt(3);
            }
            System.out.println(res[0]);
        } catch (Exception e) {
            System.out.println("Error in ProjectMapper - getStatusCounts()");
        }

        return res;
    }





    public void updateChangeDate(int parsedId, String usertype, Connection con) {
        if (usertype.equals("Dell")) {
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            Timestamp timestamp = new Timestamp(date.getTime());
            PreparedStatement statement = null;
            String SQL = "UPDATE projects SET last_change_admin = ? where id = ? ";
            try {
                statement = con.prepareStatement(SQL);
                statement.setTimestamp(1, timestamp);
                statement.setInt(2, parsedId);
                statement.executeUpdate();

            } catch (Exception e) {
                System.out.println("Error in updateChangeDate()");
            }
        } else {
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            Timestamp timestamp = new Timestamp(date.getTime());
            PreparedStatement statement = null;
            String SQL = "UPDATE projects SET last_change_partner = ? where id = ? ";
            try {
                statement = con.prepareStatement(SQL);
                statement.setTimestamp(1, timestamp);
                statement.setInt(2, parsedId);
                statement.executeUpdate();

            } catch (Exception e) {
                System.out.println("Error in updateChangeDate()");
            }

        }
        }

        public int getCompanyIdByUserId(int user_id, Connection con) {

            PreparedStatement statement = null;
            String SQL = "select companies.id from companies, users where users.company_id = companies.id and users.id = ?";

            try {
                statement = con.prepareStatement(SQL);
                statement.setInt(1, user_id);

                ResultSet rs = statement.executeQuery(SQL);

                while (rs.next()) {
                    return rs.getInt(1);
                }

            } catch (Exception e) {
                System.out.println("Error in getCompanyIdByUserId() in projectMapper");

        }

            return 0;


    }

}
