package blih.epitools.com.mobileblih;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Project> list;
    private ProjectViewAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    String token;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDatas();
        setupEvents();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        reloadRepoList();
    }

    public void setupEvents() {
        // FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProject();
            }
        });

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new ProjectViewAdapter(list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ProjectViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Gson gson = new Gson();

                Intent intent = new Intent(MainActivity.this, AclActivity.class);
                intent.putExtra("PROJECT", gson.toJson(adapter.getItemAt(position)));
                startActivity(intent);
            }
        });

        // SearchView
        android.widget.SearchView search = (android.widget.SearchView) findViewById(R.id.searchView);
        search.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s != "")
                    adapter.refreshList(getCurrentList(s));
                return false;
            }
        });

        // Pull to refresh
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadRepoList();
            }
        });
    }

    private void reloadRepoList()
    {
        String serverUrl = getResources().getString(R.string.server_url) + getResources().getString(R.string.repo_list);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("token", token);

        Log.e("Datas", serverUrl);
        Log.e("email", email);

        JsonObjectRequest req = new JsonObjectRequest(serverUrl, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response: ", response.toString());
                        try {

                            Log.e("List", response.getJSONObject("body").getJSONObject("repositories").toString());

                            String repoList = response.getJSONObject("body").getJSONObject("repositories").toString();
                            parseProjectsList(repoList);
                            adapter.refreshList(list);
                        } catch (JSONException e) {
                            try {
                                Snackbar.make(findViewById(R.id.auth_view), response.get("error").toString(), Snackbar.LENGTH_SHORT)
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
            }
        });
        req.setShouldCache(false);
        RequestHandler.getInstance(this).addToRequestQueue(req);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void createProject() {
        final EditText repository = new EditText(this);
        repository.setHint("Repository Name");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Create Repository")
                .setView(repository)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String url = repository.getText().toString();
                        Snackbar.make(findViewById(R.id.main_view), repository.getText() + " has been created.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Project> getCurrentList(String s)
    {
        ArrayList currentList = new ArrayList();
        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).getName().toLowerCase().contains(s.toLowerCase()))
                currentList.add(list.get(i));
        }
        return currentList;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
    }

    private void getDatas() {
        Gson gson = new Gson();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("PROJECT_LIST");
            parseProjectsList(value);
            value = extras.getString("TOKEN");
            token = value;
            value = extras.getString("EMAIL");
            email = value;
            Log.e("Datas", email);

        }
    }

    private void parseProjectsList(String obj)
    {
        list = new ArrayList<>();
        try
        {
            JSONObject jObject = new JSONObject(obj);
            Iterator<String> keys = jObject.keys();
            while (keys.hasNext())
            {
                String key = keys.next();
                list.add(new Project(key));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
