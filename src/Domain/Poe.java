package Domain;

import java.sql.Timestamp;

/**
 * Created by Lasse on 17-04-2015.
 */
public class Poe {

    String path = "C:\\Users\\Lasse\\Desktop\\File Storage Dell";
    int id;
    int proj_id;
    String filename;
    int user_id;
    Timestamp date;
    String filetype;

    public Poe(int id, int proj_id, String filename, int user_id, Timestamp date, String filetype) {
        this.id = id;
        this.proj_id = proj_id;
        this.filename = filename;
        this.user_id = user_id;
        this.date = date;
        this.filetype = filetype;
    }

    public int getId() {
        return id;
    }

    public int getProj_id() {
        return proj_id;
    }

    public String getFilename() {
        return filename;
    }

    public int getUser_id() {
        return user_id;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getFiletype() {
        return filetype;
    }

    public String getFilePath() {
        return System.getenv("POE_FOLDER") + "\\" + proj_id + "\\" + filename;
    }

}