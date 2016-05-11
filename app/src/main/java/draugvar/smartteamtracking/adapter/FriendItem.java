package draugvar.smartteamtracking.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;

import draugvar.smartteamtracking.R;
import draugvar.smartteamtracking.data.User;

public class FriendItem extends AbstractItem<FriendItem, FriendItem.ViewHolder> {
    public User user;

    public FriendItem(User user){
        this.user = user;
    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return 0;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.friend_item;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder);
        final Context context = viewHolder.status.getContext();
        //bind our data
        //set the text for the name
        viewHolder.name.setText(user.getName());
        //set the text for the description or hide
        viewHolder.description.setText(user.getSurname());
        //set the text for status of parties
        viewHolder.status.setText("pending");
        viewHolder.status.setTextColor(ContextCompat.getColor(context, R.color.amber_400));
        //set the text for initials // to do elaborate!
        if(!user.getName().isEmpty())
            viewHolder.initials.setText(user.getName().substring(0,1));
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView description;
        protected TextView status;
        protected TextView initials;

        public ViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.friend_name);
            this.description = (TextView) view.findViewById(R.id.friend_description);
            this.status = (TextView) view.findViewById(R.id.friend_status);
            this.initials = (TextView) view.findViewById(R.id.initials);
        }
    }
}