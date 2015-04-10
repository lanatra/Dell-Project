package DataLayer;

import Domain.Project;

import java.sql.*;
import java.util.ArrayList;


/**
 * Created by Lasse on 09-04-2015.
 */
public class ProjectMapper {

    public boolean createProjectRequest(String budget, String project_body, Connection con) {

        String SQL = "insert into projects values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        double parsedBudget;
        try {
            parsedBudget = Double.parseDouble(budget);
        } catch (NumberFormatException e) {
            return false;
        }
        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(SQL);
            int nextProjectID = getNextProjectId(con);

            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            Timestamp timestamp = new Timestamp(date.getTime());

            statement.setInt(1, nextProjectID);
            statement.setTimestamp(2, timestamp);
            statement.setTimestamp(3, null);
            statement.setInt(4, 1); // company id, gotta find a solution here, putting 1 temporarily
            statement.setInt(5, 1); // owner_id, same as above
            statement.setString(6, "Pending");
            statement.setDouble(7, parsedBudget);
            statement.setString(8, project_body);
            statement.setTimestamp(9, null);
            statement.setTimestamp(10, null);
            statement.setTimestamp(11, timestamp);
            statement.setBoolean(12, true);
            statement.setBoolean(13, false);

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


    public boolean verifyProjectRequest(String project_id, Connection con) {
        PreparedStatement statement = null;
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        Timestamp timestamp = new Timestamp(date.getTime());
        String SQL = "UPDATE projects SET status = 'Verified' where id = ?";

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

            updateChangeDate(parsedId, "Dell", con);

            return true;
        } catch (Exception e) {
            System.out.println("Zzzzz");
        }
        return false;

    }

    public ArrayList getProjectsByState(String state, Connection con) {
        ArrayList<Project> projects = new ArrayList<>();
        String SQL = "select * from projects where status= ? ";

        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(SQL);
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
                        rs.getBoolean(13)));
            }

        } catch (Exception e) {
            System.out.println("Error in UserMapper");
        }

        return projects;
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
        }
        else {
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

}
