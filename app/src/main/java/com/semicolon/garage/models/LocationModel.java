package com.semicolon.garage.models;

import java.io.Serializable;

/**
 * Created by elashry on 15/10/2018.
 */

public class LocationModel implements Serializable {
    private double lat;
    private double lng;

    public LocationModel(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
