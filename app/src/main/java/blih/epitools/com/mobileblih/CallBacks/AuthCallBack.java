package blih.epitools.com.mobileblih.CallBacks;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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

    //TODO callback management
    @Override
    public void onResponse(Call<UserToken> call, Response<UserToken> response) {
        Utils.hideLoading();
        if (response.isSuccessful()) {
            context.loadMainActivity(response.body().getToken());
        } else {
            Log.e("Error", response.message());
            alertMessage(response.message());
        }
    }

    @Override
    public void onFailure(Call<UserToken> call, Throwable t) {
        Utils.hideLoading();
        Log.e("failure", t.getStackTrace().toString());
        alertMessage("Blih is unreacheable. Please check your internet connection and try again.");
    }

    private void alertMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Authentication");
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
