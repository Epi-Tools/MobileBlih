package blih.epitools.com.mobileblih;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class AclActivity extends AppCompatActivity {

    Project currentProjet;
    String email;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDatasFromMain();

        setContentView(R.layout.acl_layout);
        setupEvents();
    }

    private void getDatasFromMain() {
        Gson gson = new Gson();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("PROJECT");
            currentProjet = gson.fromJson(value, Project.class);
            email = extras.getString("EMAIL");
            token = extras.getString("TOKEN");
        }
    }

    public void setupEvents() {
        TextView title = (TextView) findViewById(R.id.title_repo);
        title.setText(currentProjet.getName());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_acl);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createACL();
            }
        });
        Button delete = (Button) findViewById(R.id.delete_repo);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRepo();
            }
        });
    }

    private void deleteRepo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Delete")
                .setMessage("Do you really want to delete " + currentProjet.getName())
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            sendDeleteRequest();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    private void sendDeleteRequest() throws JSONException {
        String serverUrl = getResources().getString(R.string.server_url) + getResources().getString(R.string.delete);

        JSONObject obj = new JSONObject();

        obj.put("email", email);
        obj.put("token", token);
        obj.put("name", currentProjet.getName());
        obj.put("acl", true);

        Log.e("Datas", serverUrl);

        Log.e("acl", obj.toString());

        JsonObjectRequest req = new JsonObjectRequest(serverUrl, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response: ", response.toString());
                        try {
                            Snackbar.make(findViewById(R.id.acl_view),
                                    response.getJSONObject("body").get("message").toString(), Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            try {
                                Snackbar.make(findViewById(R.id.acl_view),
                                        response.get("err").toString(), Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Snackbar.make(findViewById(R.id.acl_view), "Blih is unreacheable. Please check your internet connection and try again.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        req.setShouldCache(false);
        RequestHandler.getInstance(this).addToRequestQueue(req);
    }

    private void createACL() {

        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.acl_creator, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add ACL").setView(dialogView);

        final EditText username = (EditText) dialogView.findViewById(R.id.username_acl);
        final EditText acl = (EditText) dialogView.findViewById(R.id.acl_given);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Snackbar.make(findViewById(R.id.acl_view), username.getText() + " has been given " + acl.getText() + " acl.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();

    }
}