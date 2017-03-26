package com.base.ours.eagleseyemainapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsRouteActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_route);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.routeMap);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Keeping discovery park as default location
        LatLng center = new LatLng(33.234853, -97.155472/*33.234278, -97.149120*/);
        //mMap.addMarker(new MarkerOptions().position(discPark).title("Discovery Park"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
        drawRoute();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private void drawRoute() {
        PolylineOptions polyLine=new PolylineOptions()
                .add(new LatLng(33.251914, -97.154415))
                .add(new LatLng(33.248471, -97.147902))
                .add(new LatLng(33.248966, -97.147697))
                .add(new LatLng(33.248921, -97.146270))
                .add(new LatLng(33.247620, -97.145479))
                .add(new LatLng(33.247202, -97.145908))
                .add(new LatLng(33.254715, -97.160252))
                .add(new LatLng(33.254715, -97.160252))
                .add(new LatLng(33.220495, -97.161323))
                .add(new LatLng(33.219489, -97.162058))
                .add(new LatLng(33.216339, -97.161382))
                .add(new LatLng(33.214744, -97.161391))
                .add(new LatLng(33.214767, -97.155044))
                .add(new LatLng(33.211510, -97.155072))
                .add(new LatLng(33.211592, -97.152900))
                .add(new LatLng(33.213709, -97.152900))
                .add(new LatLng(33.213681, -97.148441))
                .add(new LatLng(33.215706, -97.148411))
                .add(new LatLng(33.215819, -97.161374))
                ;
        mMap.addPolyline(polyLine);
    }


}
