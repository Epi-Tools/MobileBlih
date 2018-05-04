package blih.epitools.com.mobileblih.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import blih.epitools.com.mobileblih.API.BlihAPI;
import blih.epitools.com.mobileblih.CallBacks.AuthCallBack;
import blih.epitools.com.mobileblih.POJO.UserCredits;
import blih.epitools.com.mobileblih.POJO.UserToken;
import blih.epitools.com.mobileblih.R;
import blih.epitools.com.mobileblih.POJO.User;
import blih.epitools.com.mobileblih.Utils.Utils;
import retrofit2.Call;


public class AuthActivity extends AppCompatActivity {

    private EditText email;

    /**
     * @param savedInstanceState
     *
     * Initialise AuthActivity and GitHub Link
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserCredentials();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.auth_layout);
        underLineGithub();
    }

    /**
     * @param view auth_layout
     *
     * When click on Login button, perform Authentication API Call
     */
    public void AuthBlihAPI(View view)
    {
        email = (EditText) findViewById(blih.epitools.com.mobileblih.R.id.email);
        EditText pwd = (EditText) findViewById(blih.epitools.com.mobileblih.R.id.password);

        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        final Call<UserToken> call = service.authUser(new UserCredits(email.getText().toString(), pwd.getText().toString()));

        Utils.showLoading(this, "Authenticating...");
        call.enqueue(new AuthCallBack(this));
    }

    /**
     * @param token Access token from API response
     *
     *  Launch MainActivity and save both email and access token
     */
    public void loadMainActivity(String token)
    {
        Intent intent = new Intent(this, MainActivity.class);
        User.getInstance().setUserInfos(email.getText().toString(), token);
        saveCredentials(email.getText().toString(), token);
        startActivity(intent);
    }

    /**
     * @param email user email
     * @param token access token from API response
     */
    public void saveCredentials(String email, String token) {
        SharedPreferences.Editor editor = getSharedPreferences("credentials", MODE_PRIVATE).edit();
        editor.putString("email", email);
        editor.putString("token", token);
        editor.apply();
    }

    /**
     * Get credential from sharedPreferences and access mainActivity if they exist
     */
    private void getUserCredentials() {
        SharedPreferences prefs = getSharedPreferences("credentials", MODE_PRIVATE);
        String userEmail = prefs.getString("email", null);
        if (userEmail != null)
        {
            String userToken = prefs.getString("token", null);
            User.getInstance().setUserInfos(userEmail, userToken);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }


    /**
     * Manage Github link under the Login Button
     */
    private void underLineGithub() {
        final TextView textView = (TextView) findViewById(R.id.github_button);
        SpannableString ss = new SpannableString(textView.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Epi-Tools/MobileBlih"));
                startActivity(browserIntent);
                textView.invalidate();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                if (textView.isPressed()) {
                    ds.setUnderlineText(true);
                    ds.setColor(Color.parseColor("#5c8898"));
                    textView.invalidate();
                } else {
                    ds.setUnderlineText(false);
                    ds.setColor(Color.WHITE);
                }
            }
        };
        ss.setSpan(clickableSpan, 0, textView.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
