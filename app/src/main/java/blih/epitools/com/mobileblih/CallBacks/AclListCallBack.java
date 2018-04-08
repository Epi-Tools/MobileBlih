package blih.epitools.com.mobileblih.CallBacks;

import android.util.Log;

import java.io.IOException;

import blih.epitools.com.mobileblih.Activities.AclActivity;
import blih.epitools.com.mobileblih.Utils.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AclListCallBack implements Callback<ResponseBody> {

    private AclActivity context;

    public AclListCallBack(AclActivity _context) {
        context = _context;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                context.getAclListFromCallBack(Utils.parseProjectsList(response.body().string()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Error", response.message());
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.e("failure", t.getStackTrace().toString());

    }
}
