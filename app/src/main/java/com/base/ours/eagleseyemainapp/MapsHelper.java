package com.base.ours.eagleseyemainapp;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by USR on 4/28/2017.
 */

public class MapsHelper {

    public void displayPopup(Activity act) {

        if (ActivityCompat.checkSelfPermission(act, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationManagerCheck locationMngrCheck = new LocationManagerCheck(
                act);
        Location location = null;

        if (locationMngrCheck.isLocationServiceAvailable()) {

            if (locationMngrCheck.getProviderType() == 1)
                location = locationMngrCheck.locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            else if (locationMngrCheck.getProviderType() == 2)
                location = locationMngrCheck.locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            locationMngrCheck.createLocationServiceError(act);
        }
    }
}
