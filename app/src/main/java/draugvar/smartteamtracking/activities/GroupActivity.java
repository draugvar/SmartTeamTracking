package draugvar.smartteamtracking.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import draugvar.smartteamtracking.R;

public class GroupActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
