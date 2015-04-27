package Domain;

/**
 * Created by Andreas Poulsen on 23-Apr-15.
 */
public class Result {

    public String type;
    public String query;
    public int id;
    public String body;

    public Result(String type, String query, int id, String body) {
        this.type = type;
        this.query = query;
        this.id = id;
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public String getQuery() {
        return query;
    }

    public int getId() {
        return id;
    }

    public String getBody() {
        return body;
    }
}
