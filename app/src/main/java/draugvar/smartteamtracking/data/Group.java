package draugvar.smartteamtracking.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmList;
import io.realm.RealmObject;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Group extends RealmObject {

    @JsonProperty("id")
    private long gid;
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

    public Group(long gid, String name, Double latCenter, Double lonCenter, int radius) {
        this.gid = gid;
        this.name = name;
        this.latCenter = latCenter;
        this.lonCenter = lonCenter;
        this.radius = radius;
        this.users = new RealmList<>();
    }

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
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

    public RealmList<User> getUsers(){
        return users;
    }

    public int countUsers(){
        return users.size();
    }
}
