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
import com.google.android.gms.drive.events.ChangeListener;
import com.google.android.gms.location.FusedLocationProviderApi;
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
import com.google.android.gms.maps.model.Polyline;
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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //info TextView element
    public TextView infoText;


    //locations fetched from student labranet json-file
    private JSONArray locationArray;
    private static final String jsonLocation = "http://student.labranet.jamk.fi/~K1967/androidCourses/dummyJSON.json";

    //map-element
    private GoogleMap map;

    private GoogleApiClient mGoogleApiClient;

    //where the phone currently is
    private Location HomeLocation;

    //Current directions destination
    private LatLng Destination;

    //currently rendered directions
    private Polyline currentPath;
    private PolylineOptions currentPathOptions;

    //google apikey for directions and map
    private static final String APIKEY = "AIzaSyDZ0GaJniVAIsgNwQetR1f9RHUDpmtofo0";
    private static final int permissionTag = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationArray = null;
        Destination = null;
        infoText = (TextView) findViewById(R.id.statusText);

        //creating map element
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

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

        //execute created task
        task.execute(jsonLocation);
    }

    //when Main Activity is stated
    @Override
    protected void onStart() {
        super.onStart();

        //start google api client
        mGoogleApiClient.connect();

    }

    //when Main Activity is stopped
    @Override
    protected void onStop()
    {
        super.onStop();

        //disconnect google api client
        if(mGoogleApiClient.isConnected()) mGoogleApiClient.disconnect();

        //stop location requests
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    //...Google API Methods ...
    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        Log.i("Google_API", "Connection to API client successful");

        //if connection to Google API client has been created, start listening for phones current location
        if(HomeLocation == null)
        {
            LoadLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { Log.e("Google_API", "Connecting to google API client has failed"); }
    //...Google API Methods ...


    public void onReloadButtonPressed(View view){
        reFetchLocations();
    }

    //reload locations from json-location
    private void reFetchLocations()
    {
        Log.i("JSON_LOADER", "Fetching JSON data from '" + jsonLocation + "'");

        //create task for fetching the json data
        JSONFetcher task = new JSONFetcher() {
            @Override
            protected void onPreExecute() {
                infoText.setText("Loading json...");
            }

            @Override
            protected void onPostExecute(JSONObject object) {
                try
                {
                    infoText.setText("Loading json has been completed!");
                    locationArray = object.getJSONArray("Locations");
                    reloadMarkers();
                }
                catch (JSONException ex)
                {

                }
            }
        };

        //execute created task
        task.execute(jsonLocation);
    }

    //render path from current location to destination. startPoint can be used to render path between two separate location (not phones current location)
    private void renderDirections(LatLng startPoint, final LatLng destination){

        //get coordinates from Location variable
        LatLng homeCoordinates = null;

        if(startPoint == null)
        {
            homeCoordinates = new LatLng(HomeLocation.getLatitude(), HomeLocation.getLongitude());
        }
        else if(HomeLocation == null){
            Toast.makeText(this, "Phone's current location is unavailable", Toast.LENGTH_SHORT).show();
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

            //task to fetch directions
            JSONFetcher task = new JSONFetcher() {
                @Override
                protected void onPreExecute() {
                    Log.i("Destination_info", "Fetching Destination starts");
                }

                @Override
                protected void onPostExecute(JSONObject object) {

                    JSONObject response = object;

                    try
                    {
                        //remove currently rendered path for the new one
                        if(currentPath != null) {
                            currentPath.remove();
                        }

                        //get direction generation status
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
                            Log.i("Successful Directions", "Fetching JSON directions has succeeded");
                            infoText.setText("Directions have been generated");

                            //from the response fetch directions steps
                            JSONArray routes = response.getJSONArray("routes");
                            JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
                            JSONArray steps = legs.getJSONObject(0).getJSONArray("steps");


                            currentPathOptions = new PolylineOptions();

                            //go through steps and add them to PolylineOptions
                            for(int i = 0; i < steps.length(); i++)
                            {
                                JSONObject start = steps.getJSONObject(i).getJSONObject("start_location");
                                LatLng startLatLng = new LatLng(start.getDouble("lat"), start.getDouble("lng"));

                                JSONObject end = steps.getJSONObject(i).getJSONObject("end_location");
                                LatLng endLatLng = new LatLng(end.getDouble("lat"), end.getDouble("lng"));

                                LatLng homeLatLng = new LatLng(HomeLocation.getLatitude(), HomeLocation.getLongitude());

                                currentPathOptions.add(startLatLng, endLatLng);
                            }

                            //add the created polyline to the map
                            currentPath = map.addPolyline(currentPathOptions);
                        }

                    }
                    catch (JSONException e)
                    {
                        Log.e("Destination_ERROR", "Fetching JSON directions has failed, Exception!");
                        Log.e("Destination_ERROR", e.getMessage());

                    }
                }
            };

            //execute task
            task.execute(destinationURL.toString());
        }
        catch (MalformedURLException e) {   }
    }


    //reload the existing markers to the map
    private void reloadMarkers()
    {
        //clear existing markers
        map.clear();

        //add loaded location markers
        addMarkers(locationArray);

        //create homelocation marker
        if(HomeLocation != null){
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(HomeLocation.getLatitude(), HomeLocation.getLongitude()))
                    .title("Self Location")
                    .snippet("Lat: " + HomeLocation.getLatitude() + " Long: " + HomeLocation.getLongitude())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
            );

        }
    }


    //when map creation is completed --> generate listeners
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMaxZoomPreference(20.0f);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                String msg = "Marker: " + marker.getTitle() + " has been pressed!";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                // When marker has been clicked, render directions from phone location to destination.

                LatLng markerPosition = marker.getPosition();
                LatLng homePosition = new LatLng(HomeLocation.getLatitude(), HomeLocation.getLongitude());

                if(!markerPosition.equals(homePosition)){
                    Destination = marker.getPosition();
                    renderDirections(null, Destination);
                }
                else
                {
                    infoText.setText("This is where you should be.");
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

        //is map loaded? If map is not loaded then execute this method again when map is loaded.
        if(map == null) {  return;  }

        //map is loaded
        else
        {
            //Has location information been found?
            if (locations != null)
            {
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
                                .snippet(item.getString("Description"))
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

    //check permissions for fine_location and continue to LoadLocation
    private void SelfLocator() {

        //SelfLocator --> wait for onRequestPermissionResults -->  LoadLocation

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
            LoadLocation();
        }
    }

    //when request permission is handled
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode) {
            case permissionTag:

                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    LoadLocation();
                }
                else {
                    Log.e("PERMISSION_FAILURE", "Permission is denied by user");
                }

            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }



    //start reading location from gps and setup initial home marker
    private void LoadLocation()
    {

            LocationRequest request = LocationRequest.create();
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            request.setInterval(1000);

            int hasLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            if(mGoogleApiClient.isConnected()) {
                if (hasLocationPermission == PackageManager.PERMISSION_GRANTED) {

                    //set listerner for location
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, this);

                    //fetch and show current location
                    HomeLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                    /*
                            .addOnSuccessListener(this, new OnSuccessListener<Location>()
                            {
                                //when reading last location is successful
                                @Override
                                public void onSuccess(Location location)
                                {
                                    //set initial homelocaiton
                                    HomeLocation = location;

                                    //add home marker
                                    map.addMarker(new MarkerOptions()
                                            .position(new LatLng(HomeLocation.getLatitude(), HomeLocation.getLongitude()))
                                            .title("Self Location")
                                            .snippet("Lat: " + HomeLocation.getLatitude() + " Long: " + HomeLocation.getLongitude())
                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                                    );
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
                */
                }
            }
    }

    //when phone has been moved
    @Override
    public void onLocationChanged(Location location) {
        HomeLocation = location;
        reloadMarkers();

    }

}


