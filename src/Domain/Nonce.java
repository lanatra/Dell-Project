package Domain;

import java.sql.Timestamp;

/**
 * Created by Andreas Poulsen on 28-Apr-15.
 */
public class Nonce {

    public int id;
    public String nonce;
    public int associate_id;
    public Timestamp timestamp;
    public String type;

    public Nonce() {

    }

    public Nonce(int id, String nonce, int associate_id, Timestamp timestamp, String type) {
        this.id = id;
        this.nonce = nonce;
        this.associate_id = associate_id;
        this.timestamp = timestamp;
        this.type = type;
    }





    public int getId() {
        return id;
    }

    public String getNonce() {
        return nonce;
    }

    public int getAssociate_id() {
        return associate_id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }
}
