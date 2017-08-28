package com.example.valtteri.launchmap;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //show the map
    public void showMap(View view){

        //get latitude and longitude edittext-elements
        EditText lat = (EditText) findViewById(R.id.Latitude);
        EditText lng = (EditText) findViewById(R.id.Longitude);

        //get inputs as strings
        String lat1 = lat.getText().toString();
        String lng1 = lng.getText().toString();

        //convert to doubles
        double lat2 = Double.parseDouble(lat1);
        double lng2 = Double.parseDouble(lng1);

        //show the map
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:" + lat2 + "," + lng2));
        startActivity(intent);

    }

}
