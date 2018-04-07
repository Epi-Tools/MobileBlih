package blih.epitools.com.mobileblih.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import blih.epitools.com.mobileblih.API.BlihAPI;
import blih.epitools.com.mobileblih.CallBacks.AuthCallBack;
import blih.epitools.com.mobileblih.POJO.Token;
import blih.epitools.com.mobileblih.R;
import retrofit2.Call;


public class AuthActivity extends AppCompatActivity {

    Button valid;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.auth_layout);
    }

    public void AuthBlihAPI(View view)
    {
        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        EditText email = (EditText) findViewById(blih.epitools.com.mobileblih.R.id.email);
        EditText pwd = (EditText) findViewById(blih.epitools.com.mobileblih.R.id.password);

        // TODO error text handler on UI
        // TODO add alert loading on overlay
        Call<Token> call = service.authUser(email.getText().toString(), pwd.getText().toString());
        valid.setEnabled(false);
        call.enqueue(new AuthCallBack(this));
    }

    public void loadMainActivity(String token)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("TOKEN", token);
        intent.putExtra("EMAIL", email.getText().toString());
        valid.setEnabled(true);
        startActivity(intent);
    }

}
