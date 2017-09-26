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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    //info textview element
    public TextView infoText;

    //locations defined in the loaded json
    private JSONArray locationArray;

    //map-element
    private GoogleMap map;


    private FusedLocationProviderClient providerClient;
    private Activity source;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationArray = null;
        infoText = (TextView) findViewById(R.id.statusText);

        //creating map element
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);


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

                    Log.d("Locator", "Locator start!");
                    SelfLocator();

                    locationArray = object.getJSONArray("fetchedArray");
                    onDataLoaded();
                } catch (JSONException ex) {

                }
            }
        };


        task.execute("http://student.labranet.jamk.fi/~K1967/androidCourses/dummyJSON.json");
    }

    //when map creation is complited --> generate listerners
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMaxZoomPreference(6.0f);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                String msg = "Marker: " + marker.getTitle() + " has been pressed!";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    //when loading is complited --> create and add markers to map
    private void onDataLoaded() {

        //Has information been loaded?
        if (locationArray.length() > 0) {
            try {
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

    public void SelfLocator() {

        source = this;

        //get location provider
        providerClient = LocationServices.getFusedLocationProviderClient(source);

        int checkPermission = ContextCompat.checkSelfPermission(source, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(checkPermission != PackageManager.PERMISSION_GRANTED)
        {
            //ask permission
            android.support.v4.app.ActivityCompat.requestPermissions(source, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 666);
        }
        else {
            LoadLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 666){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                LoadLocation();
            }
            else {
                Log.w("TAGGER", "Permission is denied");
            }
        }
    }

    private void gotLocation(Location result){
        Location item = result;
    }

    private void LoadLocation(){
        int checkPermission = ContextCompat.checkSelfPermission(source, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(checkPermission != PackageManager.PERMISSION_GRANTED)
        {
            //ask permission
            android.support.v4.app.ActivityCompat.requestPermissions(source, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 666);

        }
        else
        {
            providerClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){

                                double i = location.getLatitude();
                                double y = location.getLongitude();
                                Log.w("TAGGER", "Location test: " + i + " " + y);
                                gotLocation(location);
                            }
                            else
                            {
                                Log.w("TAGGER", "No location read");
                            }
                        }
                    });

        }

    }


} //end of activity class


