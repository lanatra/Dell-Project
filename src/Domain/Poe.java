package Domain;

import javafx.scene.web.HTMLEditor;

import java.io.File;
import java.net.URLEncoder;
import java.sql.Time;
import java.sql.Timestamp;


public class Poe {

    int id;
    int proj_id;
    String filename;
    int user_id;
    Timestamp date;
    Timestamp deletion_date;
    String filetype;

    long f_date;
    long f_deletion_date;

    public Poe(int id, int proj_id, String filename, int user_id, Timestamp date, String filetype, Timestamp deletion_date) {
        this.id = id;
        this.proj_id = proj_id;
        this.filename = filename;
        this.user_id = user_id;
        this.date = date;
        this.filetype = filetype;
        this.deletion_date = deletion_date;

        if(date != null) f_date = date.getTime();
        if(deletion_date != null) f_deletion_date = deletion_date.getTime();
    }

    public int getId() {
        return id;
    }

    public int getProj_id() {
        return proj_id;
    }

    public String getFilename() {
        return URLEncoder.encode(filename);
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

    public long getF_date() {
        return f_date;
    }

    public long getF_deletion_date() {
        return f_deletion_date;
    }

    public String getFilePath() {
        return System.getenv("POE_FOLDER") + File.separator + proj_id + File.separator + filename;
    }

}