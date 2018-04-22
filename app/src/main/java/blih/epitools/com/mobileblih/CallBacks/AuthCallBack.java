package blih.epitools.com.mobileblih.CallBacks;


import android.util.Log;

import blih.epitools.com.mobileblih.Activities.AuthActivity;
import blih.epitools.com.mobileblih.POJO.UserToken;
import blih.epitools.com.mobileblih.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthCallBack implements Callback<UserToken> {

    private AuthActivity context;

    public AuthCallBack(AuthActivity _context) {
        context = _context;
    }

    @Override
    public void onResponse(Call<UserToken> call, Response<UserToken> response) {
        Utils.hideLoading();
        if (response.isSuccessful()) {
            Log.e("Response", response.message());
            if (response.body().getToken() == null) {
                if (response.body().getError() == null) {
                    Utils.alertManager(context, "Authentication", response.body().getErr());
                } else {
                    Utils.alertManager(context, "Authentication", response.body().getError());
                }
            } else {
                context.loadMainActivity(response.body().getToken());
            }
        } else {
            Utils.alertManager(context, "Authentication", response.message());
        }
    }

    @Override
    public void onFailure(Call<UserToken> call, Throwable t) {
        Utils.hideLoading();
        Utils.alertManager(context, "Authentication", "Blih is unreacheable. Please check your internet connection and try again.");
    }

}
