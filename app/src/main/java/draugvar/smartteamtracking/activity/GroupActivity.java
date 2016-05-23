package draugvar.smartteamtracking.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import org.parceler.Parcels;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import draugvar.smartteamtracking.R;
import draugvar.smartteamtracking.adapter.GroupMemberItem;
import draugvar.smartteamtracking.data.Group;
import draugvar.smartteamtracking.data.User;
import draugvar.smartteamtracking.service.UpdateGroup;
import draugvar.smartteamtracking.singleton.WorkflowManager;

public class GroupActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static GoogleMap mMap;
    private static FastItemAdapter<GroupMemberItem> fastAdapter;
    private Group group;
    private Messenger messenger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert getSupportActionBar() != null;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        group = Parcels.unwrap(getIntent().getParcelableExtra("group"));
        getSupportActionBar().setTitle(group.getName());

        // MAP init
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.group_map);
        mapFragment.getMapAsync(this);

        //create our FastAdapter which will manage everything
        fastAdapter = new FastItemAdapter<>();
        setFastAdapter();

        GroupHandler groupHandler = new GroupHandler();
        messenger = new Messenger(groupHandler);
    }

    private void setFastAdapter() {
        // set our adapters to the RecyclerView
        // we wrap our FastAdapter inside the ItemAdapter -> This allows us to chain adapters for more complex useCases
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.group_friends);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(fastAdapter);

        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<GroupMemberItem>() {
            @Override
            public boolean onClick(View v, IAdapter<GroupMemberItem> adapter, GroupMemberItem item, int position) {
                LatLng latlng;
                if(item.user.getBeacon() == null && item.user.getLatGPS() != null
                        && item.user.getLonGPS()!= null) {
                    latlng = new LatLng(item.user.getLatGPS(), item.user.getLonGPS());
                    mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(latlng, 17));
                } else if(item.user.getBeacon() != null){
                    latlng = new LatLng(item.user.getBeacon().getLatBeacon(),
                                        item.user.getBeacon().getLonBeacon());
                    mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(latlng, 17));
                }
                return true;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(false);
            if(        WorkflowManager.getWorkflowManager().getMyselfGPSLatitude()  != null
                    && WorkflowManager.getWorkflowManager().getMyselfGPSLongitude() !=null){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(WorkflowManager.getWorkflowManager().getMyselfGPSLatitude(),
                                   WorkflowManager.getWorkflowManager().getMyselfGPSLongitude()), 12));
            } else {

            }
        } else {
            // Show rationale and request permission.
            Log.d("CreateGroupActivity", "No permissions");
        }
        LatLng latLng;
        for(GroupMemberItem groupMemberItem: fastAdapter.getAdapterItems()){
            if(groupMemberItem.user.getBeacon() == null && groupMemberItem.user.getLatGPS() != null
                    && groupMemberItem.user.getLonGPS()!=null) {
                latLng = new LatLng(groupMemberItem.user.getLatGPS(), groupMemberItem.user.getLonGPS());
                mMap.addMarker(new MarkerOptions().position(latLng).title(groupMemberItem.user.getName()));
            } else if(groupMemberItem.user.getBeacon() != null) {
                latLng = new LatLng(groupMemberItem.user.getBeacon().getLatBeacon(),
                                    groupMemberItem.user.getBeacon().getLonBeacon());
                mMap.addMarker(new MarkerOptions().position(latLng).title(groupMemberItem.user.getName()));
            }
            //All is null
        }

        //Starting IntentService for updating UI
        Intent intent = new Intent(this,UpdateGroup.class);
        intent.putExtra("gid", group.getGid());
        intent.putExtra("messenger", this.messenger);
        startService(intent);
    }


    @Override
    protected void onStop() {
        super.onStop();
        UpdateGroup.shouldContinue = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateGroup.shouldContinue = true;
    }

    private static class GroupHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            Set<User> userSet = Parcels.unwrap(bundle.getParcelable("userSet"));
            Log.d("Interactive", "Unwrapped userSet in message: " + userSet.toString());
            List<GroupMemberItem> groupMemberItemList = new LinkedList<>();
            for(User user : userSet){
                groupMemberItemList.add( new GroupMemberItem(user) );
            }
            fastAdapter.set(groupMemberItemList);
            //Move stuff inside the map
            mMap.clear();
            LatLng latLng;
            for(GroupMemberItem groupMemberItem: fastAdapter.getAdapterItems()){
                if(groupMemberItem.user.getBeacon() == null && groupMemberItem.user.getLatGPS() != null
                        && groupMemberItem.user.getLonGPS()!=null) {
                    latLng = new LatLng(groupMemberItem.user.getLatGPS(), groupMemberItem.user.getLonGPS());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(groupMemberItem.user.getName()));
                } else if(groupMemberItem.user.getBeacon() != null) {
                    latLng = new LatLng(groupMemberItem.user.getBeacon().getLatBeacon(),
                            groupMemberItem.user.getBeacon().getLonBeacon());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(groupMemberItem.user.getName()));
                }
                //All is null
            }
        }
    }
}
