package model;

/**
 * Created by Kavin on 17/08/17.
 */

public class Pharmacy {

    private String name, id, distance;

    public Pharmacy(String name, String id, String distance) {
        this.name = name;
        this.id = id;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getDistance() {
        return distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
