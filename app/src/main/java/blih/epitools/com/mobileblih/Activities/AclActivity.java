package blih.epitools.com.mobileblih.Activities;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.List;
import java.util.Objects;

import blih.epitools.com.mobileblih.API.BlihAPI;
import blih.epitools.com.mobileblih.CallBacks.AclListCallBack;
import blih.epitools.com.mobileblih.CallBacks.RepoCallBack;
import blih.epitools.com.mobileblih.POJO.Repo;
import blih.epitools.com.mobileblih.POJO.RepoACL;
import blih.epitools.com.mobileblih.POJO.RepoAclUpdate;
import blih.epitools.com.mobileblih.POJO.User;
import blih.epitools.com.mobileblih.POJO.UserACL;
import blih.epitools.com.mobileblih.POJO.UserToken;
import blih.epitools.com.mobileblih.R;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class AclActivity extends AppCompatActivity {

    private String currentProjet;
    private List<UserACL> list;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.acl_layout);
        currentProjet = getIntent().getStringExtra("PROJECT");
        getSupportActionBar().setTitle(currentProjet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAclList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_acl, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_delete) {
            alertDeleteRepo();
        }
        return super.onOptionsItemSelected(item);
    }

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

    public void deleteRepo(final String projectName) {
        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        Call<UserToken> call = service.deleteRepo(new Repo(User.getInstance().getEmail(), User.getInstance().getToken(), projectName, true));
        call.enqueue(new RepoCallBack(this));
    }

    public void getAclList() {
        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        Call<ResponseBody> call = service.aclList(new RepoACL(User.getInstance().getEmail(), User.getInstance().getToken(), currentProjet));
        call.enqueue(new AclListCallBack(this));
    }

    public void updateAcl(String userName, String acl) {
        BlihAPI service = BlihAPI.retrofit.create(BlihAPI.class);

        Call<ResponseBody> call = service.updateAcl(new RepoAclUpdate(User.getInstance().getEmail(), User.getInstance().getToken(), userName, currentProjet, acl));
        call.enqueue(new AclListCallBack(this));
    }

    public void getAclListFromCallBack(List<UserACL> repoList) {
        list = repoList;
      /*  if (adapter == null) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.repo_list);
            adapter = new ProjectsAdapter(this, list);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateList(list);
        }*/
    }

    public void setUserAcl(View view) {
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.acl_creator, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add ACL").setView(dialogView);

        final EditText username = (EditText) dialogView.findViewById(R.id.username_acl);

        final String[] aclSelected = {"r"};

        spinner = (Spinner) dialogView.findViewById(R.id.spinner_acl_create);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.acls_creator, android.R.layout.simple_spinner_item);
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
                if (!Objects.equals(username.getText().toString(), ""))
                    updateAcl(username.getText().toString(), aclSelected[0]);
                else
                    Snackbar.make(findViewById(R.id.acl_view),
                            "Please enter the username.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        }).show();
    }

    // TODO refactor with adapter
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

}