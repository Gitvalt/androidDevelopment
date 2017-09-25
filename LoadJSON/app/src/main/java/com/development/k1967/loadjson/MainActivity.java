package com.development.k1967.loadjson;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationArray = null;
        infoText = (TextView)findViewById(R.id.statusText);

        //creating map element
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);

        //fetching map marker data
        FetchDataTask task = new FetchDataTask();
        task.execute("http://student.labranet.jamk.fi/~K1967/androidCourses/dummyJSON.json");
    }


    //when map creation is complited --> generate listerners
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMaxZoomPreference(6.0f);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {

                String msg = "Marker: " + marker.getTitle() + " has been pressed!";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }


    //when loading is complited --> create and add markers to map
    private void onDataLoaded(){

        //Has information been loaded?
        if(locationArray.length() > 0)
        {
            try
            {
                for (int i = 0; i < locationArray.length(); i++) {

                    JSONObject item = locationArray.getJSONObject(i);
                    item.length();

                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(item.getDouble("Latitude"), item.getDouble("Longitude")))
                            .title(item.getString("Title"))
                    );


                }
            }
            catch (JSONException error)
            {   }
        }
        //when no locations were found or JSON load failed
        else
        {
            Toast.makeText(this, "No locations found!", Toast.LENGTH_SHORT).show();
        }

    }


    //show directions between device and marker
    private void showDirections(){

    }



public class FetchDataTask extends AsyncTask<String, Void, JSONObject> {

    private JSONArray locations;

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
        }
        catch (JSONException ex)
        {

        }
    }

    @Override
    protected JSONObject doInBackground(String... urls) {

        HttpURLConnection urlConnection = null;
        JSONObject jsonObject = null;

        try {

            URL url = new URL(urls[0]);

            //open connection to website
            urlConnection = (HttpURLConnection)url.openConnection();

            //Buffer reader to read the json from input
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            //builds the string
            StringBuilder stringBuilder = new StringBuilder();
            String Line;

            //add line break after line ends
            while((Line = reader.readLine()) != null){
                stringBuilder.append(Line).append("\n");
            }

            //end reader
            reader.close();

            jsonObject = new JSONObject(stringBuilder.toString());

        }
        catch (MalformedURLException e){

        }
        catch(IOException e){

        }
        catch (JSONException e){

        }
        finally {
            if(urlConnection != null) urlConnection.disconnect();
        }

        return jsonObject;
    }
}


public class SelfLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{
    public SelfLocation() {

        GoogleApiClient client = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();



        client.disconnect();

    }

    private void checkPermission(){
        int hasPermission = getParent().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,0,1);

        if(hasPermission != PackageManager.PERMISSION_GRANTED)
        {
            getParent().requestPermissions(getParent(),new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 1);

        }
        else
        {

        }
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

    @Override
    public void onLocationChanged(Location location) {

    }
}

private class FetchDirection extends AsyncTask<String, Void, JSONObject> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private String getSelfLocation(){

        return null;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        HttpURLConnection urlConnection = null;
        JSONObject jsonObject = null;

        try {

            String origin = ;
            String destination = strings[0];
            String APIKEY;

            URL requestURL = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" + destination + "&key=" + APIKEY);

            //open connection to website
            urlConnection = (HttpURLConnection)requestURL.openConnection();

            //Buffer reader to read the json from input
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            //builds the string
            StringBuilder stringBuilder = new StringBuilder();
            String Line;

            //add line break after line ends
            while((Line = reader.readLine()) != null){
                stringBuilder.append(Line).append("\n");
            }

            //end reader
            reader.close();

            jsonObject = new JSONObject(stringBuilder.toString());

        }
        catch (MalformedURLException e){    }
        catch(IOException e){   }
        catch (JSONException e){    }
        finally {
            if(urlConnection != null) urlConnection.disconnect();
        }

        return jsonObject;

    }
}

}


