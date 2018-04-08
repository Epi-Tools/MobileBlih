package blih.epitools.com.mobileblih.Activities;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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
import blih.epitools.com.mobileblih.CallBacks.CreateRepoCallBack;
import blih.epitools.com.mobileblih.CallBacks.ProjectsListCallBack;
import blih.epitools.com.mobileblih.POJO.UserToken;
import blih.epitools.com.mobileblih.ProjectsAdapter;
import blih.epitools.com.mobileblih.R;
import blih.epitools.com.mobileblih.POJO.User;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private List<String> list;
    private ProjectsAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    //TODO loading animation when loading projects


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        setupEvents();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getRepoList();
    }

    public void getListFromCallBack(List<String> repoList) {
        list = repoList;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.repo_list);
        adapter = new ProjectsAdapter(this, list);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    public void setupEvents() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.repo_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    adapter.updateList(getCurrentList(s));
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

        Call<ResponseBody> call = service.repoList(User.getInstance());
        call.enqueue(new ProjectsListCallBack(this));
    }


    private void createRepo(final String projectName) {
        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        Call<UserToken> call = service.createRepo(User.getInstance().getEmail(), User.getInstance().getToken(), projectName, true);
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
