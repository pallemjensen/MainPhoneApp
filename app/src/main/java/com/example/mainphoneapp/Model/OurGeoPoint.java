package com.example.mainphoneapp.Model;

import java.io.Serializable;

public class OurGeoPoint implements Serializable {

    private static final long serialVersionUID = -99127398709809L;

    private double latitudeE6;
    private double longitudeE6;

    public OurGeoPoint() {
        this.setLatitudeE6(0);
        this.setLongitudeE6(0);
    }

    public OurGeoPoint(double latitudeE6, double longitudeE6) {
        this.setLatitudeE6(latitudeE6);
        this.setLongitudeE6(longitudeE6);
    }

    public double getLatitudeE6() {
        return latitudeE6;
    }

    public void setLatitudeE6(double latitudeE6) {
        this.latitudeE6 = latitudeE6;
    }

    public double getLongitudeE6() {
        return longitudeE6;
    }

    public void setLongitudeE6(double longitudeE6) {
        this.longitudeE6 = longitudeE6;
    }

}