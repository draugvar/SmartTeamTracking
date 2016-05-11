package draugvar.smartteamtracking.data;

import io.realm.RealmObject;

public class Beacon extends RealmObject{
    private Long id;
    private Long beaconIdentifier; //KEY
    private String name;
    private Double latBeacon;
    private Double lonBeacon;

    public Beacon() {
        id = null;
        beaconIdentifier = null;
        name = null;
        latBeacon = null;
        lonBeacon = null;
    }

    public Beacon(Long id, Long beaconIdentifier, String name, Double latBeacon, Double lonBeacon) {
        this.id = id;
        this.beaconIdentifier = beaconIdentifier;
        this.name = name;
        this.latBeacon = latBeacon;
        this.lonBeacon = lonBeacon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
