package DataLayer;

import Domain.Project;

import java.sql.*;


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
            statement.setTimestamp(11, null);
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




    public boolean verifyProjectRequest(int project_id, Connection con) {
        PreparedStatement statement = null;
        String SQL = "UPDATE projects SET status = 'Verified' where id = ?";

        try {
            statement.setInt(1, project_id);

            statement.executeQuery();

            return true;
        } catch (Exception e) {
            System.out.println("Error in verifyProjectRequest");
        }
        return false;


    }



}
