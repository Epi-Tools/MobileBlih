package blih.epitools.com.mobileblih;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

class ProjectsViewHolder extends RecyclerView.ViewHolder{

    private TextView ProjectName;
    private Button deleteRepo;
    private Context context;

    // TODO add delete functionnality

    public ProjectsViewHolder(View itemView, Context context) {
        super(itemView);
        ProjectName = (TextView) itemView.findViewById(R.id.projectName);
    }

    public void bind(String text) {
        ProjectName.setText(text);
    }
}
