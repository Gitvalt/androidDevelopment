package com.development.k1967.loadjson;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

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
        task.execute("www.google.com");
    }




public class FetchDataTask extends AsyncTask<String, Void, String> {
    @Override
    protected void onPreExecute() {
        infoText.setText("Loading json...");
    }

    @Override
    protected void onPostExecute(String s) {
        infoText.setText("Loading json has been completed!");
    }

    @Override
    protected String doInBackground(String... urls) {

        HttpURLConnection urlConnection = null;

        try {

            URL url = new URL(urls[0]);

            urlConnection = (HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));



        }
        catch (MalformedURLException e){

        }
        catch(IOException e){

        }

        return null;
    }
}

}


