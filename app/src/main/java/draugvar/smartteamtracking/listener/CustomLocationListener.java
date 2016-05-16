package draugvar.smartteamtracking.listener;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.ExecutionException;

import draugvar.smartteamtracking.rest.UpdateUserGPSCoordinates;
import draugvar.smartteamtracking.singleton.WorkflowManager;

/**
 * Created by StefanoMBP on 5/16/16.
 */
public class CustomLocationListener implements LocationListener{
    private Activity activity;

    public CustomLocationListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onLocationChanged(Location loc) {
        double longitude = loc.getLongitude();
        double latitude = loc.getLatitude();
        Log.d("Location", "Longitude: " + longitude + " Latitude: " + latitude);
        boolean result = false;

        try {
            result = new UpdateUserGPSCoordinates(WorkflowManager.getWorkflowManager().getMyselfId(),latitude,longitude).execute().get();
        } catch (InterruptedException e) {
            Log.d("Rest","Cannot update user GPS coordinates");
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.d("Rest","Cannot update user GPS coordinates");
            e.printStackTrace();
        }
        if(result == false){
            Log.d("Location","Cannot update user GPS coordinates. Returned false");
            return;
        }

        //At this point we update our GPS coordinates in the singleton
        WorkflowManager.getWorkflowManager().setMyselfGPSLatitude(latitude);
        WorkflowManager.getWorkflowManager().setMyselfGPSLongitude(longitude);

        //Updating UI
        // WorkflowManager.getWorkflowManager().updateGroupAdapter(WorkflowManager.getWorkflowManager().getMyself());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}
}
