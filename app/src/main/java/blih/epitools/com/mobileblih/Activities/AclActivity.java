package blih.epitools.com.mobileblih.Activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import java.util.List;

import blih.epitools.com.mobileblih.API.BlihAPI;
import blih.epitools.com.mobileblih.Adapters.AclAdapter;
import blih.epitools.com.mobileblih.CallBacks.AclListCallBack;
import blih.epitools.com.mobileblih.CallBacks.AclUpdateCallBack;
import blih.epitools.com.mobileblih.CallBacks.RepoCallBack;
import blih.epitools.com.mobileblih.POJO.Repo;
import blih.epitools.com.mobileblih.POJO.RepoACL;
import blih.epitools.com.mobileblih.POJO.RepoAclUpdate;
import blih.epitools.com.mobileblih.POJO.User;
import blih.epitools.com.mobileblih.POJO.UserACL;
import blih.epitools.com.mobileblih.POJO.UserToken;
import blih.epitools.com.mobileblih.R;
import blih.epitools.com.mobileblih.Utils.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class AclActivity extends AppCompatActivity {

    private String currentProjet;
    private AclAdapter adapter;
    private String aclSelected;

    /**
     * @param savedInstanceState Intent
     *
     * Get selected project name and write on the supportActionBar
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.acl_layout);
        currentProjet = getIntent().getStringExtra("PROJECT");
        getSupportActionBar().setTitle(currentProjet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Utils.showLoading(this, "Please wait...");
    }

    /**
     * Get acl list on onResume
     */
    @Override
    protected void onResume() {
        super.onResume();
        getAclList();
    }

    /**
     * @param menu acl menu
     * @return
     *
     * load menu acl
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_acl, menu);
        return true;
    }


    /**
     * @param item back button on Support action bar or delete button
     * @return
     *
     * Actions for home back button and delete button.
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_delete) {
            alertDeleteRepo();
        } else if (id == R.id.copy_url_repo) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Repository URL", "git@git.epitech.eu:/" + User.getInstance().getEmail() + "/" + currentProjet);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "The repository URL of " + currentProjet + " has been copied. You can now share it to your team.",
                    Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Alert when delete Button from ActionBar is clicked
     */
    private void alertDeleteRepo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Delete Repository")
                .setMessage("Do you really want to delete " + currentProjet)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteRepo(currentProjet);
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
     * @param projectName Name of the deleted project
     *  API call to delete a repository
     */
    public void deleteRepo(final String projectName) {
        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        Call<UserToken> call = service.deleteRepo(new Repo(User.getInstance().getEmail(), User.getInstance().getToken(), projectName, true));
        Utils.showLoading(this, "Delete Repository...");
        call.enqueue(new RepoCallBack(this));
    }

    /**
     * API call to receive the acl List from the current repository
     */
    public void getAclList() {
        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        Call<ResponseBody> call = service.aclList(new RepoACL(User.getInstance().getEmail(), User.getInstance().getToken(), currentProjet));
        call.enqueue(new AclListCallBack(this));
    }

    /**
     * @param userName User login of the user acl
     * @param acl acl chosen (r or rw)
     *
     * API call to add, change or delete ACL of a user
     */
    public void updateAcl(String userName, String acl) {
        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        Call<UserToken> call = service.updateAcl(new RepoAclUpdate(User.getInstance().getEmail(), User.getInstance().getToken(), userName, currentProjet, acl));
        Utils.showLoading(this, "Update acl...");
        call.enqueue(new AclUpdateCallBack(this));
    }

    /**
     * @param repoList ACL list
     *
     * Callback from the acl List API call
     */
    public void getAclListFromCallBack(List<UserACL> repoList) {
        List<UserACL> list = repoList;
        TextView noACL = (TextView) findViewById(R.id.no_acl);
        if (list.size() == 0) {
            noACL.setVisibility(View.VISIBLE);
        } else {
            noACL.setVisibility(View.GONE);
        }
        if (adapter == null) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.acl_list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new AclAdapter(this, list);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateList(list);
        }
    }

    /**
     * @param view Current View
     *
     * When click on Floating action button, show a dialog to write user login and select ACL to add
     */
    public void setUserAcl(View view) {
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.acl_creator, null);
        final EditText username = (EditText) dialogView.findViewById(R.id.username_acl);

        spinnerHandler(dialogView, R.id.spinner_acl_create, R.array.acls_creator);
        aclBuilder(dialogView, username.getText().toString(), "Add ACL");
    }

    /**
     * @param user
     *
     *  When clicking on the specific user from recycler view, edit the acl from the selected user
     */
    public void editAcl(UserACL user) {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_layout, null);
        TextView username = (TextView) dialogView.findViewById(R.id.username_acl);
        username.setText(user.getName());

        spinnerHandler(dialogView, R.id.spinner_acl_edit, R.array.acls_editor);
        aclBuilder(dialogView, username.getText().toString(), "Edit ACL");
    }

    /**
     * @param dialogView View when clicking on recycler view item or FAB
     * @param id spinner id
     * @param array editor array or create array
     *
     * Manage spinner for dialog
     */
    public void spinnerHandler(View dialogView, int id, int array) {
        Spinner spinner = (Spinner) dialogView.findViewById(id);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aclSelected = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * @param dialogView View when clicking on recycler view item or FAB
     * @param username user login
     * @param title title for alert
     *
     * Create acl dialog for edit or create ACL
     */
    private void aclBuilder(final View dialogView, final String username, final String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title).setView(dialogView);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (aclSelected.equals("None"))
                    aclSelected = "";
                if (title.contains("Add ACL")) {
                    final EditText user = (EditText) dialogView.findViewById(R.id.username_acl);
                    updateAcl(user.getText().toString(), aclSelected);
                } else {
                    updateAcl(username, aclSelected);
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        }).show();
    }


}
