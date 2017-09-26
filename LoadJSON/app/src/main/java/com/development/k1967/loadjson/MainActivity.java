package com.development.k1967.loadjson;

import android.*;
import android.Manifest;
import android.app.Activity;
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
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //info textview element
    public TextView infoText;

    private int permissionTag = 123;

    //locations defined in the loaded json
    private JSONArray locationArray;

    //map-element
    private GoogleMap map;

    private FusedLocationProviderClient providerClient;

    //where the phone currently is
    private Location HomeLocation;

    private String APIKEY = "AIzaSyDZ0GaJniVAIsgNwQetR1f9RHUDpmtofo0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationArray = null;
        infoText = (TextView) findViewById(R.id.statusText);

        //creating map element
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);


        providerClient = LocationServices.getFusedLocationProviderClient(this);

        providerClient.requestLocationUpdates(new LocationRequest())


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
                    onDataLoaded();


                    //Log.d("Locator", "Locator start!");
                    SelfLocator();


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


    private void renderDirections(LatLng destination){

        LatLng homeCoordinates = new LatLng(HomeLocation.getLatitude(), HomeLocation.getLongitude());
        LatLng destCoordinates = destination;

        try
        {
            //Url to directions API
            URL destinationURL =
                    new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + HomeLocation.getLatitude() + "," + HomeLocation.getLongitude() +
                            "&destination=" + destCoordinates.latitude + "," + destCoordinates.longitude + "&key=" + APIKEY);


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
                        //Directions could not be generated for the two markers
                        if (response.get("status") == "ZERO_RESULTS")
                        {
                            Log.e("Destination_ERROR", "Could not generate directions between two valid destinations");
                        }

                        //origin or source is not valid
                        else if(response.getString("status") == "NOT_FOUND")
                        {
                            Log.e("Destination_ERROR", "Destination or Starting place is not a valid address/coordinate");
                        }

                        //fetching directions was successfull
                        else
                        {

                        }

                    }
                    catch (JSONException e)
                    {
                        Log.e("Destination_ERROR", "Fetching JSON directions has failed");
                    }
                }
            };

            task.execute(destinationURL.toString());
        }
        catch (MalformedURLException e)
        {

        }

    }

    //when map creation is complited --> generate listerners
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMaxZoomPreference(10.0f);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                String msg = "Marker: " + marker.getTitle() + " has been pressed!";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                renderDirections(marker.getPosition());
                return false;
            }
        });
    }

    //when loading is completed --> create and add markers to map
    private void onDataLoaded() {

        //Has information been loaded?
        if (locationArray.length() > 0) {
            try
            {
                //read locations and create markers
                for (int i = 0; i < locationArray.length(); i++) {

                    JSONObject item = locationArray.getJSONObject(i);
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
        else {
            Toast.makeText(this, "No locations found!", Toast.LENGTH_SHORT).show();
        }

    }

    //check permissions for fine_location and continue
    private void SelfLocator() {

        int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        //if permission for fine_location is not granted
        if(checkPermission != PackageManager.PERMISSION_GRANTED)
        {
            //ask for permission
            android.support.v4.app.ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permissionTag);

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
        int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        //if no permission for fine_location
        if(checkPermission != PackageManager.PERMISSION_GRANTED)
        {
            //ask for permission
            android.support.v4.app.ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permissionTag);

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
                                //send fetched location information to method
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


