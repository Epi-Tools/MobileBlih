package blih.epitools.com.mobileblih.CallBacks;


import android.util.Log;

import blih.epitools.com.mobileblih.Activities.AuthActivity;
import blih.epitools.com.mobileblih.POJO.UserToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthCallBack implements Callback<UserToken> {

    private AuthActivity context;

    public AuthCallBack(AuthActivity _context) {
        context = _context;
    }

    //TODO callback management
    @Override
    public void onResponse(Call<UserToken> call, Response<UserToken> response) {
        if (response.isSuccessful()) {
            context.loadMainActivity(response.body().getToken());
        } else {
            Log.e("Error", response.message());
        }
    }

    @Override
    public void onFailure(Call<UserToken> call, Throwable t) {
        Log.e("failure", t.getStackTrace().toString());
    }
}
