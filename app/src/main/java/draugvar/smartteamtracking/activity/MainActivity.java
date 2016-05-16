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

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import java.util.List;
import java.util.concurrent.ExecutionException;

import draugvar.smartteamtracking.R;
import draugvar.smartteamtracking.adapter.GroupItem;
import draugvar.smartteamtracking.adapter.PendingGroupItem;
import draugvar.smartteamtracking.data.Group;
import draugvar.smartteamtracking.data.Myself;
import draugvar.smartteamtracking.rest.AddContains;
import draugvar.smartteamtracking.rest.GetGroupsOfUsers;
import draugvar.smartteamtracking.rest.GetPendingGroupsOfUsers;
import draugvar.smartteamtracking.rest.RemovePending;
import draugvar.smartteamtracking.singleton.WorkflowManager;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    private Realm realm;
    private FastItemAdapter fastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.realm = Realm.getDefaultInstance();

        Log.d("LoginTask", "Inside onCreate of MainActivity");

        //Init fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FriendsActivity.class);
                startActivity(intent);
            }
        });

        //init our FastAdapter which will manage everything
        fastAdapter = new FastItemAdapter();

        //set our adapters to the RecyclerView
        //we wrap our FastAdapter inside the ItemAdapter -> This allows us to chain adapters for more complex useCases
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.group_recycler_view);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(fastAdapter);

        // ----- fastAdapter -- OnLongCLickListener -----
        fastAdapter.withOnLongClickListener(new FastAdapter.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v, IAdapter adapter, IItem item, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setTitle("Delete this group");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        GroupItem groupItem = (GroupItem) fastAdapter.getAdapterItem(position);
                        fastAdapter.remove(position);

                        /*  NO MORE REALM. TO BE DISCUSSED!
                        realm.beginTransaction();
                        realm.where(Group.class).equalTo("gid", groupItem.group.getGid()).findAll()
                                .deleteFirstFromRealm();
                        realm.commitTransaction();
                        */

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
        // ----- fastAdapter -- OnCLickListener ------
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener() {
            @Override
            public boolean onClick(View v, IAdapter adapter, IItem item, final int position) {
                if(item instanceof PendingGroupItem){
                    final PendingGroupItem pendingGroupItem = (PendingGroupItem) item;
                    final long uid = realm.where(Myself.class).findFirst().getUser().getUid();

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                    builder.setTitle("Do you want to join this group?");
                    builder.setMessage("Are you sure?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            GroupItem groupItem = new GroupItem(pendingGroupItem.group);
                            new AddContains(uid, pendingGroupItem.group.getGid()).execute();
                            fastAdapter.remove(position);
                            fastAdapter.add(groupItem);
                            dialog.dismiss();
                        }

                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            new RemovePending(uid, pendingGroupItem.group.getGid()).execute();
                            fastAdapter.remove(position);
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                } else if(item instanceof GroupItem) {
                    GroupItem groupItem = (GroupItem) item;
                    Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
                    intent.putExtra("gid", groupItem.group.getGid());
                    startActivity(intent);
                }
                return true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Long myselfId = WorkflowManager.getWorkflowManager().getMyselfId();
        List<Group> groupList =null;
        List<Group> groupPendingList = null;

        // update with current groups and pending groups
        try {
            groupList = new GetGroupsOfUsers(myselfId).execute().get();
            groupPendingList = new GetPendingGroupsOfUsers(myselfId).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d("Rest","MainActivity - onResume - Cannot retrieve groupList or groupPendingList ");
            e.printStackTrace();
        }

        assert groupList != null;
        for(Group group: groupList){
            GroupItem groupItem = new GroupItem(group);
            if(!fastAdapter.getAdapterItems().contains(groupItem))    //This might be too slow
                fastAdapter.add(groupItem);
        }

        assert groupPendingList != null;
        for(Group group: groupPendingList){
            PendingGroupItem groupItem = new PendingGroupItem(group);
            if(!fastAdapter.getAdapterItems().contains(groupItem))    //This might be too slow
                fastAdapter.add(0,groupItem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setBeaconManager(){
        final BeaconManager beaconManager;
        // ----- Estimote Beacon set-up ----- //
        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<com.estimote.sdk.Beacon> list) {
                /*showNotification(
                        "Your gate closes in 47 minutes.",
                        "Current security wait time is 15 minutes, "
                                + "and it's a 5 minute walk from security to the gate. "
                                + "Looks like you've got plenty of time!");*/
                for(com.estimote.sdk.Beacon beacon: list){
                    Log.d("ESTIMOTE", beacon.getProximityUUID().toString() + " " + beacon.getMajor()
                            + " " +  beacon.getMinor());
                }
            }
            @Override
            public void onExitedRegion(Region region) {
                // could add an "exit" notification too if you want (-:
            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region(
                        "monitored region",
                        null, // UUID
                        null, null)); // Major, Minor
            }
        });
    }
}
