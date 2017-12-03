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
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Project> list;
    private ProjectViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDatas();
        setupEvents();
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
