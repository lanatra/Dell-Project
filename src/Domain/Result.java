package Domain;

/**
 * Created by Andreas Poulsen on 23-Apr-15.
 */
public class Result {

    public String type;
    public int id;
    public String body;
    public int rating;

    public Result(String type, int id, String body, int rating) {
        this.type = type;
        this.id = id;
        this.body = body;
        this.rating = rating;
    }

    public String getType() {
        return type;
    }

    public int getRating() {
        return rating;
    }

    public int getId() {
        return id;
    }

    public String getBody() {
        return body;
    }
}
