package blih.epitools.com.mobileblih.CallBacks;

import blih.epitools.com.mobileblih.Activities.AclActivity;
import blih.epitools.com.mobileblih.POJO.UserToken;
import blih.epitools.com.mobileblih.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AclUpdateCallBack implements Callback<UserToken> {

    private AclActivity context;

    public AclUpdateCallBack(AclActivity _context) {
        context = _context;
    }

    /**
     * @param call
     * @param response
     *
     * Callback to update a specific user acl
     */
    @Override
    public void onResponse(Call<UserToken> call, Response<UserToken> response) {
        Utils.hideLoading();
        if (response.isSuccessful()) {
            try {
                try {
                    Utils.alertManager(context, "ACL", response.body().getBody().getMessage());
                } catch (NullPointerException ex) {
                    Utils.alertManager(context, "ACL", response.body().get_body().getMessage());
                }
            } catch (NullPointerException ex) {
                Utils.alertManager(context, "ACL", response.body().getErr());
            }
            context.getAclList();
        } else {
            Utils.alertManager(context, "ACL", response.message());
        }
    }

    /**
     * @param call
     * @param t
     *
     * Callback when the call doesn't work
     */
    @Override
    public void onFailure(Call<UserToken> call, Throwable t) {
        Utils.hideLoading();
        Utils.alertManager(context, "ACL", "Blih is unreacheable. Please check your internet connection and try again.");
    }

}

