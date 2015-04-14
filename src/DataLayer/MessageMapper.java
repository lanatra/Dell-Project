package DataLayer;

import Domain.Message;

import java.sql.*;
import java.util.ArrayList;
public class MessageMapper {

    public ArrayList getMessagesByProjectId(int id, Connection con) {
        ArrayList<Message> messages = new ArrayList<>();
        String SQL = "select * from messages where project_id =" + id;

        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(SQL);

            ResultSet rs = statement.executeQuery();
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
        }

        return messages;
    }

    public Message getMessageById(int id, Connection con) {
        Message message = null;
        String SQL = "select * from messages where id =" + id;

        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement(SQL);

            ResultSet rs = statement.executeQuery();
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
        }

        return message;
    }

    public Message postMessage(int userId, int projectId, String body, Connection con) {

        String SQL = "insert into messages values (?,?,?,?,?)";

        PreparedStatement statement = null;
        int nextMessageId = 0;
        try {
            System.out.println("wehere1");
            statement = con.prepareStatement(SQL);
            System.out.println("wehere2");
            nextMessageId = getNextMessageId(con);
            System.out.println("wehere3");

            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            System.out.println("wehere4");
            Timestamp timestamp = new Timestamp(date.getTime());
            System.out.println("wehere5");

            statement.setInt(1, nextMessageId);
            statement.setInt(2, userId);
            statement.setInt(3, projectId);
            statement.setString(4, body);
            statement.setTimestamp(5, timestamp);
            System.out.println("wehere6");
            statement.executeUpdate();
            System.out.println("wehere7");

        } catch (SQLException t) {
            System.out.println("SQLException in Messagemapper() - postMessage*(");
        }
        catch (Exception e) {
            System.out.println("Error in Messagemapper - postMessage()");
        }

        return getMessageById(nextMessageId, con);
    }

    public int getNextMessageId(Connection con) {
        PreparedStatement statement = null;
        String SQL = "select MAX(id) from messages";
        int id = 0;
        try {
            statement = con.prepareStatement(SQL);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Error in MessageMapper - getNextMessageId()");
        }

        return id + 1;
    }
}
