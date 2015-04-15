package DataLayer;

import Domain.Company;

import java.sql.*;

/**
 * Created by Lasse on 10-04-2015.
 */
public class CompanyMapper {

    public Company getCompanyById(int id, Connection con) {
        Company company = null;
        String SQL = "select * from companies where id= ? ";

        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = con.prepareStatement(SQL);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.next()) {
                company = new Company(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3));
            }

        } catch (Exception e) {
            System.out.println("Error in CompanyMapper");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return company;
    }

    public boolean createCompany(String company_name, Connection con) {
        String SQL = "insert into companies values (?,?,?)";

        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQL);

            int nextCompanyId = getNextCompanyId(con);

            statement.setInt(1, nextCompanyId);
            statement.setString(2, company_name);
            statement.setString(3, "N/A");

            statement.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("Error in createCompany");
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return false;
    }

     public int getNextCompanyId(Connection con) {
         PreparedStatement statement = null;
         ResultSet rs = null;

         String SQL = "select MAX(id) from companies";
         int id = 0;

        try {
            statement = con.prepareStatement(SQL);
            rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Error in CompanyMapper - getNextCompanyId()");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return id + 1;
    }




}
