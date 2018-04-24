package blih.epitools.com.mobileblih.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import blih.epitools.com.mobileblih.POJO.UserACL;
import blih.epitools.com.mobileblih.R;

public class AclViewHolder extends RecyclerView.ViewHolder {

    private TextView userName;
    private TextView userAcl;

    /**
     * @param itemView current item
     *
     * get textview to update datas on bind
     */
    public AclViewHolder(View itemView) {
        super(itemView);
        userName = (TextView) itemView.findViewById(R.id.user_name);
        userAcl = (TextView) itemView.findViewById(R.id.user_acl);
    }

    /**
     * @param acl ACL object
     *
     * Update each username and each acl
     */
    public void bind(final UserACL acl) {
        userName.setText(acl.getName());
        userAcl.setText(acl.getAcl());
    }
}