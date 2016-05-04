package draugvar.smartteamtracking.data;

import io.realm.RealmObject;

public class User extends RealmObject {

    private long ID;
    private String name;
    private String surname;
    private String email;
    private String pw;
    private Double latGPS;
    private Double lonGPS;

    public User(){
        this.name = null;
        this.surname = null;
        this.email = null;
        this.pw = null;
        this.latGPS = null;
        this.lonGPS = null;
    }

    public User(long ID, String name, String surname, String email, String pw, Double latGPS, Double lonGPS) {
        this.ID = ID;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.pw = pw;
        this.latGPS = latGPS;
        this.lonGPS = lonGPS;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public Double getLatGPS() {
        return latGPS;
    }

    public void setLatGPS(Double latGPS) {
        this.latGPS = latGPS;
    }

    public Double getLonGPS() {
        return lonGPS;
    }

    public void setLonGPS(Double lonGPS) {
        this.lonGPS = lonGPS;
    }
}
