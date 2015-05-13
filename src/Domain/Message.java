package Domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;

public class Message {

    public int id;
    public int author_id;
    public int project_id;
    public String body;
    public Timestamp creation_date;

    public Long creation_date_millis;

    public User user;
    public Company company;

    public Message(int id, int author_id, int project_id, String body, Timestamp creation_date) {
        this.id = id;
        this.author_id = author_id;
        this.project_id = project_id;
        this.body = "<p>" + body.replaceAll("\\n","</p><p>")  + "</p>";
        this.creation_date = creation_date;

        if(creation_date != null) creation_date_millis = creation_date.getTime();
    }

    public String toHTML() {
        String html = "      <div class=\"item message pull-right\">" +
                "               <span class=\"user-data\">" + user.getName() + " - " + company.getName() + "</span>" +
                "               <span class=\"date isDate\">" + creation_date_millis + "</span>" +
                "               <div class=\"inner-bubble\">" +
                "                   <p>" + body + "</p>" +
                "               </div>" +
                "            </div>";


        return html;
    }

    public static final Comparator<Message> TIME = (Message o1, Message o2) -> o1.creation_date_millis.compareTo(o2.creation_date_millis);

    public int getId() {
        return id;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public String getBody() {
        return body;
    }

    public Timestamp getCreation_date() {
        return creation_date;
    }

    public Long getCreation_date_millis() {
        return creation_date_millis;
    }

    public User getUser() {
        return user;
    }

    public Company getCompany() {
        return company;
    }
}
