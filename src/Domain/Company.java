package Domain;

/**
 * Created by Lasse on 10-04-2015.
 */
public class Company {

    int id;
    String name;
    String img_filename;

    public Company(int id, String name, String img_filename) {
        this.id = id;
        this.name = name;
        this.img_filename = img_filename;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImg_filename() {
        return img_filename;
    }
}
