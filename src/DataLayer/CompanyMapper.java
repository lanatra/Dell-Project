package DataLayer;

import Domain.Company;

import java.sql.*;
import java.util.ArrayList;

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
                        rs.getString(3),
                        rs.getString(4));
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

    public int createCompany(String company_name, String country_code, Connection con) {
        String SQL = "insert into companies values (?,?,?,?)";

        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQL);

            int nextCompanyId = getNextCompanyId(con);

            statement.setInt(1, nextCompanyId);
            statement.setString(2, company_name);
            statement.setString(3, "N/A");
            statement.setString(4, country_code);

            statement.executeUpdate();

            return nextCompanyId;

        } catch (Exception e) {
            System.out.println("Error in createCompany");
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return -1;
    }

    public void updateCompanyLogo(String filename, int id, Connection con) {
        String SQL = "update companies set IMAGE_FILENAME=? where id=?";

        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQL);
            statement.setString(1, filename);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error in updateCompanyLogo");
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
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

    public ArrayList getCompanies(Connection con) {
        ArrayList<Company> companies = new ArrayList<>();
        String SQL = "select * from companies";

        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = con.prepareStatement(SQL);
            rs = statement.executeQuery();
            while(rs.next()) {
                companies.add(new Company(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)));
            }

        } catch (Exception e) {
            System.out.println("Error in CompanyMapper");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return companies;
    }
    public ArrayList getCompanyNames(String query, int company_id, Connection con) {
        ArrayList<String> results = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet rs = null;
        String SQL = "select DISTINCT name\n" +
                "from companies\n" +
                "where lower(name) like lower('%" + query + "%')\n";

        System.out.println(SQL);

        try {
            statement = con.prepareStatement(SQL);
            rs = statement.executeQuery();
            while (rs.next()) {
                results.add(rs.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Error in ProjectMapper - getCompanyNames()");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return results;
    }

    public int getCompanyIdByName(String name) {
        Connection con = null;
        try {
            con = DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) { }

        System.out.println("name getcompanyidbyname: " + name);

        PreparedStatement statement = null;
        ResultSet rs = null;
        String SQL = "select id " +
                "from companies " +
                "where name=?";

        int id = -1;

        try {
            statement = con.prepareStatement(SQL);
            statement.setString(1, name);
            rs = statement.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error in CompanyMapper - getCompanyIdByName()");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return id;
    }

}
