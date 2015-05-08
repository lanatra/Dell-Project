package DataLayer;

import Domain.Nonce;
import Domain.Stage;

import java.sql.*;
import java.util.ArrayList;

public class NonceMapper {

    public int addNonce(Nonce nonce, Connection con) {
        PreparedStatement statement = null;
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        Timestamp timestamp = new Timestamp(date.getTime());
        int id = getNextNonceId();
        String SQL = "insert into nonces values(?,?,?,?,?)";

        try {
            statement = con.prepareStatement(SQL);

            statement.setInt(1, id);
            statement.setString(2, nonce.getNonce());
            statement.setInt(3, nonce.associate_id);
            statement.setTimestamp(4, timestamp);
            statement.setString(5, nonce.getType());

            statement.executeUpdate();

            return id;
        } catch (Exception e) {
            System.out.println("Exception in addNonce()");
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return -1;

    }

    public int getUserIdByNonce(String nonce, Connection con) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String SQL = "select associate_id from nonces where nonce=?";

        int id = 0;

        try {
            statement = con.prepareStatement(SQL);
            statement.setString(1, nonce);
            rs = statement.executeQuery();
            if(rs.next()) {
                id = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Error in NonceMapper - getUserIdByNonce()");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return id;
    }

    public int getNextNonceId() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {};
        String SQL = "select MAX(id) from nonces";
        int id = 0;

        try {
            statement = con.prepareStatement(SQL);
            rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Error in NonceMapper - getNextNonceId()");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return id + 1;
    }


    public void deleteNonce(String nonce, Connection con) {
        PreparedStatement statement = null;
        String SQL = "delete from nonces" +
                " where nonce=?";

        try {
            statement = con.prepareStatement(SQL);
            statement.setString(1, nonce);
            statement.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error in NonceMapper - deleteNonce()");
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

    }
}
