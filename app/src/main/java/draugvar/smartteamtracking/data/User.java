package draugvar.smartteamtracking.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import io.realm.RealmList;
import io.realm.RealmObject;

@Parcel
@JsonIgnoreProperties(ignoreUnknown = true)

public class User extends RealmObject {

    @JsonProperty("id")
    long uid;
    String name;
    String surname;
    String email;
    Double latGPS;
    Double lonGPS;
    String authToken;
    String facebookId;
    Beacon beacon;

    public User(){
        this.name = null;
        this.surname = null;
        this.email = null;
        this.latGPS = null;
        this.lonGPS = null;
        this.authToken = null;
        this.facebookId = null;
    }

    public User(long uid, String name, String surname, String email, Double latGPS,
                Double lonGPS, String authToken, String facebookId) {
        this.uid = uid;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.latGPS = latGPS;
        this.lonGPS = lonGPS;
        this.authToken = authToken;
        this.facebookId = facebookId;
    }

    public User(long uid, String name, String surname, String email) {
        this.uid = uid;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
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

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", latGPS=" + latGPS +
                ", lonGPS=" + lonGPS +
                ", authToken='" + authToken + '\'' +
                ", facebookId='" + facebookId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return uid == user.uid;

    }

    @Override
    public int hashCode() {
        return (int) (uid ^ (uid >>> 32));
    }


}
