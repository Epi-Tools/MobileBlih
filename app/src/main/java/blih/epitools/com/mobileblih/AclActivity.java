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
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

public class AclActivity extends AppCompatActivity {

    Project currentProjet;

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