package DataLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Anden702 on 03-05-2015.
 */
public class StatisticsGetter {
    public static ArrayList<String[]> getDistinctTypesCounts(Connection con) {

        String SQL = "select distinct(type), count(type) as count from projects GROUP BY type";
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<String[]> res = new ArrayList<>();


        try {
            statement = con.prepareStatement(SQL);
            rs = statement.executeQuery();
            while(rs.next()) {
                res.add(new String[]{rs.getString(1), String.valueOf(rs.getInt(2))});
            }
        } catch (Exception e) {
            System.out.println("error in fisk");
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return res;
    }

}
