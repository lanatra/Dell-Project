package DataLayer;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Anden702 on 03-05-2015.
 */
public class StatisticsGetter {
    public static ArrayList<String[]> getDistinctTypesCounts(Timestamp start, Timestamp end, Connection con) {

        String SQL = "select distinct(type), count(type) as count" +
                " from projects" +
                " where START_TIME between ? and ?" +
                " GROUP BY type";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<String[]> res = new ArrayList<>();


        try {
            statement = con.prepareStatement(SQL);
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            rs = statement.executeQuery();
            while(rs.next()) {
                res.add(new String[]{rs.getString(1), String.valueOf(rs.getInt(2))});
            }
        } catch (Exception e) {
            System.out.println("error in StatisticsGetter#getDistinctTypesCounts");
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return res;
    }
    public static ArrayList<String[]> getAvgCostPerType(Timestamp start, Timestamp end, Connection con) {

        String SQL = "select distinct(type), avg(budget)" +
                " from projects" +
                " where START_TIME between ? and ?" +
                " GROUP BY type";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<String[]> res = new ArrayList<>();


        try {
            statement = con.prepareStatement(SQL);
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            rs = statement.executeQuery();
            while(rs.next()) {
                res.add(new String[]{rs.getString(1), String.valueOf(rs.getInt(2))});
            }
        } catch (Exception e) {
            System.out.println("error in StatisticsGetter#getAvgCostPerType");
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return res;
    }
    public static ArrayList<String[]> getCostPerType(Timestamp start, Timestamp end, Connection con) {

        String SQL = "select distinct(type), sum(budget)" +
                " from projects" +
                " where START_TIME between ? and ?" +
                " GROUP BY type";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<String[]> res = new ArrayList<>();


        try {
            statement = con.prepareStatement(SQL);
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            rs = statement.executeQuery();
            while(rs.next()) {
                res.add(new String[]{rs.getString(1), String.valueOf(rs.getInt(2))});
            }
        } catch (Exception e) {
            System.out.println("error in StatisticsGetter#getCostPerType");
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return res;
    }
    public static ArrayList<String[]> getBudgetProgression(Timestamp start, Timestamp end, Connection con) {

        String SQL = "select stages.time, projects.budget from projects" +
                " LEFT JOIN STAGES" +
                " on PROJECTS.ID = STAGES.PROJECT_ID" +
                " where stages.type = 'Project Approved'" +
                " and START_TIME between ? and ?" +
                " order by stages.time";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<String[]> res = new ArrayList<>();


        try {
            statement = con.prepareStatement(SQL);
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            rs = statement.executeQuery();
            while(rs.next()) {
                res.add(new String[]{String.valueOf(rs.getTimestamp(1).getTime()), String.valueOf(rs.getInt(2))});
            }
        } catch (Exception e) {
            System.out.println("error in StatisticsGetter#getBudgetProgression");
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return res;
    }
    public static ArrayList<String[]> getCountOfProjectsByCountry(Timestamp start, Timestamp end, Connection con) {

        String SQL = "select distinct(companies.COUNTRY_CODE), count(PROJECTS.id)" +
                " from companies" +
                " LEFT JOIN PROJECTS" +
                " ON COMPANIES.ID = PROJECTS.COMPANY_ID" +
                " where START_TIME between ? and ?" +
                " GROUP BY COMPANIES.COUNTRY_CODE";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<String[]> res = new ArrayList<>();


        try {
            statement = con.prepareStatement(SQL);
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            rs = statement.executeQuery();
            while(rs.next()) {
                res.add(new String[]{rs.getString(1), String.valueOf(rs.getInt(2))});
            }
        } catch (Exception e) {
            System.out.println("error in StatisticsGetter#getCountOfProjectsByCountry");
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return res;
    }
    public static ArrayList<String[]> getAvgCostOfProjectsByCountry(Timestamp start, Timestamp end, Connection con) {

        String SQL = "select distinct(companies.COUNTRY_CODE), avg(PROJECTS.budget) from companies" +
                " LEFT JOIN PROJECTS" +
                "  ON COMPANIES.ID = PROJECTS.COMPANY_ID" +
                " where START_TIME between ? and ?" +
                " GROUP BY COMPANIES.COUNTRY_CODE";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<String[]> res = new ArrayList<>();


        try {
            statement = con.prepareStatement(SQL);
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            rs = statement.executeQuery();
            while(rs.next()) {
                res.add(new String[]{rs.getString(1), String.valueOf(rs.getInt(2))});
            }
        } catch (Exception e) {
            System.out.println("error in StatisticsGetter#getAvgCostOfProjectsByCountry");
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return res;
    }
    public static ArrayList<String[]> getCompaniesByLargestApprovedBudget(Timestamp start, Timestamp end, Connection con) {

        String SQL = "select C.name, pbudget" +
                "  from (" +
                "         select distinct(c.id) cid, sum(p.budget) pbudget from companies c" +
                "           LEFT JOIN PROJECTS p" +
                "             ON c.ID = p.COMPANY_ID" +
                "           where p.budget is not null and p.status not in ('Waiting Project Verification', 'Project Rejected') and p.START_TIME between ? and ?" +
                "         GROUP BY c.id) lists" +
                " INNER JOIN companies C on lists.cid = C.id" +
                " ORDER BY pbudget DESC";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<String[]> res = new ArrayList<>();


        try {
            statement = con.prepareStatement(SQL);
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            rs = statement.executeQuery();
            while(rs.next()) {
                res.add(new String[]{rs.getString(1), String.valueOf(rs.getInt(2))});
            }
        } catch (Exception e) {
            System.out.println("error in StatisticsGetter#getCompaniesByLargestApprovedBudget");
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return res;
    }
    public static ArrayList<String[]> getTypesWithHighestSuccessRate(Timestamp start, Timestamp end, Connection con) {

        String SQL = "select distinct(projects.type), cast(count(c.TYPE) as FLOAT)/cast(count(PROJECTS.type) as float) as Percent from projects" +
                " left join (select * from projects where status in ('Project Finished','Reimbursed')) C" +
                "  ON PROJECTS.id = c.id" +
                " where projects.START_TIME between ? and ?" +
                " group by projects.type" +
                " order by Percent DESC";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<String[]> res = new ArrayList<>();


        try {
            statement = con.prepareStatement(SQL);
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            rs = statement.executeQuery();
            while(rs.next()) {
                res.add(new String[]{rs.getString(1), String.valueOf(rs.getDouble(2))});
            }
        } catch (Exception e) {
            System.out.println("error in StatisticsGetter#getTypesWithHighestSuccessRate");
            System.out.println(e.getMessage());
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return res;
    }
public static ArrayList<String[]> getCountOfMessages(Timestamp start, Timestamp end, Connection con) {

        String SQL = "select count(id)" +
                " from MESSAGES" +
                " where CREATION_TIME between ? and ?";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<String[]> res = new ArrayList<>();


        try {
            statement = con.prepareStatement(SQL);
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            rs = statement.executeQuery();
            while(rs.next()) {
                res.add(new String[]{String.valueOf(rs.getInt(1))});
            }
        } catch (Exception e) {
            System.out.println("error in StatisticsGetter#getCountOfMessages");
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return res;
    }
public static ArrayList<String[]> getAvgMessagesPerProject(Timestamp start, Timestamp end, Connection con) {

        String SQL = "Select tot2/tot1" +
                " from" +
                "  (Select Count(id) as tot1 FROM projects where START_TIME between ? and ?) h," +
                " (Select Count(id) as tot2 FROM MESSAGES where CREATION_TIME between ? and ?) s";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<String[]> res = new ArrayList<>();


        try {
            statement = con.prepareStatement(SQL);
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            statement.setTimestamp(3, start);
            statement.setTimestamp(4, end);
            rs = statement.executeQuery();
            while(rs.next()) {
                res.add(new String[]{String.valueOf(rs.getInt(1))});
            }
        } catch (Exception e) {
            System.out.println("error in StatisticsGetter#getAvgMessagesPerProject");
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return res;
    }
public static ArrayList<String[]> getCountOfFinishedProjects(Timestamp start, Timestamp end, Connection con) {

        String SQL = "select count(id)" +
                " from projects" +
                " where STATUS='Project Finished'" +
                " and START_TIME between ? and ?";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<String[]> res = new ArrayList<>();


        try {
            statement = con.prepareStatement(SQL);
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            rs = statement.executeQuery();
            while(rs.next()) {
                res.add(new String[]{String.valueOf(rs.getInt(1))});
            }
        } catch (Exception e) {
            System.out.println("error in StatisticsGetter#getCountOfFinishedProjects");
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return res;
    }
public static ArrayList<String[]> getMoneyReimbursed(Timestamp start, Timestamp end, Connection con) {

        String SQL = "select sum(REIMBURSED) from BUDGET";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<String[]> res = new ArrayList<>();


        try {
            statement = con.prepareStatement(SQL);
            rs = statement.executeQuery();
            while(rs.next()) {
                res.add(new String[]{String.valueOf(rs.getInt(1))});
            }
        } catch (Exception e) {
            System.out.println("error in StatisticsGetter#getMoneyReimbursed");
            System.out.println(e.getMessage());
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return res;
    }





}
