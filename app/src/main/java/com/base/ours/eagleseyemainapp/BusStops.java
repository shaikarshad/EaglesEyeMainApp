package com.base.ours.eagleseyemainapp;

/**
 * Created by USR on 4/15/2017.
 */

public enum BusStops {
    GAB("1", "GAB", 33.213998, -97.148342),
    DP("6", "Discovery Park East", 33.253485, -97.153917),
    SRC("12", "Student Rec Center", 33.211609, -97.153699);

    private String sno;
    private String name;
    private double bsLat;
    private double bsLon;

    BusStops(String sno, String name, double bsLat, double bsLon) {
        this.sno = sno;
        this.name = name;
        this.bsLat = bsLat;
        this.bsLon = bsLon;
    }

    public String getSNO() {
        return sno;
    }

    public String getName() {
        return name;
    }

    public double getBsLat() {
        return bsLat;
    }

    public double getBsLon() {
        return bsLon;
    }
}
