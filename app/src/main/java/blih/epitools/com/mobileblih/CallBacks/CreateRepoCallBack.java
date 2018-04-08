package blih.epitools.com.mobileblih.CallBacks;

import android.util.Log;

import blih.epitools.com.mobileblih.Activities.MainActivity;
import blih.epitools.com.mobileblih.POJO.UserToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRepoCallBack implements Callback<UserToken> {

    private MainActivity context;

    public CreateRepoCallBack(MainActivity _context) {
        context = _context;
    }

    @Override
    public void onResponse(Call<UserToken> call, Response<UserToken> response) {
        if (response.isSuccessful()) {

        } else {
            Log.e("Error", response.message());
        }
    }

    @Override
    public void onFailure(Call<UserToken> call, Throwable t) {
        Log.e("failure", t.getStackTrace().toString());
    }
}