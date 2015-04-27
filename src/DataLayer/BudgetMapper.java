package DataLayer;

import Domain.Budget;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Lasse on 27-04-2015.
 */
public class BudgetMapper {

    public boolean addBudget(int year, int quarter, int budget, Connection con) {

        String SQL = "insert into budget values(?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(SQL);

            statement.setInt(1, getNextBudgetId());
            statement.setInt(2, budget);
            statement.setInt(3, year);
            statement.setInt(4, quarter);
            statement.setInt(5, 0);
            statement.setInt(6, 0);

            statement.executeUpdate();


        } catch(Exception e) {
            return false;
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return true;
    }

    public int getNextBudgetId() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {};
        String SQL = "select MAX(id) from budget";
        int id = 0;

        try {
            statement = con.prepareStatement(SQL);
            rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Error in BudgetMapper - getNextBudgetId()");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return id + 1;
    }

    public boolean modifyBudget(int budget_id, int new_budget, Connection con) {
        String SQL = "update budget set initial_budget=? where id=?";
        PreparedStatement statement = null;

        try {
        statement = con.prepareStatement(SQL);
            statement.setInt(1, new_budget);
            statement.setInt(2, budget_id);

            statement.executeUpdate();

        } catch(Exception e) {
            return false;
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return true;

    }

    public ArrayList<Budget> getBudget(int budget_id, Connection con) {
        String SQL = "select * from budget where budget_id = ?";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<Budget> BudgetCollection = new ArrayList<>();


        try {
            statement = con.prepareStatement(SQL);

            statement.setInt(1, budget_id);

            rs = statement.executeQuery();


            while (rs.next()) {
                BudgetCollection.add(new Budget(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getInt(6)
                ));
            }



        } catch (Exception e) {
            System.out.println("error in budgetmapperrerr");
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
                        rs.getInt(5),
                        rs.getInt(6)
                ));
            }



        } catch (Exception e) {
            System.out.println("error in budgetmapperrerr");
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }


        return BudgetCollection;
    }

    public int getAvailableFunds(int budget_id, Connection con) {
        int availablefunds = 0;
        PreparedStatement statement = null;
        String SQL = "select initial_budget, reserved from budget where id=?";

        ResultSet rs = null;
        try {
            statement = con.prepareStatement(SQL);
            statement.setInt(1, budget_id);

            rs = statement.executeQuery();

            while (rs.next()) {
                availablefunds = rs.getInt(1) - rs.getInt(2);
            }

        } catch (Exception e) {
            System.out.println("Error in availableFunds");
        }

        return availablefunds;

    }





}
