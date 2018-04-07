package blih.epitools.com.mobileblih.CallBacks;


import blih.epitools.com.mobileblih.Activities.AuthActivity;
import blih.epitools.com.mobileblih.POJO.Token;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthCallBack implements Callback<Token> {

    AuthActivity context;

    public AuthCallBack(AuthActivity _context) {
        context = _context;
    }

    @Override
    public void onResponse(Call<Token> call, Response<Token> response) {
        if (response.isSuccessful())
        {
            context.loadMainActivity(response.body().getToken());
        }
    }

    @Override
    public void onFailure(Call<Token> call, Throwable t) {

    }
}
