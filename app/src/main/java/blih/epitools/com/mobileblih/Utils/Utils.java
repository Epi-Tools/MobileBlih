package blih.epitools.com.mobileblih.Utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Utils {

    public static List<String> parseProjectsList(JSONObject request) {
        JSONObject obj = null;
        try {
            obj = request.getJSONObject("body").getJSONObject("repositories");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<String> list = new ArrayList<>();
        Iterator<String> keys = obj.keys();
        JSONArray array = obj.names();

        for (int i = 0; i < array.length(); i++) {
            try {
                String key = array.get(i).toString();
                list.add(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
