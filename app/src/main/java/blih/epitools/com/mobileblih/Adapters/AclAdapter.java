package blih.epitools.com.mobileblih.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import blih.epitools.com.mobileblih.Activities.AclActivity;
import blih.epitools.com.mobileblih.POJO.UserACL;
import blih.epitools.com.mobileblih.R;

public class AclAdapter extends RecyclerView.Adapter<AclViewHolder> {

    private List<UserACL> aclLists;
    private Context context;

    public AclAdapter(Context _context, List<UserACL> repos) {
        aclLists = repos;
        context = _context;
    }

    /**
     * @param parent
     * @param viewType
     * @return
     *
     * create AclViewHolder
     *
     */
    @Override
    public AclViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.acl_row, parent, false);
        return new AclViewHolder(itemView);
    }

    /**
     * @param holder
     * @param position
     *
     * bind acl list and handle click event
     */
    @Override
    public void onBindViewHolder(final AclViewHolder holder, final int position) {
        holder.bind(aclLists.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AclActivity)context).editAcl(aclLists.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return aclLists.size();
    }

    /**
     * @param list
     *
     * update acl list
     */
    public void updateList(List<UserACL> list) {
        aclLists = list;
        notifyDataSetChanged();
    }
}