package blih.epitools.com.mobileblih.CallBacks;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import blih.epitools.com.mobileblih.Activities.MainActivity;
import blih.epitools.com.mobileblih.Utils.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectsListCallBack implements Callback<ResponseBody> {

    private MainActivity context;

    public ProjectsListCallBack(MainActivity _context) {
        context = _context;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        Utils.hideLoading();
        if (response.isSuccessful()) {
            try {
                try {
                    context.getListFromCallBack(Utils.parseProjectsList(new JSONObject(response.body().string())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            alertMessage(response.message());
            Log.e("Error", response.message());
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Utils.hideLoading();
        Log.e("failure", t.getStackTrace().toString());
        alertMessage("Blih is unreacheable. Please check your internet connection and try again.");
    }

    private void alertMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("List");
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

