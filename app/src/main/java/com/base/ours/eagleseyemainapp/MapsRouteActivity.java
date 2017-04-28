package com.base.ours.eagleseyemainapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.base.ours.eagleseyemainapp.POJO.Example;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.base.ours.eagleseyemainapp.ClientConnection.sendMessage;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MapsRouteActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    TextView ShowDistanceDuration;
    Polyline line;
    private GoogleApiClient mLocationClient;
    private Bundle mBundle;
    private HashMap<String, Marker> bus_markers = new HashMap<String, Marker>(); //<route\tid,lat\tlong>
    private HashMap<String, String> bus_location = new HashMap<String, String>(); //<route\tid,lat\tlong>
    private HashMap<String, Integer> bus_fullstatus = new HashMap<String, Integer>(); //<route\tid,int>
    private NearestDistanceHelper NDHelper = new NearestDistanceHelper();
    public static final int BUS_NO = 0;
    public static final int BUS_STOP_ENCODED_STR = 1;
    int minDist = Integer.MAX_VALUE;
    MapsHelper mpHelper = new MapsHelper();
    ;

    //private Marker busMarker;
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter("CUSTOM_INTENT"));
    }

    protected void putBusMarkers() {
        for (String s : bus_location.keySet()) {
            String title = s.replace("\t", " ");
            String split[] = bus_location.get(s).split("\t");
            Integer fullStatus = bus_fullstatus.get(s);
            Log.d("At line 81", fullStatus.toString());

            LatLng pos = new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
            //mMap.addMarker(new MarkerOptions().position(discPark).title("Discovery Park"));
            BitmapDescriptor icon;
            if (fullStatus < 3) {
                icon = BitmapDescriptorFactory.fromResource(R.drawable.green_bus_2);
                Log.d("Green", "Green");
            } else {
                icon = BitmapDescriptorFactory.fromResource(R.drawable.red_bus_2);
                Log.d("Red", "Red");
            }

            if (bus_markers.containsKey(s)) {
                Marker busMarker = bus_markers.get(s);
                Log.d("Inside Update", "");
                busMarker.setIcon(icon);
                busMarker.setPosition(pos);
            } else {
                Marker busMarker = mMap.addMarker(new MarkerOptions().position(pos).title(title).icon(icon));
                bus_markers.put(s, busMarker);
            }
        }
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //System.out.println("hi");
            //Toast.makeText(MapsRouteActivity.this, "hi", Toast.LENGTH_SHORT).show();

            if (intent.hasExtra("bus")) {//bus send lat and long: "bus" \t route \t ID \t Lat \t Long \t FullStatus \t CommentReset
                String msg = intent.getStringExtra("bus");
                String[] split = msg.split("\t");
                String route = split[1];
                String id = split[2];
                String lat = split[3];
                String longi = split[4];
                int fullStatus = Integer.parseInt(split[5]); //integer number
                String commentReset = split[6]; //0 or 1
                bus_location.put(route + "\t" + id, lat + "\t" + longi);
                bus_fullstatus.put(route + "\t" + id, fullStatus);

                //Toast.makeText(MapsRouteActivity.this, msg, Toast.LENGTH_SHORT).show();
                //System.out.println(msg);

                putBusMarkers();
                //mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(longi))).title("Hello world"));
            }
            if (intent.hasExtra("comment")) {//comment reply: "comment" \t Route BusID \t CommentID \t comment(UserID Comment)
                String msg = intent.getStringExtra("comment");
                Intent intent_new = new Intent(MapsRouteActivity.this, CommentsActivity.class);
                intent_new.putExtra("comment", msg);
                //Intent intent = new Intent(StartActivity.this, RouteListActivity.class);
                startActivity(intent_new);
            }
        }
    };

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (bus_markers.values().contains(marker)) {
            String routeID = marker.getTitle();

            //comments request by user: "comment request" \t Route BusID \t userID
            sendMessage("comment request\t" + marker.getTitle() + "\t" + ClientConnection.euID);

            //    Toast.makeText(MapsRouteActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(MapsRouteActivity.this, CommentsActivity.class);
            //intent.putExtra("comment","hi\thello\tabc\thello123");
            //Intent intent = new Intent(StartActivity.this, RouteListActivity.class);
            //startActivity(intent);
        }
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
        setContentView(R.layout.activity_maps_route);
        ShowDistanceDuration = (TextView) findViewById(R.id.show_distance_time);
        ShowDistanceDuration.setEnabled(false);
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

        sendMessage("register\t" + ClientConnection.euID + "\tDiscveryPark");

        mMap.setOnMarkerClickListener(this);

        // Keeping discovery park as default location
        LatLng center = new LatLng(33.234853, -97.155472/*33.234278, -97.149120*/);
        //mMap.addMarker(new MarkerOptions().position(discPark).title("Discovery Park"));
        //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.green_bus_2);
        //mMap.addMarker(new MarkerOptions().position(center).title("Discovery Park").icon(icon));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
        drawRoute();
        drawBusStops();
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
        mLocationClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .build();
        mLocationClient.connect();
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                mpHelper.displayPopup(MapsRouteActivity.this);
                return false;
            }
        });
    }

    private void drawBusStops() {
        for (BusStops BsStp : BusStops.values()) {
            mMap.addMarker(new MarkerOptions().
                    position(new LatLng(BsStp.getBsLat(), BsStp.getBsLon())).
                    title(BsStp.getName()).
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop_icon)));
        }
    }

    private void drawRoute() {
        PolylineOptions polyLine = new PolylineOptions()
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
                .width(7)
                .color(Color.DKGRAY);
        mMap.addPolyline(polyLine);
    }

    public void showNearest(View v) {
        //LatLng origin = new LatLng(33.207513, -97.152730);
        //LatLng dest = new LatLng(33.215819, -97.161374);

        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);
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

        boolean shortstDistanceSet = false;
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
        if (currentLocation == null) {
            mpHelper.displayPopup(MapsRouteActivity.this);
        } else {
            for (final BusStops BsStp : BusStops.values()) {
                LatLng origin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                LatLng dest = new LatLng(BsStp.getBsLat(), BsStp.getBsLon());
                Call<Example> call = service.getDistanceDuration("metric", origin.latitude + "," + origin.longitude, dest.latitude + "," + dest.longitude, "walking");

                call.enqueue(new Callback<Example>() {

                    @Override
                    public void onResponse(Response<Example> response, Retrofit retrofit) {
                        try {

                            //Log.d("response received",response.body().toString());
                            Log.d("Performing retrfit for:", BsStp.getName());
                            // This loop will go through all the results and add marker on each location.
                            for (int i = 0; i < response.body().getRoutes().size(); i++) {
                                String distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                                int distanceVal = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getValue();
                                String time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
                                //ShowDistanceDuration.setText("Distance:" + distance + ", Duration:" + time);
                                String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                                //Log.d("at line 450","at line 450");
                                //NDHelper.addDetls(BsStp.getSNO(),String.valueOf(distanceVal),encodedString);
                                Log.d("at line 456 min value", String.valueOf(minDist));
                                Log.d("at line 457 currValue", String.valueOf(distanceVal));

                                if (distanceVal <= minDist) {
                                    minDist = distanceVal;
                                    List<LatLng> list = decodePoly(encodedString);
                                    //Remove previous line from map
                                    if (line != null) {
                                        line.remove();
                                    }
                                    ShowDistanceDuration.setEnabled(true);
                                    ShowDistanceDuration.setText("Distance:" + distance + ", Duration:" + time);
                                    line = mMap.addPolyline(new PolylineOptions()
                                            .addAll(list)
                                            .width(5)
                                            .color(R.color.colorUntGreen)
                                            .geodesic(true)
                                    );
                                }

                            }
                        } catch (Exception e) {
                            Log.d("onResponse", "There is an error");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.d("onFailure", t.toString());
                    }
                });
            }
        }

    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void getBusSchedule(View v) {
        Intent it = new Intent(MapsRouteActivity.this, actual_tab.class);

        startActivity(it);
    }
}
