package blih.epitools.com.mobileblih.Activities;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

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
import blih.epitools.com.mobileblih.Utils.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private List<String> list;
    private ProjectsAdapter adapter;

    /**
     * @param savedInstanceState
     *
     * Initialise the main activity, orientation and
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        assignEmailToActionBar();
        Utils.showLoading(this, "Please wait...");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Utils.hideLoading();
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.hideLoading();
    }

    /**
     * Put user email below app title
     */
    private void assignEmailToActionBar() {
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.NO_GRAVITY);
        View emailView = LayoutInflater.from(this).inflate(R.layout.email_row, null);

        TextView email = (TextView) emailView.findViewById(R.id.user_email);
        email.setText(User.getInstance().getEmail());
        TextView title = (TextView) emailView.findViewById(R.id.application_name);
        title.setText(getSupportActionBar().getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(emailView, lp);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    /**
     * Initialise the list of repository
     */
    @Override
    protected void onResume() {
        super.onResume();
        getRepoList();
    }

    /**
     * @param repoList list of repositories
     * Initialise recyclerview with Blih API Data callback
     */
    public void getListFromCallBack(List<String> repoList) {
        list = repoList;
        java.util.Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        TextView noRepo = (TextView) findViewById(R.id.no_repo);
        if (list.size() == 0) {
            noRepo.setVisibility(View.VISIBLE);
        } else {
            noRepo.setVisibility(View.GONE);
        }
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

    /**
     * perform API call to get repositories list
     */
    public void getRepoList()
    {
        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);
        Call<ResponseBody> call = service.repoList(User.getInstance());
        call.enqueue(new ProjectsListCallBack(this));
    }


    /**
     * @param projectName repository to create
     *
     * perform API call to create a repository
     */
    private void createRepo(final String projectName) {
        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        Call<UserToken> call = service.createRepo(new Repo(User.getInstance().getEmail(), User.getInstance().getToken(), projectName, true));
        Utils.showLoading(this, "Create Repository...");
        call.enqueue(new RepoCallBack(this));
    }

    /**
     * @param projectName repository to delete
     *
     * perform API to delete a repository
     */
    public void deleteRepo(final String projectName) {
        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        Call<UserToken> call = service.deleteRepo(new Repo(User.getInstance().getEmail(), User.getInstance().getToken(), projectName, true));
        Utils.showLoading(this, "Delete Repository...");
        call.enqueue(new RepoCallBack(this));
    }


    /**
     * @param view Mainactivity
     *
     * create alert to create a repository
     */
    public void createProject(View view) {
        final EditText repository = new EditText(this);
        repository.setInputType(InputType.TYPE_CLASS_TEXT);
        repository.setHint("Repository Name");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Repository")
                .setView(repository)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (!repository.getText().toString().isEmpty())
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

    /**
     * @param menu main_menu
     * @return
     *
     * Handle search view to find a specific repository
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
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

    /**
     * @param item
     * @return
     *
     * Handle log out actions
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_log_out) {
            SharedPreferences.Editor editor = getSharedPreferences("credentials", Context.MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param s string to find
     * @return list of repositories found
     *
     * return a list of repository which contains the following string
     */
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

    /**
     * Move application to the background and access Android main menu
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
