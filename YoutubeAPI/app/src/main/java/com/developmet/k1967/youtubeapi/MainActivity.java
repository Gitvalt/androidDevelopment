package com.developmet.k1967.youtubeapi;

import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class MainActivity extends YouTubeBaseActivity {


    private String YoutubeLink = "DnBHq5I52LM";
    private String APIKEY = "AIzaSyDZ0GaJniVAIsgNwQetR1f9RHUDpmtofo0";
    private URL youtubeSearchLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchNames name = new fetchNames();

        LoadVideo(YoutubeLink);
    }

    //Load video and display in fragment
    public void LoadVideo(final String Url){
        YouTubePlayerFragment youTubePlayerSupportFragment = YouTubePlayerFragment.newInstance();

        youTubePlayerSupportFragment.initialize(APIKEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo(Url);

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_1, youTubePlayerSupportFragment).commit();
    }

    private class fetchNames extends AsyncTask<URL, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        }

        public String[] fetchNamesMethod(String searchParam){
            try
            {
                youtubeSearchLink = new URL("https://www.googleapis.com/youtube/v3/search");

                HttpURLConnection connection = (HttpURLConnection) youtubeSearchLink.openConnection();

                connection.addRequestProperty("part","snippet");
                connection.addRequestProperty("q",searchParam);
                connection.addRequestProperty("type","video");
                connection.addRequestProperty("maxResults","30");

                connection.connect();

                StringBuilder builder = new StringBuilder();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line+"\n");
                }

                reader.close();

                connection.disconnect();


            }
            catch (IOException ex)
            {

            }
        }

        @Override
        protected String doInBackground(URL... urls) {
            String[] item = fetchNamesMethod("YTP");
            return item;
        }
    }
}
