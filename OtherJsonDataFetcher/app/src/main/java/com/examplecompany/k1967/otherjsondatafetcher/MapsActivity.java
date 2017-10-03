package com.examplecompany.k1967.otherjsondatafetcher;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //Google map element
    private GoogleMap mMap;

    private JSONArray golfCourses;
    private static final String jsonLocation = "http://student.labranet.jamk.fi/~K1967/androidCourses/golf_courses.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        golfCourses = null;
        FetchJsonFromURL retriever = new FetchJsonFromURL(){
            @Override
            protected void onPreExecute() {
                Log.i("Fetching Json", "We start looking for json-data from " + jsonLocation);
            }

            @Override
            protected void onPostExecute(JSONObject object) {
                try
                {
                    Log.i("Fetching Json", "Fetching completed!");
                    golfCourses = object.getJSONArray("courses");
                    drawMarkersToMap(golfCourses);
                }
                catch (JSONException error)
                {
                    Log.e("Fetching Json", "Parsing results has failed");
                }
            }
        };

        retriever.execute(jsonLocation);
    }



    private void drawMarkersToMap(JSONArray locations)
    {
        if(locations == null)
        {
            Log.e("Drawing error", "No locations defined!");
            return;
        }

        try
        {
            for (int i = 0; i < locations.length(); i++) {

                JSONObject underInspection = locations.getJSONObject(i);

                String snippet = underInspection.getString("address") + "\n"
                        + underInspection.getString("phone") + "\n"
                        + underInspection.getString("email") + "\n"
                        + underInspection.getString("web");

                BitmapDescriptor definedIcon;

                switch (underInspection.getString("type")){
                    case "Kulta":
                        definedIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                        break;
                    case "Kulta/Etu":
                        definedIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                        break;
                    case "?":
                        definedIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
                        break;
                    default:
                        definedIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                        break;
                }

                Marker mark  = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(underInspection.getDouble("lat"), underInspection.getDouble("lng")))
                                .title(underInspection.getString("course"))
                                .snippet(snippet)
                        .icon(definedIcon)
                );

            }
        }
        catch (JSONException excp)
        {

        }
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

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);

                //setup title
                TextView title = new TextView(getApplicationContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                //setup snippet
                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
    }
}
