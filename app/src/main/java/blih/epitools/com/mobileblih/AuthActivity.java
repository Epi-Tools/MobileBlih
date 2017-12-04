package blih.epitools.com.mobileblih;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AuthActivity extends AppCompatActivity {

    EditText email;
    EditText pwd;
    String token = null;
    String list = null;
    Gson gson;
    Button valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_layout);
        setupEvents();
    }

    private void HandleRequest()
    {
        String serverUrl = getResources().getString(R.string.server_url) + getResources().getString(R.string.auth);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email.getText().toString());
        params.put("pwd", pwd.getText().toString());

        Log.e("Datas", serverUrl);

        JsonObjectRequest req = new JsonObjectRequest(serverUrl, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response: ", response.toString());
                        try {
                            Log.e("Token", response.get("token").toString());

                            token = response.get("token").toString();

                            Log.e("List", response.getJSONObject("body").getJSONObject("repositories").toString());

                            list = response.getJSONObject("body").getJSONObject("repositories").toString();
                            connectToBlih();

                        } catch (JSONException e) {
                            try {
                                Snackbar.make(findViewById(R.id.auth_view), response.get("error").toString(), Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                                valid.setEnabled(true);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Snackbar.make(findViewById(R.id.auth_view), "Blih is unreacheable. Please check your internet connection and try again.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                valid.setEnabled(true);
            }
        });
        req.setShouldCache(false);
        RequestHandler.getInstance(this).addToRequestQueue(req);
    }

    private void setupEvents()
    {
        email = (EditText) findViewById(R.id.email);
        pwd = (EditText) findViewById(R.id.password);
        gson = new Gson();

        valid = (Button) findViewById(R.id.valid);
        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valid.setEnabled(false);
                HandleRequest();
            }
        });
    }

    private void connectToBlih()
    {
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        intent.putExtra("TOKEN", token);
        intent.putExtra("PROJECT_LIST", list);
        intent.putExtra("EMAIL", email.getText().toString());
        valid.setEnabled(true);
        startActivity(intent);
    }

}
