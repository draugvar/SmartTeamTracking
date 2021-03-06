package draugvar.smartteamtracking.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.parceler.Parcel;

import io.realm.RealmObject;

@JsonIgnoreProperties(ignoreUnknown = true)
@Parcel
public class Beacon extends RealmObject{
    String uuid;
    Integer major;
    Integer minor;
    Long beaconIdentifier; //KEY
    String name;
    Double latBeacon;
    Double lonBeacon;

    public Beacon() {
        uuid = null;
        major = null;
        minor = null;
        beaconIdentifier = null;
        name = null;
        latBeacon = null;
        lonBeacon = null;
    }

    public Beacon(String uuid, Integer major, Integer minor, Long beaconIdentifier, String name,
                  Double latBeacon, Double lonBeacon) {
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.beaconIdentifier = beaconIdentifier;
        this.name = name;
        this.latBeacon = latBeacon;
        this.lonBeacon = lonBeacon;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getMajor() {
        return major;
    }

    public void setMajor(Integer major) {
        this.major = major;
    }

    public Integer getMinor() {
        return minor;
    }

    public void setMinor(Integer minor) {
        this.minor = minor;
    }

    public Long getBeaconIdentifier() {
        return beaconIdentifier;
    }

    public void setBeaconIdentifier(Long beaconIdentifier) {
        this.beaconIdentifier = beaconIdentifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatBeacon() {
        return latBeacon;
    }

    public void setLatBeacon(Double latBeacon) {
        this.latBeacon = latBeacon;
    }

    public Double getLonBeacon() {
        return lonBeacon;
    }

    public void setLonBeacon(Double lonBeacon) {
        this.lonBeacon = lonBeacon;
    }

    @Override
    public String toString() {
        return "Beacon{" +
                "uuid='" + uuid + '\'' +
                ", major=" + major +
                ", minor=" + minor +
                ", beaconIdentifier=" + beaconIdentifier +
                ", name='" + name + '\'' +
                ", latBeacon=" + latBeacon +
                ", lonBeacon=" + lonBeacon +
                '}';
    }
}
