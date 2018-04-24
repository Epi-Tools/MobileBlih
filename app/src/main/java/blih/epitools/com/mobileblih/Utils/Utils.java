package blih.epitools.com.mobileblih.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import blih.epitools.com.mobileblih.POJO.UserACL;

public class Utils {

    private static ProgressDialog pd;

    /**
     * @param request request to parse
     * @return parsed list
     *
     * Get requestJson from callback and convert it to a list of repo name
     */
    public static List<String> parseProjectsList(JSONObject request) {
        JSONObject obj = null;
        try {
            obj = request.getJSONObject("body").getJSONObject("repositories");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<String> list = new ArrayList<>();
        if (obj == null)
            return list;
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

    /**
     * @param data convert string jsonobject
     * @return parsed list
     *
     * Get requestJson from callback and convert it to a list of acl name
     */
    public static List<UserACL> parseProjectsList(String data) {
        JSONObject request = null;
        try {
            request = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject obj = null;
        try {
            obj = request.getJSONObject("body");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<UserACL> list = new ArrayList<>();
        if (obj == null) {
            return list;
        }
        Iterator<String> keys = obj.keys();
        JSONArray array = obj.names();

        for (int i = 0; i < array.length(); i++)
        {
            try {
                String key = array.get(i).toString();
                UserACL aclList = new UserACL(key, obj.get(key).toString());
                list.add(aclList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * @param ctx current context
     * @param Message message to show on
     *
     * show progressDialog on current Activity
     */
    public static void showLoading(Context ctx, String Message) {
        pd = new ProgressDialog(ctx);
        pd.setMessage(Message);
        pd.show();
    }

    /**
     * hide progressDialog
     */
    public static void hideLoading() {
        pd.hide();
    }


    /**
     * @param ctx current context
     * @param title alert title
     * @param message alert message
     *
     * Create a alert for each callback
     *
     */
    public static void alertManager(Context ctx, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
