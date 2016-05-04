package draugvar.smartteamtracking.data;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Group extends RealmObject {

    private long ID;
    private String name;
    private Double latCenter;
    private Double lonCenter;
    private int radius;
    private RealmList<User> users;

    public Group(){
        this.name = null;
        this.latCenter = null;
        this.lonCenter = null;
        this.users = new RealmList<>();
    }

    public Group(long ID, String name, Double latCenter, Double lonCenter, int radius) {
        this.ID = ID;
        this.name = name;
        this.latCenter = latCenter;
        this.lonCenter = lonCenter;
        this.radius = radius;
        this.users = new RealmList<>();
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatCenter() {
        return latCenter;
    }

    public void setLatCenter(Double latCenter) {
        this.latCenter = latCenter;
    }

    public Double getLonCenter() {
        return lonCenter;
    }

    public void setLonCenter(Double lonCenter) {
        this.lonCenter = lonCenter;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void addUser(User user){
        users.add(user);
    }

    public void removeUser(User user){
        users.remove(user);
    }
}
