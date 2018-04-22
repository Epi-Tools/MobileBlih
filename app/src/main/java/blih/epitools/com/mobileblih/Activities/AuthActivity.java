package blih.epitools.com.mobileblih.Activities;

import android.content.Intent;
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
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import blih.epitools.com.mobileblih.API.BlihAPI;
import blih.epitools.com.mobileblih.CallBacks.AuthCallBack;
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
        underLineGithub();
    }

    public void AuthBlihAPI(View view)
    {
        email = (EditText) findViewById(blih.epitools.com.mobileblih.R.id.email);
        EditText pwd = (EditText) findViewById(blih.epitools.com.mobileblih.R.id.password);

        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        final Call<UserToken> call = service.authUser(new UserCredits(email.getText().toString(), pwd.getText().toString()));

        Utils.showLoading(this, "Authenticating...");
        call.enqueue(new AuthCallBack(this));
    }

    public void loadMainActivity(String token)
    {
        Intent intent = new Intent(this, MainActivity.class);
        User.getInstance().setUserInfos(email.getText().toString(), token);
        startActivity(intent);
    }

    public void githubAccess(View view) {

    }

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
