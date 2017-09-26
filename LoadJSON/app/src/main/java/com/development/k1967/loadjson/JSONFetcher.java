package com.development.k1967.loadjson;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by K1967 on 26.9.2017.
 *
 * Fetch JSON-data from location
 *
 */

public class JSONFetcher extends AsyncTask<String, Void, JSONObject> {

    private JSONArray fetchedArray;

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onPostExecute(JSONObject object) {}

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

