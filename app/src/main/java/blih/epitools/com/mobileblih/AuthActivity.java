package blih.epitools.com.mobileblih;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AuthActivity extends AppCompatActivity {

    EditText emailAddress;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_layout);
        setupEvents();
    }

    private void setupEvents()
    {
        emailAddress = (EditText) findViewById(R.id.email);

        password = (EditText) findViewById(R.id.password);

        Button valid = (Button) findViewById(R.id.valid);
        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToBlih();
            }
        });
    }

    private void connectToBlih()
    {
        Snackbar.make(findViewById(R.id.auth_view), emailAddress.getText(), Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

}
