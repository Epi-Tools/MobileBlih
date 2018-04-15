package blih.epitools.com.mobileblih.CallBacks;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import blih.epitools.com.mobileblih.Activities.AclActivity;
import blih.epitools.com.mobileblih.Activities.MainActivity;
import blih.epitools.com.mobileblih.POJO.UserToken;
import blih.epitools.com.mobileblih.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoCallBack implements Callback<UserToken> {

    private Context context;

    // TODO handle error management
    public RepoCallBack(Context _context) {
        context = _context;
    }

    @Override
    public void onResponse(Call<UserToken> call, Response<UserToken> response) {
        Utils.hideLoading();
        if (response.isSuccessful()) {
            if (context.getClass().getName().contains("MainActivity")) {
                try {
                    try {
                        alertMessage(response.body().getBody().getMessage());
                    } catch (NullPointerException ex) {
                        alertMessage(response.body().get_body().getMessage());
                    }
                } catch (NullPointerException ex) {
                    alertMessage(response.body().getErr());
                }
                ((MainActivity) context).getRepoList();
            } else
                ((AclActivity) context).finish();
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
        alertDialog.setTitle("Repo");
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