package Domain;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Andreas Poulsen on 27-Apr-15.
 */
public class JSONTranslator {

    public static String stringArrayList(ArrayList list) {
        String res = "[";

        for (String item : (ArrayList<String>) list) {
            res += "\"" + item + "\",";
        }
        res = res.substring(0, res.length()-1);

        return res + "]";
    }
}
