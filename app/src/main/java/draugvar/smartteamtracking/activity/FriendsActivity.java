package draugvar.smartteamtracking.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import draugvar.smartteamtracking.R;
import draugvar.smartteamtracking.adapter.FriendItem;
import draugvar.smartteamtracking.data.User;

public class FriendsActivity extends AppCompatActivity {
    private EditText editText;
    private ArrayList<User> users = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText = (EditText) findViewById(R.id.edit_text_friend);

        //create our FastAdapter which will manage everything
        final FastItemAdapter<FriendItem> fastAdapter = new FastItemAdapter<>();

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
                User user = new User(0, editText.getText().toString(),"","");
                FriendItem friendItem = new FriendItem( user );

                fastAdapter.add(0, friendItem);
                recyclerView.scrollToPosition(0);
                editText.setText("");
                users.add(0, user);
            }
        });
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
            Intent intent = new Intent(this, CreateGroupActivity.class);
            intent.putExtra("users", Parcels.wrap(users));
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<String> getFriends() {
        final ArrayList<String> friends_list = new ArrayList<>();

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

                                friends_list.add(id);
                                friends_list.add(name);
                                friends_list.add(surname);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        Bundle param = new Bundle();
        param.putString("fields", "id, first_name, last_name");
        friend_request.setParameters(param);
        friend_request.executeAsync();

        return friends_list;
    }


}
