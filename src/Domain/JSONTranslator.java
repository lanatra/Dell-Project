package Domain;

import java.lang.reflect.Array;
import java.util.ArrayList;
import static org.apache.commons.lang3.StringUtils.*;

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

    public static String stringArrayArrayList(ArrayList<String[]> list) {
        String res = "[";
        for(String[] itemSet : list) {
            res += "[";
            for(String item : itemSet) {
                if(isNumeric(item))
                    res += item + ",";
                else
                    res += "\"" + item + "\",";
            }
            res = res.substring(0, res.length()-1);
            res += "],";
        }
        res = res.substring(0, res.length()-1);

        return res + "]";
    }
}
