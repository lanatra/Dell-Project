package DataLayer;

import Domain.DisplayProject;
import Domain.Project;
import Domain.Stage;
import Domain.User;

import javax.xml.crypto.Data;
import javax.xml.transform.Result;
import java.security.interfaces.RSAKey;
import java.sql.*;
import java.util.ArrayList;


public class ProjectMapper {


    public boolean createProjectRequest(String budget, String project_body, User user, String project_type, Timestamp execution_date, Connection con) {

        String SQL = "insert into projects values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        double parsedBudget;
        try {
            parsedBudget = Double.parseDouble(budget);
        } catch (NumberFormatException e) {
            return false;
        }
/* Figure out how to handle dates from frontend; would be ideal if we could have input such that we can convert it to timestamp instead of int
        int execDateParsed;
        try {
            execDateParsed = Integer.parseInt(execution_date);
        } catch (NumberFormatException e) {
            return false;
        }
*/
        System.out.println("IM HERE");
        PreparedStatement statement = null;

        try {
            int nextProjectID = getNextProjectId();
            statement = con.prepareStatement(SQL);

            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            Timestamp timestamp = new Timestamp(date.getTime());


            statement.setInt(1, nextProjectID);
            statement.setTimestamp(2, timestamp);
            statement.setTimestamp(3, null);
            statement.setInt(4, user.company_id);
            statement.setInt(5, user.id);
            statement.setString(6, "Waiting Project Verification");
            statement.setDouble(7, parsedBudget);
            statement.setString(8, project_body);
            statement.setTimestamp(9, execution_date);
            statement.setTimestamp(10, null);
            statement.setTimestamp(11, timestamp);
            statement.setBoolean(12, true);
            statement.setBoolean(13, false);
            statement.setString(14, "New Project Request");
            statement.setString(15, project_type);

            statement.executeUpdate();

            
            addStage(user.id, nextProjectID, "Waiting Project Verification", DatabaseConnection.getInstance().getConnection());

            return true;



        } catch (SQLException t) {
            System.out.println("SQLException in createProjectRequest()");
        }
        catch (Exception e) {
            System.out.println("Error in ProjectMapper - createProjectRequest()");
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return false;
    }

    public int getNextProjectId() {
        Connection con = null;
        try {
            con = DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {

        }

        PreparedStatement statement = null;
        ResultSet rs = null;
        String SQL = "select MAX(id) from projects";
        int id = 0;

        try {
            statement = con.prepareStatement(SQL);
            rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Error in ProjectMapper - getNextProjectId()");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return id + 1;
    }

    // Will make following function more generic; should be able to change status depending on parameter to reduce amount of methods needed
    public boolean changeProjectStatus(int project_id, String new_status, int companyId, int userId,  Connection con) {
        PreparedStatement statement = null;

        String SQL;
        if(companyId == 1) {
            SQL = "UPDATE projects SET status = ?, unread_partner = 1, notification = null where id = ?";
        } else {
            SQL = "UPDATE projects SET status = ?, unread_admin = 1, notification = null where id = ?";
        }



        try {
            statement = con.prepareStatement(SQL);
            statement.setString(1, new_status);
            statement.setInt(2, project_id);
            statement.executeUpdate();

            updateChangeDate(project_id, companyId);
            addStage(userId, project_id, new_status, DatabaseConnection.getInstance().getConnection());
            DatabaseFacade facade = new DatabaseFacade();
            if(companyId == 1)
                facade.markUnread(project_id, 2); // 2 is just not dell, a la partner
            else
                facade.markUnread(project_id, 1);

            return true;
        } catch (Exception e) {
            System.out.println("Zzzzz");
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return false;

    }

    public boolean addStage(int user_id, int project_id, String type, Connection con) {
        PreparedStatement statement = null;
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        Timestamp timestamp = new Timestamp(date.getTime());
        int id = getNextStageId();
        String SQL = "insert into stages values(?,?,?,?,?)";

        try {
            statement = con.prepareStatement(SQL);

            statement.setInt(1, id);
            statement.setInt(2, user_id);
            statement.setInt(3, project_id);
            statement.setTimestamp(4, timestamp);
            statement.setString(5, type);

            statement.executeUpdate();

            return true;
        } catch (Exception e) {
            System.out.println("Exception in addStage()");
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return false;

    }

    public ArrayList getStagesByProjectId(int project_id, Connection con) {
        String SQL = "select * from stages where project_id=?";
        ArrayList<Stage> stages = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = con.prepareStatement(SQL);
            statement.setInt(1, project_id);

            rs = statement.executeQuery();
            while(rs.next()) {
                stages.add(new Stage(rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getTimestamp(4),
                        rs.getString(5)));

            }

        } catch (Exception e) {
            System.out.println("Error in UserMapper");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }


        return stages;
    }

    public int getNextStageId() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {};
        String SQL = "select MAX(id) from stages";
        int id = 0;

        try {
            statement = con.prepareStatement(SQL);
            rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Error in ProjectMapper - getNextStageId()");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return id + 1;
    }

    public DisplayProject getProjectById(int id, int companyId, Connection con) {
        String SQL = "select * from projects where id=?";
        DisplayProject project = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = con.prepareStatement(SQL);
            statement.setInt(1, id);

            rs = statement.executeQuery();
            if (rs.next()) {
                project = new DisplayProject(rs.getInt(1),
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
                );
            } else {
                project.message = "A project with the id " + id + "doesn't exist.";
            }

        } catch (Exception e) {
            System.out.println("Error in UserMapper");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        if(project != null)
            if(companyId != 1)
                if(project.getCompany_id() != companyId) { //unauthorized access
                    project = new DisplayProject();
                    project.message = "You do not have permission to view this project";
                }
        return project;
    }

    public void changeReadStatus(int read, int id, int companyId, Connection con) {
        String SQL = "";
        System.out.println("Read: " + read + ", projid: " + id + ", compId: " + companyId);
        if(companyId == 1)
             SQL = "update projects set unread_admin=? where id=?";
        else
            SQL = "update projects set unread_partner=? where id=?";

        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQL);
            statement.setInt(1, read);
            statement.setInt(2, id);

            statement.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error in markRead");
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

    }

    public ArrayList getProjectsByState(String state, int companyId, Connection con) {
        ArrayList<DisplayProject> displayProjects = new ArrayList<>();

        String SQL;
        if(companyId == 1) { // Request from Dell user
            if(state.equals("waitingForAction"))
                SQL = "select * from projects where status='Waiting Project Verification' or status='Waiting Claim Verification' order by last_change_partner DESC, start_time DESC";
            else
                SQL = "select * from projects where status= ? order by last_change_partner DESC, start_time DESC";
        } else {
            if(state.equals("waitingForAction"))
                SQL = "select * from projects where (status='Project Verified' or status='Waiting Project Verification' or status='Waiting Claim Verification' or status='Project Approved') and company_id=? order by case when last_change_admin is null then 0 else 1 end DESC, last_change_admin DESC, start_time DESC";
            else
                SQL = "select * from projects where status= ? and company_id=? order by case when last_change_admin is null then 0 else 1 end DESC, last_change_admin DESC, start_time DESC";
        }


        PreparedStatement statement = null;
        ResultSet rs = null;

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

            rs = statement.executeQuery();
            while (rs.next()) {
                displayProjects.add(new DisplayProject(rs.getInt(1),
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
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }


        return displayProjects;
    }

    public int[] getStatusCounts(int companyId, Connection con) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String SQL = "";
        if(companyId == 1) {
            SQL = "select " +
                    "  sum(case when status='Waiting Project Verification' or status='Waiting Claim Verification' then 1 else 0 end) WaitingForAction," +
                    "  sum(case when status='Project Approved'  then 1 else 0 end) InExecution," +
                    "  sum(case when status='Project Finished' then 1 else 0 end) Finished" +
                    " from projects";
        } else {
            SQL = "select \n" +
                    "  sum(case when (status not in ('Project Finished', 'Cancelled')) and company_id=" + companyId + " then 1 else 0 end) WaitingForAction,\n" +
                    "  sum(case when status='' and company_id=" + companyId + "  then 1 else 0 end) InExecution,\n" +
                    "  sum(case when status='Project Finished' or status='Cancelled' and company_id=" + companyId + "   then 1 else 0 end) Finished\n" +
                    " from projects";
        }

        int[] res = new int[3];
        System.out.println(SQL);

        try {
            statement = con.prepareStatement(SQL);
            rs = statement.executeQuery();
            if (rs.next()) {
                res[0] = rs.getInt(1);
                res[1] = rs.getInt(2);
                res[2] = rs.getInt(3);
            }
            System.out.println(res[0]);
        } catch (Exception e) {
            System.out.println("Error in ProjectMapper - getStatusCounts()");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return res;
    }





    public void updateChangeDate(int project_id, int companyId) {

        Connection con = null;
        try {
            con = DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {

        }

        if (companyId == 1) {
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            Timestamp timestamp = new Timestamp(date.getTime());
            PreparedStatement statement = null;
            String SQL = "UPDATE projects SET last_change_admin = ? where id = ? ";
            try {
                statement = con.prepareStatement(SQL);
                statement.setTimestamp(1, timestamp);
                statement.setInt(2, project_id);
                statement.executeUpdate();

            } catch (Exception e) {
                System.out.println("Error in updateChangeDate()");
            } finally {
                if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
                if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
            }
        } else {
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            Timestamp timestamp = new Timestamp(date.getTime());
            PreparedStatement statement = null;
            String SQL = "UPDATE projects SET last_change_partner = ? where id = ? ";
            try {
                statement = con.prepareStatement(SQL);
                statement.setTimestamp(1, timestamp);
                statement.setInt(2, project_id);
                statement.executeUpdate();

            } catch (Exception e) {
                System.out.println("Error in updateChangeDate()");
            } finally {
                if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
                if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
            }

        }
        }

        public void updateNotification(int project_id, String notification) {
            Connection con = null;
            try {
                con = DatabaseConnection.getInstance().getConnection();
            } catch (Exception e) {

            }

            PreparedStatement statement = null;
            String SQL = "UPDATE projects SET notification = ? where id = ? ";
            try {
                statement = con.prepareStatement(SQL);
                statement.setString(1, notification);
                statement.setInt(2, project_id);
                statement.executeUpdate();

            } catch (Exception e) {
                System.out.println("Error in updateNotification()");
            } finally {
                if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
                if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
            }
        }


}
