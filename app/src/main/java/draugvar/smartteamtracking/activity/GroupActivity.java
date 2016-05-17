package draugvar.smartteamtracking.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import java.util.Set;
import java.util.concurrent.ExecutionException;

import draugvar.smartteamtracking.R;
import draugvar.smartteamtracking.adapter.FriendItem;
import draugvar.smartteamtracking.data.Group;
import draugvar.smartteamtracking.data.User;
import draugvar.smartteamtracking.rest.GetUsers;
import io.realm.Realm;

public class GroupActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String g_name = getIntent().getExtras().getString("group_name", "Group");
        getSupportActionBar().setTitle(g_name);

        Long gid = getIntent().getExtras().getLong("gid", 0);

        // MAP init
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.group_map);
        mapFragment.getMapAsync(this);

        //create our FastAdapter which will manage everything
        FastItemAdapter fastAdapter = new FastItemAdapter();

        //set our adapters to the RecyclerView
        //we wrap our FastAdapter inside the ItemAdapter -> This allows us to chain adapters for more complex useCases
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.group_friends);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(fastAdapter);

        try {
            Set<User> users = new GetUsers().execute(gid).get();
            for(User user: users) {
                FriendItem friendItem = new FriendItem(user);
                fastAdapter.add(friendItem);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //NEEDS TO BE POPULATED WITH REST CALLS!
        /*Realm realm = Realm.getDefaultInstance();
        long gid = getIntent().getLongExtra("gid", 0);
        Group group = realm.where(Group.class).equalTo("gid", gid).findFirst();
        for(User user: group.getUsers()){
            fastAdapter.add(new FriendItem(user));
        }*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
