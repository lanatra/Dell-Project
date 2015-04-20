package DataLayer;

import Domain.Message;

import java.sql.*;
import java.util.ArrayList;
public class MessageMapper {

    public ArrayList getMessagesByProjectId(int id, Connection con) {
        ArrayList<Message> messages = new ArrayList<>();
        String SQL = "select * from messages where project_id =" + id;

        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = con.prepareStatement(SQL);

            rs = statement.executeQuery();
            while (rs.next()) {
                messages.add(new Message(rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getTimestamp(5)
                ));
            }

        } catch (Exception e) {
            System.out.println("Error in MessageMapper.getMessagesByProjectId");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return messages;
    }

    public Message getMessageById(int id, Connection con) {
        Message message = null;
        String SQL = "select * from messages where id =" + id;

        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = con.prepareStatement(SQL);

            rs = statement.executeQuery();
            if (rs.next()) {
                message = new Message(rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getTimestamp(5)
                );
            }

        } catch (Exception e) {
            System.out.println("Error in MessageMapper.getMessagesByProjectId");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return message;
    }

    public Message postMessage(int userId, int projectId, String body, int companyId, Connection con) {

        String SQL = "insert into messages values (?,?,?,?,?)";

        PreparedStatement statement = null;
        int nextMessageId = 0;
        try {
            statement = con.prepareStatement(SQL);
            nextMessageId = getNextMessageId(con);

            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            Timestamp timestamp = new Timestamp(date.getTime());

            statement.setInt(1, nextMessageId);
            statement.setInt(2, userId);
            statement.setInt(3, projectId);
            statement.setString(4, body);
            statement.setTimestamp(5, timestamp);
            statement.executeUpdate();

            DatabaseFacade facade = new DatabaseFacade();
            facade.updateChangeDate(projectId, companyId);
            facade.updateNotification(projectId, "New message!");
            if(companyId == 1)
                facade.markUnread(projectId, 2); // 2 is just not dell, a la partner
            else
                facade.markUnread(projectId, 1);

        } catch (SQLException t) {
            System.out.println("SQLException in Messagemapper() - postMessage*(");
        }
        catch (Exception e) {
            System.out.println("Error in Messagemapper - postMessage()");
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        try {
              con = DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {};
        return getMessageById(nextMessageId, con);
    }

    public int getNextMessageId(Connection con) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String SQL = "select MAX(id) from messages";
        int id = 0;
        try {
            statement = con.prepareStatement(SQL);
            rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Error in MessageMapper - getNextMessageId()");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (con != null) try { con.close(); } catch (SQLException e) {e.printStackTrace();}
        }

        return id + 1;
    }
}
