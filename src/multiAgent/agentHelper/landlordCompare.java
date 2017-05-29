package multiAgent.agentHelper;

import DO.landlord;

/**
 * Created by H77 on 2017/5/16.
 */
public class landlordCompare {

    private landlord land;
    private double distance;

    public landlordCompare(landlord land, double distance) {
        this.land = land;
        this.distance = distance;
    }
    public landlord getLand() {
        return land;
    }

    public void setLand(landlord land) {
        this.land = land;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
