package sample;

import org.json.JSONArray;

import java.util.HashMap;

/**
 * Created by User on 29.03.2017.
 */
public class CachedPages {
    private static int totalComponentNumber = 0;

    private static HashMap<Integer,JSONArray> map = new HashMap<Integer,JSONArray>();

    public static int getTotalComponentNumber() {
        return totalComponentNumber;
    }

    public static void setTotalComponentNumber(int total) {
        totalComponentNumber = total;
    }

    public static JSONArray getPageElements(int i) {
        return map.get(i);
    }

    public static void setMap(int i, JSONArray jsonArray) {
        map.put(i, jsonArray);
    }
    public static void clearMap() {
        if(!map.isEmpty())
            map.clear();
    }
}
