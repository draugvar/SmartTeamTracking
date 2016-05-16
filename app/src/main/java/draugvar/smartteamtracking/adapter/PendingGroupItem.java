package draugvar.smartteamtracking.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import draugvar.smartteamtracking.R;
import draugvar.smartteamtracking.data.Group;

public class PendingGroupItem extends AbstractItem<PendingGroupItem, PendingGroupItem.ViewHolder> {
    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();
    public Group group;

    public PendingGroupItem(Group group){
        this.group = group;
    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return 0;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.pending_group_item;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder);

        //bind our data
        //set the text for the name
        viewHolder.name.setText(group.getName());
        //set the text for the description or hide
        viewHolder.description.setText("Pending");
        //set the text for number of parties
        viewHolder.num_users.setText(group.countUsers() + "");
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView description;
        protected TextView num_users;

        public ViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.pending_group_name);
            this.description = (TextView) view.findViewById(R.id.pending_group_description);
            this.num_users = (TextView) view.findViewById(R.id.pending_num_users);
        }
    }

    protected static class ItemFactory implements ViewHolderFactory<ViewHolder>{
        @Override
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    @Override
    public ViewHolderFactory<? extends ViewHolder> getFactory() {
        return FACTORY;
    }
}
