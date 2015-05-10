package Domain;

import java.util.ArrayList;

public class ResultsContainer {

    public String type;
    public ArrayList<Result> container;

    public ResultsContainer(String type) {
        this.type = type;
        container = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public ArrayList<Result> getContainer() {
        return container;
    }
}
