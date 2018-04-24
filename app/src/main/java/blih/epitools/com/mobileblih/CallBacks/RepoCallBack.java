package blih.epitools.com.mobileblih.CallBacks;

import android.content.Context;

import blih.epitools.com.mobileblih.Activities.AclActivity;
import blih.epitools.com.mobileblih.Activities.MainActivity;
import blih.epitools.com.mobileblih.POJO.UserToken;
import blih.epitools.com.mobileblih.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoCallBack implements Callback<UserToken> {

    private Context context;

    public RepoCallBack(Context _context) {
        context = _context;
    }

    /**
     * @param call
     * @param response
     *
     * Callback to either create or delete a repository in both main or acl activity
     */
    @Override
    public void onResponse(Call<UserToken> call, Response<UserToken> response) {
        Utils.hideLoading();
        if (response.isSuccessful()) {
            if (context.getClass().getName().contains("MainActivity")) {
                try {
                    try {
                        Utils.alertManager(context, "Repo", response.body().getBody().getMessage());
                    } catch (NullPointerException ex) {
                        Utils.alertManager(context, "Repo", response.body().get_body().getMessage());
                    }
                } catch (NullPointerException ex) {
                    Utils.alertManager(context, "Repo", response.body().getErr());
                }
                ((MainActivity) context).getRepoList();
            } else
                ((AclActivity) context).finish();
        } else {
            Utils.alertManager(context, "Repo", response.message());
        }
    }

    /**
     * @param call
     * @param t
     *
     * Callback when the call isn't working
     */
    @Override
    public void onFailure(Call<UserToken> call, Throwable t) {
        Utils.hideLoading();
        Utils.alertManager(context, "Repo", "Blih is unreacheable. Please check your internet connection and try again.");
    }

}