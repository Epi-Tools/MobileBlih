package blih.epitools.com.mobileblih.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import blih.epitools.com.mobileblih.API.BlihAPI;
import blih.epitools.com.mobileblih.AclActivity;
import blih.epitools.com.mobileblih.CallBacks.CreateRepoCallBack;
import blih.epitools.com.mobileblih.CallBacks.ProjectsListCallBack;
import blih.epitools.com.mobileblih.ListViewAdapter;
import blih.epitools.com.mobileblih.POJO.Token;
import blih.epitools.com.mobileblih.R;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> list;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String token;
    String email;

    //TODO loading animation when loading projects


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        token = getIntent().getStringExtra("TOKEN");
        email = getIntent().getStringExtra("EMAIL");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setupEvents();
        getRepoList();
    }

    public void setupEvents() {
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        //adapter = new ListViewAdapter(list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ListViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Intent intent = new Intent(MainActivity.this, AclActivity.class);
     //           intent.putExtra("PROJECT", gson.toJson(adapter.getItemAt(position)));
                intent.putExtra("TOKEN", token);
                intent.putExtra("EMAIL", email);
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
                getRepoList();
            }
        });
    }

    public void getRepoList()
    {
        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        Call<Token> call = service.repoList(email, token);
        call.enqueue(new ProjectsListCallBack(this));
    }


    private void createRepo(final String projectName) {
        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        Call<Token> call = service.createRepo(email, token, projectName, true);
        call.enqueue(new CreateRepoCallBack(this));
    }


    public void createProject(View view) {
        final EditText repository = new EditText(this);
        repository.setHint("Repository Name");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Repository")
                .setView(repository)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //TODO handle text error
                        createRepo(repository.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // TODO just cancel the action, find something useful
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


        return super.onOptionsItemSelected(item);
    }

    private List<String> getCurrentList(String s)
    {
        ArrayList currentList = new ArrayList();
        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).toLowerCase().contains(s.toLowerCase()))
                currentList.add(list.get(i));
        }
        return currentList;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
    }

}
