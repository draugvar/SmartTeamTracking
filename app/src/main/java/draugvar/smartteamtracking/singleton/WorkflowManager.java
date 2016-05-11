package draugvar.smartteamtracking.singleton;

import android.content.Context;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import draugvar.smartteamtracking.data.Group;
import draugvar.smartteamtracking.data.User;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class WorkflowManager {

    private static WorkflowManager instance = null;

    // Data
    private User myself;
    private List<User> userList = new LinkedList<User>(); // FRIENDS LIST
    private List<Group> groupList = new LinkedList<Group>(); // SHOULD MAKE LIST THREAD SAFE

    private Context context;

    private WorkflowManager() {}

    public static synchronized WorkflowManager getWorkflowManager() {
        if (instance == null) {
            instance = new WorkflowManager();
        }
        return instance;
    }

    public User getMyself() {
        return myself;
    }

    public void setMyself(User myself) {
        this.myself = myself;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void setRealm(){
        Realm.setDefaultConfiguration( new RealmConfiguration.Builder(context).build());
    }
}
