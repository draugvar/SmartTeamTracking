package draugvar.smartteamtracking.singleton;

import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import draugvar.smartteamtracking.data.Beacon;
import draugvar.smartteamtracking.data.Group;
import draugvar.smartteamtracking.data.Myself;
import draugvar.smartteamtracking.data.User;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class WorkflowManager {

    private static WorkflowManager instance = null;
    public static String RESULT_CLOSE_ALL = "result_close_all";

    // Data
    private Myself myself;
    private Long myselfId;
    private Double myselfGPSLatitude;
    private Double myselfGPSLongitude;
    private boolean isInside;
    private Beacon currentBeacon;

    public Beacon getCurrentBeacon() {
        return currentBeacon;
    }

    public void setCurrentBeacon(Beacon currentBeacon) {
        this.currentBeacon = currentBeacon;
    }

    public boolean isInside() {
        return isInside;
    }

    public void setInside(boolean inside) {
        isInside = inside;
    }

    //Pointers to objects
    private LocationManager locationManager;

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    private Context context;

    private WorkflowManager() {}

    public static synchronized WorkflowManager getWorkflowManager() {
        if (instance == null) {
            instance = new WorkflowManager();
            instance.myself = null;
        }
        return instance;
    }

    public Myself getMyself() {
        if(myself == null)
            myself = Realm.getDefaultInstance().where(Myself.class).findFirst();
        return myself;
    }

    public void setMyself(Myself myself) {
        this.myself = myself;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void setRealm(){
        Realm.setDefaultConfiguration( new RealmConfiguration.Builder(context).build());
    }

    public Long getMyselfId() {
        return myselfId;
    }

    public void setMyselfId(Long myselfId) {
        this.myselfId = myselfId;
    }


    public Double getMyselfGPSLatitude() {
        return myselfGPSLatitude;
    }

    public void setMyselfGPSLatitude(Double myselfGPSLatitude) {
        this.myselfGPSLatitude = myselfGPSLatitude;
    }

    public Double getMyselfGPSLongitude() {
        return myselfGPSLongitude;
    }

    public void setMyselfGPSLongitude(Double myselfGPSLongitude) {
        this.myselfGPSLongitude = myselfGPSLongitude;
    }
}
