package blih.epitools.com.mobileblih;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import blih.epitools.com.mobileblih.Activities.AclActivity;


public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsViewHolder> {

    private List<String> repositories;
    private Context context;

    public ProjectsAdapter(Context _context, List<String> repos) {
        repositories = repos;
        context = _context;
    }

    @Override
    public ProjectsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_row, parent, false);
        return new ProjectsViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(final ProjectsViewHolder holder, final int position) {
        holder.bind(repositories.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AclActivity.class);
                intent.putExtra("PROJECT", repositories.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    public void updateList(List<String> list) {
        repositories = list;
        notifyDataSetChanged();
    }
}
