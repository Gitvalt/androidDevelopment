package com.development.k1967.loadjson;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.drive.events.ChangeListener;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //info textview element
    public TextView infoText;

    //locations defined in the loaded json
    private JSONArray locationArray;

    //map-element
    private GoogleMap map;

    //where the phone currently is
    private Location HomeLocation;
    private LatLng Destination;

    private String APIKEY = "AIzaSyDZ0GaJniVAIsgNwQetR1f9RHUDpmtofo0";

    private LocationCallback locationCallback;

    private int permissionTag = 123;
    private FusedLocationProviderClient providerClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationArray = null;
        Destination = null;
        infoText = (TextView) findViewById(R.id.statusText);
        providerClient = new FusedLocationProviderClient(this);

        //creating map element
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);

        //get gps location updates
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                //update home location
                HomeLocation = locationResult.getLastLocation();
                renderDirections(null, Destination);
            }
        };

        //create custom json-fetcher object and define what to do when data has been fetched
        JSONFetcher task = new JSONFetcher() {
            @Override
            protected void onPreExecute() {
                infoText.setText("Loading json...");
            }

            @Override
            protected void onPostExecute(JSONObject object) {
                try {
                    infoText.setText("Loading json has been completed!");

                    locationArray = object.getJSONArray("Locations");
                    addMarkers(locationArray);
                    SelfLocator();

                    //after we received the locations we look for phones location;

                } catch (JSONException ex) {

                }
            }
        };

        task.execute("http://student.labranet.jamk.fi/~K1967/androidCourses/dummyJSON.json");
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


    //render path from current location to destination
    private void renderDirections(LatLng startPoint, final LatLng destination){


        LatLng homeCoordinates = null;

        if(startPoint == null)
        {
            homeCoordinates = new LatLng(HomeLocation.getLatitude(), HomeLocation.getLongitude());
        }
        else if(HomeLocation == null){
            Toast.makeText(this, "Phone location unavailable", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            homeCoordinates = startPoint;
        }

        LatLng destCoordinates = destination;

        try
        {
            //Url to directions API
            URL destinationURL =
                    new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + homeCoordinates.latitude + "," + homeCoordinates.longitude +
                            "&destination=" + destCoordinates.latitude + "," + destCoordinates.longitude + "&key=" + APIKEY);


            JSONFetcher task = new JSONFetcher() {
                @Override
                protected void onPreExecute() {
                    Log.i("Destination_info", "Fetching Destination starts");
                    map.clear();
                    reloadMarkers();
                }

                @Override
                protected void onPostExecute(JSONObject object) {

                    JSONObject response = object;

                    try
                    {

                        String item  = response.getString("status");

                        //Directions could not be generated for the two markers
                        if (item.equals("ZERO_RESULTS"))
                        {
                            Log.e("Destination_ERROR", "Could not generate directions between two valid destinations");
                            infoText.setText("Directions could not be generated");
                        }

                        //origin or source is not valid
                        else if(item.equals("NOT_FOUND"))
                        {
                            Log.e("Destination_ERROR", "Destination or Starting place is not a valid address/coordinate");
                            infoText.setText("Destination or Location is not valid");
                        }

                        //fetching directions was successfull
                        else
                        {
                            Log.i("Successfull Directions", "Fetching JSON directions has succeeded");
                            infoText.setText("Directions have been generated");

                            JSONArray routes = response.getJSONArray("routes");
                            JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
                            JSONArray steps = legs.getJSONObject(0).getJSONArray("steps");


                            PolylineOptions options = new PolylineOptions();

                            for(int i = 0; i < steps.length(); i++)
                            {
                                JSONObject start = steps.getJSONObject(i).getJSONObject("start_location");
                                LatLng startLatLng = new LatLng(start.getDouble("lat"), start.getDouble("lng"));

                                JSONObject end = steps.getJSONObject(i).getJSONObject("end_location");
                                LatLng endLatLng = new LatLng(end.getDouble("lat"), end.getDouble("lng"));

                                LatLng homeLatLng = new LatLng(HomeLocation.getLatitude(), HomeLocation.getLongitude());


                                /*
                                    Do not draw a staright line through current location and destination
                                 */

                                if(startLatLng.equals(destination) && endLatLng.equals(homeLatLng))
                                {   }
                                else if(startLatLng.equals(homeLatLng) && endLatLng.equals(destination))
                                {   }
                                else
                                {
                                    options.add(startLatLng, endLatLng);
                                }
                            }

                            //options.addAll(list);
                            map.addPolyline(options);

                        }

                    }
                    catch (JSONException e)
                    {
                        Log.e("Destination_ERROR", "Fetching JSON directions has failed, Exception!");
                        Log.e("Destination_ERROR", e.getMessage());

                    }
                }
            };

            task.execute(destinationURL.toString());
        }
        catch (MalformedURLException e)
        {

        }

    }

    private void reloadMarkers()
    {
        addMarkers(locationArray);
        SelfLocator();
    }

    //when map creation is complited --> generate listerners
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMaxZoomPreference(20.0f);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                String msg = "Marker: " + marker.getTitle() + " has been pressed!";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                /*
                When marker has been clicked, render directions from phone location to destination.
                 */

                LatLng markerPosition = marker.getPosition();
                LatLng homePosition = new LatLng(HomeLocation.getLatitude(), HomeLocation.getLongitude());

                if(!markerPosition.equals(homePosition)){
                    Destination = marker.getPosition();
                    renderDirections(null, Destination);
                }
                return false;
            }
        });


        /*
        If locations have already been loaded then markers can be shown
         */

        if(locationArray != null){
            addMarkers(locationArray);
            SelfLocator();
        }

    }

    
    //when loading is completed --> create and add markers to map
    private void addMarkers(JSONArray locations)
    {

        //is map loaded?
        if(map == null)
        {
            //if map is not loaded then execute this method again when map is loaded
        }

        //map is loaded
        else
        {
            //Has information been found?
            if (locations != null) {
                try
                {
                    map.clear();

                    //read locations and create markers
                    for (int i = 0; i < locations.length(); i++) {

                        JSONObject item = locations.getJSONObject(i);
                        item.length();

                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(item.getDouble("Latitude"), item.getDouble("Longitude")))
                                .title(item.getString("Title"))
                        );
                    }


                }
                catch (JSONException error) {   }
            }

            //when no locations were found or JSON load failed
            else
            {
                Toast.makeText(this, "No locations found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //check permissions for fine_location and continue
    private void SelfLocator() {

        /*
            SelfLocator --> wait for onRequestPermissionResults -->  LoadLocation --> ReceiveSelfLocation
         */

        int checkPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        //if permission for fine_location is not granted
        if(checkPermission != PackageManager.PERMISSION_GRANTED)
        {
            android.support.v4.app.ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, permissionTag);
            //continues in onRequestPermissionResult method when user has granted permission
        }

        //if permission has been granted
        else
        {
            //get phone location
            LoadLocation();
        }
    }

    //when request permission is handled
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == permissionTag){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                LoadLocation();
            }
            else {
                Log.e("PERMISSION_FAILURE", "Permission is denied");
            }
        }
    }

    //when gps returns phone location
    private void receiveSelfLocation(Location resultLocation){
        if(resultLocation == null)
        {
            Log.e("Location_ERROR", "Self location was null");
        }
        else
        {

            HomeLocation = resultLocation;

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(resultLocation.getLatitude(), resultLocation.getLongitude()))
                    .title("Self Location")
                    .snippet("Lat: " + resultLocation.getLatitude() + " Long: " + resultLocation.getLongitude())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
            );

        }
    }

    //read location data from gps
    private void LoadLocation(){
        int checkPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        //if no permission for fine_location
        if(checkPermission != PackageManager.PERMISSION_GRANTED)
        {
            //ask for permission
            android.support.v4.app.ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, permissionTag);

        }

        //if permission is granted for fine_location
        else
        {

            //get last known location
            providerClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>()
                    {
                        //when reading last location is successfull
                        @Override
                        public void onSuccess(Location location)
                        {
                            if(location != null)
                            {
                                Log.i("Location_INFO", "Location test: " + location.getLatitude() + " ; " + location.getLongitude());

                                //send fetched location information to handler method
                                receiveSelfLocation(location);
                            }
                            else
                            {
                                Log.e("Location_ERROR", "Location was empty?");
                            }
                        }
                    })

                    //when reading last location has failed
                    .addOnFailureListener(this, new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Log.e("Location_ERROR", "Reading location has failed");
                            Log.e("Location_ERROR", e.getMessage());
                        }
                    });

        }
}

} //end of MainActivity class


