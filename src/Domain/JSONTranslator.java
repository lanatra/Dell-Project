package Domain;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
                if(item.matches("^[\\d\\.]+$"))
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
    public static String stringArrayArrayListWithOptions(ArrayList<String[]> list, String o) {
        String res = stringArrayArrayList(list);
        String options = "";
        for(int i = 0; i < o.split(",").length; i++)
            options += "\"" + o.split(",")[i] + "\",";
        options = options.substring(0, options.length() - 1);
        res = res.substring(0,1) + "[" + options + "]," + res.substring(1);
        return res;
    }

}
