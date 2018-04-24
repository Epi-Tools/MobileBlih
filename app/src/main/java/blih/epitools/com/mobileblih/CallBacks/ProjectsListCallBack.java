package blih.epitools.com.mobileblih.CallBacks;


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

    /**
     * @param call
     * @param response
     *
     * Callback to get the repository list
     */
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
            Utils.alertManager(context, "List", response.message());
        }
    }

    /**
     * @param call
     * @param t
     *
     * Callback when the call isn't working
     */
    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Utils.hideLoading();
        Utils.alertManager(context, "List", "Blih is unreacheable. Please check your internet connection and try again.");
    }

}

