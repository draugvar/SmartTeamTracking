package draugvar.smartteamtracking.listener;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.util.Log;

import draugvar.smartteamtracking.data.User;
import draugvar.smartteamtracking.rest.UpdateUserGPSCoordinates;
import draugvar.smartteamtracking.singleton.WorkflowManager;

/**
 * Created by StefanoMBP on 5/16/16.
 */
public class CustomGpsStatusListener implements GpsStatus.Listener {

    @Override
    public void onGpsStatusChanged(int event) {
        if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            int satellites = 0;
            int satellitesInFix = 0;
            for (GpsSatellite sat : WorkflowManager.getWorkflowManager().getLocationManager().getGpsStatus(null).getSatellites()) {
                if (sat.usedInFix()) {
                    satellitesInFix++;
                }
                satellites++;
            }

            Log.v("Location", "Number of satellites: " + satellites + ", used In Last Fix " + satellitesInFix);

            if (satellitesInFix >= 4) {
                if (WorkflowManager.getWorkflowManager().isInside())
                    WorkflowManager.getWorkflowManager().setInside(false);
            } else if (!WorkflowManager.getWorkflowManager().isInside()) {
                WorkflowManager.getWorkflowManager().setInside(true);
                User user = new User();
                user.setUid(WorkflowManager.getWorkflowManager().getMyselfId());
                user.setLatGPS(null);
                user.setLonGPS(null);
                new UpdateUserGPSCoordinates(user).execute();
                Log.d("Location","Set GPS values to null");
            }
        }
    }
}