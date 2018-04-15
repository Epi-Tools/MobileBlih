package blih.epitools.com.mobileblih.Activities;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import blih.epitools.com.mobileblih.API.BlihAPI;
import blih.epitools.com.mobileblih.CallBacks.RepoCallBack;
import blih.epitools.com.mobileblih.CallBacks.ProjectsListCallBack;
import blih.epitools.com.mobileblih.POJO.Repo;
import blih.epitools.com.mobileblih.POJO.UserToken;
import blih.epitools.com.mobileblih.Adapters.ProjectsAdapter;
import blih.epitools.com.mobileblih.R;
import blih.epitools.com.mobileblih.POJO.User;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private List<String> list;
    private ProjectsAdapter adapter;

    //TODO loading animation when loading projects


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getRepoList();
    }

    public void getListFromCallBack(List<String> repoList) {
        list = repoList;
        java.util.Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        if (adapter == null) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.repo_list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new ProjectsAdapter(this, list);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateList(list);
        }
    }

    public void getRepoList()
    {
        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        Call<ResponseBody> call = service.repoList(User.getInstance());
        call.enqueue(new ProjectsListCallBack(this));
    }


    private void createRepo(final String projectName) {
        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        Call<UserToken> call = service.createRepo(new Repo(User.getInstance().getEmail(), User.getInstance().getToken(), projectName, true));
        call.enqueue(new RepoCallBack(this));
    }

    public void deleteRepo(final String projectName) {
        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        Call<UserToken> call = service.deleteRepo(new Repo(User.getInstance().getEmail(), User.getInstance().getToken(), projectName, true));
        call.enqueue(new RepoCallBack(this));
    }


    public void createProject(View view) {
        final EditText repository = new EditText(this);
        repository.setInputType(InputType.TYPE_CLASS_TEXT);
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
                            dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //TODO refactor search view
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.gray));
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.equals(""))
                    adapter.updateList(getCurrentList(s));
                else
                    adapter.updateList(list);
                return false;
            }
        });
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
        List<String> currentList = new ArrayList();
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
