package draugvar.smartteamtracking.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcels;

import java.util.ArrayList;

import draugvar.smartteamtracking.R;
import draugvar.smartteamtracking.data.Group;
import draugvar.smartteamtracking.data.User;
import io.realm.Realm;

public class CreateGroupActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private CircleOptions circleOptions;
    private EditText group_name;
    private Realm realm;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Group");

        realm = Realm.getDefaultInstance();

        group_name = (EditText) findViewById(R.id.chosen_group_name);

        // CircleOption init
        circleOptions = new CircleOptions()
                .radius(1) // In meters
                .strokeWidth(3)
                .strokeColor(Color.argb(40,20,20,20))
                .fillColor(Color.argb(20,20,20,20));

        // MAP init
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        // Slider stuff
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        assert seekBar != null;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mMap.clear();
                circleOptions.radius(progress*50);
                mMap.addCircle(circleOptions);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setEnabled(false);
    }

    public void centerMyLocation(View view) {
        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(new Criteria(), true));
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Get back the mutable Circle
            mMap.clear();
            circleOptions.center(new LatLng(latitude, longitude));
            mMap.addCircle(circleOptions);
        }
        seekBar.setEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            Location location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(new Criteria(), true));
            if(location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12));
            }
        } else {
            // Show rationale and request permission.
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.accept_group) {
            String g_name = group_name.getText().toString();
            realm.beginTransaction();
            Group mGroup = realm.createObject(Group.class);
            mGroup.setName(g_name);
            mGroup.setGid(0);
            ArrayList<User> users = Parcels.unwrap(getIntent().getParcelableExtra("users"));
            for(User u: users){
                mGroup.addUser(u);
            }
            realm.commitTransaction();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}