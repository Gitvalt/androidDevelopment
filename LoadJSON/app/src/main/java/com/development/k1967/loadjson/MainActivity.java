package com.development.k1967.loadjson;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoText = (TextView)findViewById(R.id.statusText);

        FetchDataTask task = new FetchDataTask();
        task.execute("http://student.labranet.jamk.fi/~K1967/androidCourses/dummyJSON.json");
    }




public class FetchDataTask extends AsyncTask<String, Void, JSONObject> {

    private JSONArray locations;

    @Override
    protected void onPreExecute() {
        infoText.setText("Loading json...");
    }

    @Override
    protected void onPostExecute(JSONObject object) {
        infoText.setText("Loading json has been completed!");

        try
        {
            JSONArray locations = object.getJSONArray("Locations");

            for(int i = 0; i < locations.length(); i++){

                JSONObject item = locations.getJSONObject(i);

            }

            int tester = object.length();

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

}


