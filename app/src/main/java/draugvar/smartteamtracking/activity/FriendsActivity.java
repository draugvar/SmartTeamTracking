package draugvar.smartteamtracking.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import draugvar.smartteamtracking.R;
import draugvar.smartteamtracking.adapter.FriendItem;
import draugvar.smartteamtracking.data.User;
import draugvar.smartteamtracking.singleton.WorkflowManager;

public class FriendsActivity extends AppCompatActivity {
    private AutoCompleteTextView editText;
    private ArrayList<User> users = new ArrayList<User>();
    private ArrayList<String[]> friends_list;
    private Context context;
    private FastItemAdapter<FriendItem> fastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add your friends");

        context = this;

        editText = (AutoCompleteTextView) findViewById(R.id.edit_text_friend);
        assert editText != null;
        editText.setEnabled(false);

        //create our FastAdapter which will manage everything
        fastAdapter = new FastItemAdapter<>();

        //set our adapters to the RecyclerView
        //we wrap our FastAdapter inside the ItemAdapter -> This allows us to chain adapters for more complex useCases
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chosen_friends);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(fastAdapter);
        fastAdapter.withOnLongClickListener(new FastAdapter.OnLongClickListener<FriendItem>() {
            @Override
            public boolean onLongClick(View v, IAdapter<FriendItem> adapter, FriendItem item, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setTitle("Delete this friend");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        fastAdapter.remove(position);
                        users.remove(position);
                        dialog.dismiss();
                    }

                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });

        // FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] user_info = editText.getText().toString().split(" ");
                for(String[] fb_friends: friends_list){
                    if(user_info[0].equals(fb_friends[0]) && user_info[1].equals(fb_friends[1])) {
                        User user = new User();
                        user.setName(fb_friends[0]);
                        user.setSurname(fb_friends[1]);
                        user.setFacebookId(fb_friends[2]);
                        FriendItem friendItem = new FriendItem(user);

                        fastAdapter.add(0, friendItem);
                        recyclerView.scrollToPosition(0);
                        editText.setText("");
                        users.add(0, user);
                        return;
                    }
                }
                // friends not found
                Snackbar.make(view, "Credentials doesn't match to any of your friends", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        getFriends();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.accept_friends) {
            if(fastAdapter.getItemCount() > 0) {
                Intent intent = new Intent(this, CreateGroupActivity.class);
                intent.putExtra("users", Parcels.wrap(users));
                startActivity(intent);
                return true;
            } else {
                Snackbar.make(findViewById(R.id.coordinator_friends), "No friends selected", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void getFriends() {
        friends_list = new ArrayList<>();
        final LinkedList<String> friends = new LinkedList<>();

        GraphRequest friend_request = GraphRequest.newMyFriendsRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray objects, GraphResponse response) {
                        Log.i("JSONArray", objects.toString());

                        for (int i = 0; i < objects.length(); i++) {
                            try {
                                JSONObject friend = objects.getJSONObject(i);
                                String id = friend.getString("id");
                                String name = friend.getString("first_name");
                                String surname = friend.getString("last_name");
                                String[] fields = {name, surname, id};
                                friends_list.add(fields);
                                friends.add(name + " " + surname);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // Create the adapter and set it to the AutoCompleteTextView
                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, friends);
                        editText.setAdapter(adapter);
                        editText.setEnabled(true);
                    }
                });
        Bundle param = new Bundle();
        param.putString("fields", "id, first_name, last_name");
        friend_request.setParameters(param);
        friend_request.executeAsync();
    }
}
