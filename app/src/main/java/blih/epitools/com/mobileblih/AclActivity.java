package blih.epitools.com.mobileblih;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class AclActivity extends AppCompatActivity {

    private Project currentProjet;
    private String email;
    private String token;
    private ListViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Project> list;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDatasFromMain();

        setContentView(R.layout.acl_layout);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setupEvents();
    }

    private void getDatasFromMain() {
        Gson gson = new Gson();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("PROJECT");
            Log.e("Response: ", value);
            currentProjet = gson.fromJson(value, Project.class);
            email = extras.getString("EMAIL");
            token = extras.getString("TOKEN");
        }
        list = new ArrayList<>();
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

        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new ListViewAdapter(list);

        adapter.setOnItemClickListener(new ListViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(final int position, View v) {
               editAcl(position);
            }
        });
        recyclerView.setAdapter(adapter);
        try {
            setupAclList();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

        spinner = (Spinner) dialogView.findViewById(R.id.spinner_acl_create);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.acls_creator, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Snackbar.make(findViewById(R.id.acl_view),
                                username.getText() + " has been given " + " acl.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();

    }

    private void setupAclList() throws JSONException {
        String serverUrl = getResources().getString(R.string.server_url) + getResources().getString(R.string.acl_list);

        JSONObject obj = new JSONObject();

        obj.put("email", email);
        obj.put("token", token);
        obj.put("name", currentProjet.getName());

        Log.e("Datas", serverUrl);

        Log.e("acl", obj.toString());

        JsonObjectRequest req = new JsonObjectRequest(serverUrl, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response: ", response.toString());
                        try {
                            Log.e("Response: ", response.get("body").toString());
                            parseProjectsList(response.getJSONObject("body"));
                            adapter.refreshList(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            try {
                                Snackbar.make(findViewById(R.id.acl_view),
                                        response.get("err").toString(), Snackbar.LENGTH_LONG)
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
                Snackbar.make(findViewById(R.id.acl_view),
                        "Blih is unreacheable. Please check your internet connection and try again.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        req.setShouldCache(false);
        RequestHandler.getInstance(this).addToRequestQueue(req);
    }

    private void parseProjectsList(JSONObject obj)
    {
        list = new ArrayList<>();
            Iterator<String> keys = obj.keys();
            JSONArray array = obj.names();
            Log.e("List acl", array.toString());

            for (int i = 0; i < array.length(); i++)
            {
                try {
                    String key = array.get(i).toString();
                    Project aclList = new Project(key, obj.get(key).toString());
                    list.add(aclList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    }

    private void editAcl(int pos)
    {
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.edit_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit ACL").setView(dialogView);

        if (list.size() != 0)
        {
            final TextView username = (TextView) dialogView.findViewById(R.id.username_acl);

            final String[] aclSelected = {"r"};

            username.setText(list.get(pos).getName());

            spinner = (Spinner) dialogView.findViewById(R.id.spinner_acl_edit);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.acls_editor, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    aclSelected[0] = parent.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (aclSelected[0].equals("None"))
                    aclSelected[0] = "";
                updateAcl(username.getText().toString(), aclSelected[0]);

            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void updateAcl(String user, String acl) {
        String serverUrl = getResources().getString(R.string.server_url) + getResources().getString(R.string.update_acl);

        JSONObject obj = new JSONObject();

        try {
            obj.put("email", email);
            obj.put("token", token);
            obj.put("name", user);
            obj.put("repoName", currentProjet.getName());
            obj.put("acl", acl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("Datas", serverUrl);

        Log.e("acl", obj.toString());

        JsonObjectRequest req = new JsonObjectRequest(serverUrl, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response: ", response.toString());
                        try {
                            Log.e("Response: ", response.get("body").toString());
                            Snackbar.make(findViewById(R.id.acl_view),
                                    response.getJSONObject("body").get("message").toString(), Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            setupAclList();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            try {
                                Snackbar.make(findViewById(R.id.acl_view),
                                        response.get("error").toString(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                                try {
                                    Snackbar.make(findViewById(R.id.acl_view),
                                            response.get("err").toString(), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                } catch (JSONException e2) {
                                    e2.printStackTrace();
                                }
                            }
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Snackbar.make(findViewById(R.id.acl_view),
                        "Blih is unreacheable. Please check your internet connection and try again.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        req.setShouldCache(false);
        RequestHandler.getInstance(this).addToRequestQueue(req);
    }

}