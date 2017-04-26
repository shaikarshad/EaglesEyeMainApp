package com.base.ours.eagleseyemainapp;

/**
 * Created by USR on 4/15/2017.
 */

public enum BusStops {
    GAB("1", "GAB", 33.213998, -97.148342),
    OAK("2", "OAK @ Ave G", 33.215769, -97.157431),
    BONNIE_BRAE_UNIV("3", "University & Bonnie Brae (NB)", 33.228976, -97.160283),
    BONNIE_BRAE_WINDSOR("4", "Bonnie Brae & Windsor (NB)", 33.239059, -97.160559),
    DISCOVERY_PARK("5", "Discovery Park East", 33.253485, -97.153917),
    HICKORY_AVE_G("6", "Ave H & Hickory", 33.214718, -97.159871),
    NT_BLVD_STELLA("7", "Stella & N Texas Blvd", 33.213874, -97.155101),
    STUDENT_REC_CENTER("8", "Student Rec Center", 33.211609, -97.153699),
    EESAT("9", "College Inn/EESAT", 33.213673, -97.151495);

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
