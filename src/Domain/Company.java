package Domain;

/**
 * Created by Lasse on 10-04-2015.
 */
public class Company {

    int id;
    String name;
    String img_filename;
    String country_code;
    public Company(int id, String name, String img_filename, String country_code) {
        this.id = id;
        this.name = name;
        this.img_filename = img_filename;
        this.country_code = country_code;
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

    public String country_code() {
        return country_code;
    }
}
