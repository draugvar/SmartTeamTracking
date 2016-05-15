package draugvar.smartteamtracking.data;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Myself extends RealmObject{
    private User user;
    private RealmList<Group> groups;
    private RealmList<Group> pendingGroups;

    public Myself(User user, List<Group> groups, List<Group> pendingGroups){
        this.user = user;
        this.groups = new RealmList<>();
        this.groups.addAll(groups);
        this.pendingGroups = new RealmList<>();
        this.pendingGroups.addAll(pendingGroups);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RealmList<Group> getGroups() {
        return groups;
    }

    public void setGroups(RealmList<Group> groups) {
        this.groups = groups;
    }

    public RealmList<Group> getPendingGroups() {
        return pendingGroups;
    }

    public void setPendingGroups(RealmList<Group> pendingGroups) {
        this.pendingGroups = pendingGroups;
    }
}
