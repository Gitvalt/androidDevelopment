package com.developmet.k1967.youtubeapi;

import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import java.net.URL;

public class MainActivity extends YouTubeBaseActivity {


    private String YoutubeLink = "DnBHq5I52LM";
    private String APIKEY = "AIzaSyDZ0GaJniVAIsgNwQetR1f9RHUDpmtofo0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadVideo(YoutubeLink);
    }

    public void fetchNamesMethod(){

    }

    //Load video and display fragment
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
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(URL... urls) {
            fetchNamesMethod();
            return null;
        }
    }
}
