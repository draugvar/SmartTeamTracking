package draugvar.smartteamtracking.listener;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.ExecutionException;

import draugvar.smartteamtracking.data.User;
import draugvar.smartteamtracking.rest.UpdateUserGPSCoordinates;
import draugvar.smartteamtracking.singleton.WorkflowManager;

public class CustomLocationListener implements LocationListener{
    private Activity activity;

    public CustomLocationListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onLocationChanged(Location loc) {
        Double longitude = loc.getLongitude();
        Double latitude = loc.getLatitude();
        Log.d("Location", "Longitude: " + longitude + " Latitude: " + latitude);
        boolean result = false;

        try { User user = new User();
            user.setUid(WorkflowManager.getWorkflowManager().getMyselfId());
            user.setLatGPS(latitude);
            user.setLonGPS(longitude);
            result = new UpdateUserGPSCoordinates(user).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d("Rest","Cannot update user GPS coordinates");
            e.printStackTrace();
        }
        if(!result){
            Log.d("Location","Cannot update user GPS coordinates. Returned false");
            return;
        }

        //At this point we update our GPS coordinates in the singleton
        WorkflowManager.getWorkflowManager().setMyselfGPSLatitude(latitude);
        WorkflowManager.getWorkflowManager().setMyselfGPSLongitude(longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}
}
