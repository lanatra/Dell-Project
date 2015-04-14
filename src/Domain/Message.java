package Domain;

import java.sql.Timestamp;
import java.util.ArrayList;

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
        this.body = body;
        this.creation_date = creation_date;

        if(creation_date != null) creation_date_millis = creation_date.getTime();
    }

    public String toHTML() {
        String html = "        <div class=\"message-item\">\n" +
                "            <span class=\"id\"><strong>#" + id + "</span>\n" +
                "            <span class=\"partner\">" + user.getName() + "</span>\n" +
                "            <span class=\"partner\">" + company.getName() + "</span>\n" +
                "            <span class=\"partner\">" + company.getImg_filename() + "</span>\n" +
                "            <span class=\"body\">" + body + "</span>\n" +
                "            <span class=\"notification small\">" + creation_date_millis  + "</span>\n" +
                "        </div>";


        return html;
    }

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

    public int compareTo(Message o) {
        return Comparators.TIME.compare(this, o);
    }
}
