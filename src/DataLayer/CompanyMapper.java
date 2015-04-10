package DataLayer;

import Domain.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Lasse on 10-04-2015.
 */
public class CompanyMapper {

    public Company getCompanyById(int id, Connection con) {
        Company company = null;
        String SQL = "select * from companies where id= ? ";

        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(SQL);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                company = new Company(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3));
            }

        } catch (Exception e) {
            System.out.println("Error in CompanyMapper");
        }

        return company;
    }


}
