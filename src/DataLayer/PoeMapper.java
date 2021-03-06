package DataLayer;

import Domain.Company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Domain.Poe;


/**
 * Created by Lasse on 17-04-2015.
 */

import javax.xml.crypto.Data;
import javax.xml.transform.Result;
import java.security.interfaces.RSAKey;
import java.sql.*;
import java.util.ArrayList;

public class PoeMapper {

    public boolean addPoeFile(int project_id, String filename, int user_id, String filetype, int stage, Connection con) {

        String SQL = "insert into poes values(?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = null;

        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        Timestamp timestamp = new Timestamp(date.getTime());

        try {
            statement = con.prepareStatement(SQL);

            statement.setInt(1, getNextPoEId());
            statement.setInt(2, project_id);
            statement.setString(3, filename);
            statement.setInt(4, user_id);
            statement.setTimestamp(5, timestamp);
            statement.setString(6, filetype);
            statement.setTimestamp(7, null);
            statement.setInt(8, stage);

            statement.executeUpdate();


        } catch(Exception e) {
            return false;
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return true;
    }

    public boolean deletePoe(int fileId, Connection con) {

        String SQL = "delete from poes where id=?";
        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(SQL);

            statement.setInt(1, fileId);

            statement.executeUpdate();

        } catch (Exception e) {
            System.out.println("couldn't delete poe from db");
            return false;
        }finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return true;

    }

    public boolean markDeletePoe(int fileId, Connection con) {
        String SQL = "update poes set deletion_date=? where id=?";
        PreparedStatement statement = null;

        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        Timestamp timestamp = new Timestamp(date.getTime());

        try {
            statement = con.prepareStatement(SQL);

            statement.setTimestamp(1, timestamp);
            statement.setInt(2, fileId);

            statement.executeUpdate();

        } catch (Exception e) {
            System.out.println("couldn't delete poe from db");
            return false;
        }finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return true;

    }

    public ArrayList<Poe> getPoe(int project_id, Connection con) {

        String SQL = "select * from poes where project_id = ? order by creation_date";
        //int amountPoes = getAmountPoesByProjectId(project_id);
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<Poe> PoeCollection = new ArrayList<>();


        try {
            statement = con.prepareStatement(SQL);

            statement.setInt(1, project_id);

            rs = statement.executeQuery();


            while (rs.next()) {
                PoeCollection.add(new Poe(rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getTimestamp(5),
                        rs.getString(6),
                        rs.getTimestamp(7),
                        rs.getInt(8)
                ));
            }



        } catch (Exception e) {
            System.out.println("error in fisk");
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }


        return PoeCollection;

    }

    public int getNextPoEId() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {};
        String SQL = "select MAX(id) from poes";
        int id = 0;

        try {
            statement = con.prepareStatement(SQL);
            rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Error in PoeMapper - getNextPoEId()");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return id + 1;
    }

}