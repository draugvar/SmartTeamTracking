package draugvar.smartteamtracking.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import draugvar.smartteamtracking.R;
import draugvar.smartteamtracking.data.Group;

public class GroupItem extends AbstractItem<GroupItem, GroupItem.ViewHolder> {
    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();
    public Group group;

    public GroupItem(Group group){
        this.group = group;
    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.group_item;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.group_item;
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
        //if(pending)
            //viewHolder.description.setText("Pending");
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
            this.name = (TextView) view.findViewById(R.id.group_name);
            this.description = (TextView) view.findViewById(R.id.group_description);
            this.num_users = (TextView) view.findViewById(R.id.num_users);
        }
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        GroupItem groupItem = (GroupItem) o;

        return group != null ? group.equals(groupItem.group) : groupItem.group == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (group != null ? group.hashCode() : 0);
        return result;
    }*/

    protected static class ItemFactory implements ViewHolderFactory<ViewHolder> {
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