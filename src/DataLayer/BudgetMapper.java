package DataLayer;

import Domain.Budget;

import java.sql.*;
import java.util.ArrayList;

public class BudgetMapper {

    public boolean addBudget(int year, int quarter, int budget, Connection con) {

        String SQL = "insert into budget values(?, ?, ?, ?, ?)";

        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(SQL);

            statement.setInt(1, budget);
            statement.setInt(2, year);
            statement.setInt(3, quarter);
            statement.setInt(4, 0);
            statement.setInt(5, 0);

            statement.executeUpdate();


        } catch(Exception e) {
            return false;
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return true;
    }

    public boolean modifyBudget(int new_budget, int year, int quarter, Connection con) {
        String SQL = "update budget set initial_budget=? where yearnum = ? and quarternum = ?";
        PreparedStatement statement = null;

        try {
        statement = con.prepareStatement(SQL);
            statement.setInt(1, new_budget);
            statement.setInt(2, year);
            statement.setInt(3, quarter);

            statement.executeUpdate();

        } catch(Exception e) {
            return false;
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return true;

    }

    public ArrayList<Budget> getActiveBudget(int year, int quarter, Connection con) {
        String SQL = "select * from budget where yearnum = ? and quarternum = ?";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<Budget> BudgetCollection = new ArrayList<>();


        try {
            statement = con.prepareStatement(SQL);

            statement.setInt(1, year);
            statement.setInt(2, quarter);

            rs = statement.executeQuery();


            while (rs.next()) {
                BudgetCollection.add(new Budget(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getInt(5),
                        rs.getInt(4)
                ));
            }



        } catch (Exception e) {
            return null;
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }


        return BudgetCollection;
    }

    public ArrayList<Budget> getAllBudgets(Connection con) {
        String SQL = "select * from budget";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<Budget> BudgetCollection = new ArrayList<>();

        try {
            statement = con.prepareStatement(SQL);

            rs = statement.executeQuery();

            while (rs.next()) {
                BudgetCollection.add(new Budget(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5)
                ));
            }

        } catch (Exception e) {
            System.out.println("error in BudgetMapper");
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }


        return BudgetCollection;
    }

    public int getAvailableFunds(int year, int quarter, Connection con) {
        int availablefunds = 0;
        PreparedStatement statement = null;
        String SQL = "select initial_budget, reserved from budget where yearnum=? and quarternum = ?";

        ResultSet rs = null;
        try {
            statement = con.prepareStatement(SQL);
            statement.setInt(1, year);
            statement.setInt(2, quarter);

            rs = statement.executeQuery();

            while (rs.next()) {
                availablefunds = rs.getInt(1) - rs.getInt(2);
            }

        } catch (Exception e) {
            System.out.println("Error in availableFunds");
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return availablefunds;

    }





}
