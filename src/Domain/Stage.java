package Domain;

import java.sql.Timestamp;
import java.util.Comparator;

public class Stage {

    public int id;

    public int user_id;
    public int project_id;
    public Long date;
    public String type;

    public User user;

    public Stage(int id, int user_id, int project_id, Timestamp date, String type) {
        this.id = id;
        this.user_id = user_id;
        this.project_id = project_id;
        if(date != null) this.date = date.getTime();
        this.type = type;
    }

    public static final Comparator<Stage> TIME = (Stage o1, Stage o2) -> o1.date.compareTo(o2.date);

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public Long getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public User getUser() {
        return user;
    }

}
