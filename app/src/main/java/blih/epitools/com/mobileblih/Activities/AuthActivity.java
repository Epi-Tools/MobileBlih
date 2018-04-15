package blih.epitools.com.mobileblih.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import blih.epitools.com.mobileblih.API.BlihAPI;
import blih.epitools.com.mobileblih.CallBacks.AuthCallBack;
import blih.epitools.com.mobileblih.Constant;
import blih.epitools.com.mobileblih.POJO.UserCredits;
import blih.epitools.com.mobileblih.POJO.UserToken;
import blih.epitools.com.mobileblih.R;
import blih.epitools.com.mobileblih.POJO.User;
import blih.epitools.com.mobileblih.Utils.Utils;
import retrofit2.Call;


public class AuthActivity extends AppCompatActivity {

    EditText email;
    //TODO add Github button to login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.auth_layout);
    }

    public void AuthBlihAPI(View view)
    {
        email = (EditText) findViewById(blih.epitools.com.mobileblih.R.id.email);
        EditText pwd = (EditText) findViewById(blih.epitools.com.mobileblih.R.id.password);

        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        // TODO error text handler on UI
        // TODO add alert loading on overlay
        // TODO remove Constant email and Constant pwd

        final Call<UserToken> call = service.authUser(new UserCredits(Constant.email, Constant.pwd));
        Utils.showLoading(this, "Authenticating...");
        call.enqueue(new AuthCallBack(this));
    }

    public void loadMainActivity(String token)
    {
        Intent intent = new Intent(this, MainActivity.class);
        User.getInstance().setUserInfos(Constant.email, token);
        startActivity(intent);
    }

}
