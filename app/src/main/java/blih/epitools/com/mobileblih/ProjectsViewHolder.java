package blih.epitools.com.mobileblih;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;

import blih.epitools.com.mobileblih.Activities.MainActivity;

class ProjectsViewHolder extends RecyclerView.ViewHolder{

    private TextView ProjectName;
    private ImageButton deleteRepo;
    private Context context;

    // TODO add delete functionnality

    public ProjectsViewHolder(View itemView, Context _context) {
        super(itemView);
        ProjectName = (TextView) itemView.findViewById(R.id.projectName);
        deleteRepo = (ImageButton) itemView.findViewById(R.id.delete_repo);
        context = _context;
    }

    public void bind(final String repoName) {
        ProjectName.setText(repoName);
        deleteRepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setTitle("Delete Repository")
                        .setMessage("Do you really want to delete " + repoName)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MainActivity)context).deleteRepo(repoName);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .show();
            }
        });
    }
}
