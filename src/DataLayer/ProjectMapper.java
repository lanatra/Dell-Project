package DataLayer;

import Domain.DisplayProject;
import Domain.Project;
import Domain.Stage;
import Domain.User;
import Domain.Result;

import javax.xml.crypto.Data;
import java.security.interfaces.RSAKey;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;


public class ProjectMapper {


    public int createProjectRequest(int budget, String project_body, User user, String project_type, Timestamp execution_date, Connection con) {

        String SQL = "insert into projects values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

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
            statement.setInt(7, budget);
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

            return nextProjectID;

        } catch (SQLException t) {
            System.out.println("SQLException in createProjectRequest()");
        }
        catch (Exception e) {
            System.out.println("Error in ProjectMapper - createProjectRequest()");
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return 0;
    }
public void resubmitProject(int budget, String project_body, int project_id, String project_type, Timestamp execution_date, int user_id, Connection con) {

        String SQL = "update projects" +
                " set budget=?,body=?,type=?,EXECUTION_DATE=?,status='Waiting Project Verification'" +
                " where id=?";

        PreparedStatement statement = null;

        try {
            int nextProjectID = getNextProjectId();
            statement = con.prepareStatement(SQL);
            statement.setInt(1, budget);
            statement.setString(2, project_body);
            statement.setString(3, project_type);
            statement.setTimestamp(4, execution_date);
            statement.setInt(5, project_id);

            statement.executeUpdate();

            addStage(user_id, project_id, "Waiting Project Verification", DatabaseConnection.getInstance().getConnection());

        } catch (SQLException t) {
            System.out.println("SQLException in resubmitProject()");
        }
        catch (Exception e) {
            System.out.println("Error in ProjectMapper - resubmitProject()");
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
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
        if (!new_status.equals("Project Approved") && !new_status.equals("Project Finished") && !new_status.equals("Cancelled")) {
            try {
                statement = con.prepareStatement(SQL);
                statement.setString(1, new_status);
                statement.setInt(2, project_id);
                statement.executeUpdate();

                updateChangeDate(project_id, companyId);
                addStage(userId, project_id, new_status, DatabaseConnection.getInstance().getConnection());
                DatabaseFacade facade = new DatabaseFacade();
                if (companyId == 1)
                    facade.markUnread(project_id, 2); // 2 is just not dell, a la partner
                else
                    facade.markUnread(project_id, 1);

                return true;
            } catch (Exception e) {
                System.out.println("Zzzzz");
            } finally {
                if (statement != null) try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (con != null) try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        // IF PROJECT BECOMES APPROVED WE NEED A TRANSACTION TO PROCESS CHANGES IN BUDGET SIMULTANEOUSLY
        else if (new_status.equals("Project Approved")){
            PreparedStatement budgetStatement = null;

            int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int currentQuarter = (currentMonth / 3 ) + 1;

            int project_budget = projectBudgetById(project_id);

            String SQLBudget = "update budget set reserved = reserved + ? where yearnum = ? and quarternum = ?";

            try {
                // TRANSACTION BEGIN
                con.setAutoCommit(false);
                // PROJECT CHANGES
                statement = con.prepareStatement(SQL);
                statement.setString(1, new_status);
                statement.setInt(2, project_id);
                statement.executeUpdate();
                // BUDGET CHANGES
                budgetStatement = con.prepareStatement(SQLBudget);
                budgetStatement.setInt(1, project_budget);
                budgetStatement.setInt(2, currentYear);
                budgetStatement.setInt(3, currentQuarter);
                budgetStatement.executeUpdate();
                // COMMIT
                con.commit();
                // TRANSACTION OVER
                updateChangeDate(project_id, companyId);
                addStage(userId, project_id, new_status, DatabaseConnection.getInstance().getConnection());
                DatabaseFacade facade = new DatabaseFacade();
                if (companyId == 1)
                    facade.markUnread(project_id, 2); // 2 is just not dell, a la partner
                else
                    facade.markUnread(project_id, 1);

                return true;
            } catch (Exception e) {
                try {
                    con.rollback();
                } catch(Exception y) {
                }
                return false;

            } finally {
                if (statement != null) try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (con != null) try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        // IF PROJECT IS FINISHED (POE APPROVED) WE NEED A TRANSACTION TO PROCESS CHANGES IN BUDGET SIMULTANEOUSLY
        else if (new_status.equals("Project Finished")){
            PreparedStatement budgetStatement1 = null;
            PreparedStatement budgetStatement2 = null;

            long timestamp = getProjectFinishedByProjectId(project_id).getTime();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timestamp);

            int currentMonth = cal.get(Calendar.MONTH);
            int currentYear = cal.get(Calendar.YEAR);
            int currentQuarter = (currentMonth / 3 ) + 1;

            int project_budget = projectBudgetById(project_id);

            String SQLBudget1 = "update budget set reserved = reserved - ? where yearnum = ? and quarternum = ?";
            String SQLBudget2 = "update budget set reimbursed = reimbursed + ? where yearnum = ? and quarternum = ?";
            try {
                // TRANSACTION BEGIN
                con.setAutoCommit(false);
                // PROJECT CHANGES
                statement = con.prepareStatement(SQL);
                statement.setString(1, new_status);
                statement.setInt(2, project_id);
                statement.executeUpdate();
                // RESERVED CHANGES
                budgetStatement1 = con.prepareStatement(SQLBudget1);
                budgetStatement1.setInt(1, project_budget);
                budgetStatement1.setInt(2, currentYear);
                budgetStatement1.setInt(3, currentQuarter);
                budgetStatement1.executeUpdate();
                // REIMBURSED CHANGES
                budgetStatement2 = con.prepareStatement(SQLBudget2);
                budgetStatement2.setInt(1, project_budget);
                budgetStatement2.setInt(2, currentYear);
                budgetStatement2.setInt(3, currentQuarter);
                budgetStatement2.executeUpdate();
                // COMMIT
                con.commit();
                // TRANSACTION OVER
                updateChangeDate(project_id, companyId);
                addStage(userId, project_id, new_status, DatabaseConnection.getInstance().getConnection());
                DatabaseFacade facade = new DatabaseFacade();
                if (companyId == 1)
                    facade.markUnread(project_id, 2); // 2 is just not dell, a la partner
                else
                    facade.markUnread(project_id, 1);


                return true;
            } catch (Exception e) {
                try {
                    con.rollback();
                } catch(Exception y) {
                }
                return false;

            } finally {
                if (statement != null) try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (con != null) try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        else if (new_status.equals("Cancelled")){

            String current_status = projectStatusById(project_id);

            if (current_status.equals("Waiting Project Verification") || current_status.equals("Project Rejected") || current_status.equals("Project Finished")) {
                try {
                    statement = con.prepareStatement(SQL);
                    statement.setString(1, new_status);
                    statement.setInt(2, project_id);
                    statement.executeUpdate();

                    updateChangeDate(project_id, companyId);
                    addStage(userId, project_id, new_status, DatabaseConnection.getInstance().getConnection());
                    DatabaseFacade facade = new DatabaseFacade();
                    if (companyId == 1)
                        facade.markUnread(project_id, 2); // 2 is just not dell, a la partner
                    else
                        facade.markUnread(project_id, 1);

                    return true;
                } catch (Exception e) {
                    System.out.println("Zzzzz");
                } finally {
                    if (statement != null) try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (con != null) try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else {

                PreparedStatement budgetStatement = null;
                String SQLBudget = "update budget set reserved = reserved - ? where yearnum = ? and quarternum = ?";

                long timestamp = getProjectFinishedByProjectId(project_id).getTime();
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(timestamp);

                int currentMonth = cal.get(Calendar.MONTH);
                int currentYear = cal.get(Calendar.YEAR);
                int currentQuarter = (currentMonth / 3) + 1;

                int project_budget = projectBudgetById(project_id);

                try {
                    // TRANSACTION BEGIN
                    con.setAutoCommit(false);
                    // PROJECT CHANGES
                    statement = con.prepareStatement(SQL);
                    statement.setString(1, new_status);
                    statement.setInt(2, project_id);
                    statement.executeUpdate();
                    // RESERVED CHANGES
                    budgetStatement = con.prepareStatement(SQLBudget);
                    budgetStatement.setInt(1, project_budget);
                    budgetStatement.setInt(2, currentYear);
                    budgetStatement.setInt(3, currentQuarter);
                    budgetStatement.executeUpdate();
                    // COMMIT
                    con.commit();
                    // TRANSACTION OVER
                    updateChangeDate(project_id, companyId);
                    addStage(userId, project_id, new_status, DatabaseConnection.getInstance().getConnection());
                    DatabaseFacade facade = new DatabaseFacade();
                    if (companyId == 1)
                        facade.markUnread(project_id, 2); // 2 is just not dell, a la partner
                    else
                        facade.markUnread(project_id, 1);


                    return true;
                } catch (Exception e) {
                    try {
                        con.rollback();
                    } catch (Exception y) {
                    }
                    return false;

                } finally {
                    if (statement != null) try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (con != null) try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

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
            else if(state.equals("inExecution"))
                SQL = "select * from projects where status='Project Approved' or status='Project Rejected' or status='Claim Rejected' order by last_change_partner DESC, start_time DESC";
            else if(state.equals("finished"))
                SQL = "select * from projects where status='Project Finished' or status='Cancelled' order by last_change_partner DESC, start_time DESC";
            else
                SQL = "select * from projects where status='" + state + "' order by last_change_partner DESC, start_time DESC";
        } else {
            if(state.equals("waitingForAction"))
                SQL = "select * from projects where status not in ('Project Finished', 'Cancelled') and company_id=? order by case when last_change_admin is null then 0 else 1 end DESC, last_change_admin DESC, start_time DESC";
            else if(state.equals("finished"))
                SQL = "select * from projects where status in ('Project Finished', 'Cancelled') and company_id=? order by case when last_change_admin is null then 0 else 1 end DESC, last_change_admin DESC, start_time DESC";
            else
                SQL = "select * from projects where status='" + state + "' and company_id=? order by case when last_change_admin is null then 0 else 1 end DESC, last_change_admin DESC, start_time DESC";
        }


        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = con.prepareStatement(SQL);
            if(companyId != 1)
                statement.setInt(1, companyId);

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

    public ArrayList getProjectsByType(String type, int companyId, Connection con) {
        ArrayList<DisplayProject> displayProjects = new ArrayList<>();

        String SQL;
        if(companyId == 1)
            SQL = "select * from projects where type=? order by last_change_partner DESC, start_time DESC";
        else
            SQL = "select * from projects where type=? and company_id=? order by case when last_change_admin is null then 0 else 1 end DESC, last_change_admin DESC, start_time DESC";



        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = con.prepareStatement(SQL);
            statement.setString(1, type);
            if(companyId != 1)
                statement.setInt(2, companyId);

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

    public ArrayList getProjectsByCompanyName(String companyName, int companyId, Connection con) {
        ArrayList<DisplayProject> displayProjects = new ArrayList<>();

        DatabaseFacade facade = new DatabaseFacade();
        int id = facade.getCompanyIdByName(companyName);

        String SQL = "select * from projects where company_id=? order by last_change_partner DESC, start_time DESC";


        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = con.prepareStatement(SQL);
            statement.setInt(1, id);

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
                    "  sum(case when status='Project Approved' or status='Project Rejected' or status='Claim Rejected'  then 1 else 0 end) InExecution," +
                    "  sum(case when status='Project Finished' then 1 else 0 end) Finished" +
                    " from projects";
        } else {
            SQL = "select " +
                    "  sum(case when status not in ('Project Finished', 'Cancelled') and company_id=? then 1 else 0 end) WaitingForAction," +
                    "  0 InExecution," +
                    "  sum(case when status in ('Project Finished', 'Cancelled') and company_id=? then 1 else 0 end) Finished" +
                    " from projects";
        }

        int[] res = new int[3];
        System.out.println(SQL);

        try {
            statement = con.prepareStatement(SQL);
            if(companyId != 1) {
                statement.setInt(1, companyId);
                statement.setInt(2, companyId);
            }
            rs = statement.executeQuery();
            if (rs.next()) {
                res[0] = rs.getInt(1);
                res[1] = rs.getInt(2);
                res[2] = rs.getInt(3);
            }
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

    public ArrayList getDistinctStatuses(String query, Connection con) {
        ArrayList<String> results = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet rs = null;
        String SQL = "select DISTINCT status" +
                " from projects" +
                " where lower(status) like lower('%" + query + "%')";

        System.out.println(SQL);

        try {
            statement = con.prepareStatement(SQL);
            rs = statement.executeQuery();
            while (rs.next()) {
                results.add(rs.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Error in ProjectMapper - getDistinctStatuses()");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return results;
    }

    public ArrayList getDistinctTypes(String query, int companyId, Connection con) {
        ArrayList<String> results = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet rs = null;
        String SQL;
        if(companyId != 1)
            SQL = "select DISTINCT type" +
                " from projects" +
                " where company_id=? and lower(type) like lower('%" + query + "%')";
        else
            SQL = "select DISTINCT type" +
                    " from projects" +
                    " where lower(type) like lower('%" + query + "%')";

        System.out.println(SQL);

        try {
            statement = con.prepareStatement(SQL);
            if(companyId != 1)
                statement.setInt(1, companyId);
            rs = statement.executeQuery();
            while (rs.next()) {
                results.add(rs.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Error in ProjectMapper - getDistinctStatuses()");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return results;
    }

    public ArrayList search(String query, int companyId, Connection con) {
        ArrayList<Result> results = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet rs = null;
        String SQL;

        if(companyId == 1)
            SQL = "    select 'Project' OriginatingTable, id, body, utl_match.jaro_winkler_similarity(body, '" + query + "') Ayy, 1" +
                "    from projects" +
                "    where lower(body) like lower('%" + query + "%')" +
                "    union all" +
                "    select 'Message', project_id, body, utl_match.jaro_winkler_similarity(body, '" + query + "') Ayy, 2" +
                "    from messages" +
                "    where lower(body) like lower('%" + query + "%')" +
                "    union all" +
                "    select 'User', id, name, utl_match.jaro_winkler_similarity(name, '" + query + "') Ayy, 3" +
                "    from users" +
                "    where lower(name) like lower('%" + query + "%')" +
                "    ORDER by 5 ASC, Ayy DESC";
        else
            SQL = "    select 'Project' OriginatingTable, id, body, utl_match.jaro_winkler_similarity(body, '" + query + "') Ayy, 1" +
                    "    from projects" +
                    "    where lower(body) like lower('%" + query + "%') and company_id=" + companyId +"" +
                    "    union all" +
                    "    select 'Message', project_id, body, utl_match.jaro_winkler_similarity(body, '" + query + "') Ayy, 2" +
                    "    from messages" +
                    "    where lower(body) like lower('%" + query + "%') and author_id IN (SELECT id from users where company_id=" + companyId + ") " +
                    "    union all" +
                    "    select 'User', id, name,  utl_match.jaro_winkler_similarity(name, '" + query + "') Ayy, 3" +
                    "    from users" +
                    "    where lower(name) like lower('%" + query + "%') and company_id=" + companyId +
                    "    ORDER by 5 ASC, Ayy DESC";


        try {
            statement = con.prepareStatement(SQL);
            rs = statement.executeQuery();
            while (rs.next()) {
                results.add(new Result(
                        rs.getString(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getInt(4)
                ));
            }
        } catch (Exception e) {
            System.out.println("Error in ProjectMapper - search()");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return results;
    }


    public ArrayList<Project> getProjectsByCompanyId(int company_id, Connection con) {
        String SQL = "select * from projects where company_id = ?";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<Project> ProjectCollection = new ArrayList<>();


        try {
            statement = con.prepareStatement(SQL);

            statement.setInt(1, company_id);

            rs = statement.executeQuery();

            while (rs.next()) {
                ProjectCollection.add(new Project(
                                rs.getInt(1),
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
            System.out.println("error in projectmapperrerr get by company id method" );
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }


        return ProjectCollection;
    }

    public ArrayList<Project> getProjectsByUserId(int user_id, Connection con) {
        String SQL = "select * from projects where user_id = ?";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<Project> ProjectCollection = new ArrayList<>();

        try {
            statement = con.prepareStatement(SQL);
            statement.setInt(1, user_id);

            rs = statement.executeQuery();

            while (rs.next()) {
                ProjectCollection.add(new Project(
                        rs.getInt(1),
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
            System.out.println("error in ProjectMapper get by user id method" );
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return ProjectCollection;
    }

    public int projectBudgetById(int project_id) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String SQL = "select budget from projects where id= ? ";
        Connection con = null;
        try {
            con = DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {

        }

        try {
            statement = con.prepareStatement(SQL);

            statement.setInt(1, project_id);

            rs = statement.executeQuery();

            while (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("error irgwgew" );
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return -1;
    }

    public Timestamp getProjectFinishedByProjectId(int project_id) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String SQL = "select stages.time from stages where project_id= ? ";
        Connection con = null;
        try {
            con = DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {

        }

        try {
            statement = con.prepareStatement(SQL);

            statement.setInt(1, project_id);

            rs = statement.executeQuery();

            while (rs.next()) {
                return rs.getTimestamp(1);
            }

        } catch (Exception e) {
            System.out.println("error in timestampgetterererer" );
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return null;
    }

    public String projectStatusById(int project_id) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String SQL = "select status from projects where id= ? ";
        Connection con = null;
        try {
            con = DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {

        }

        try {
            statement = con.prepareStatement(SQL);

            statement.setInt(1, project_id);

            rs = statement.executeQuery();

            while (rs.next()) {
                return rs.getString(1);
            }

        } catch (Exception e) {
            System.out.println("error stringGetetERERERERERR status project blebeleb" );
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return null;
    }


}
