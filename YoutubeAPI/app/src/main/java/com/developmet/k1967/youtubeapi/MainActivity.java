package com.developmet.k1967.youtubeapi;

import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class MainActivity extends YouTubeBaseActivity {

    private String APIKEY = "AIzaSyDZ0GaJniVAIsgNwQetR1f9RHUDpmtofo0";
    private String loadedVideo = "";
    public YouTubePlayer videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

             //setup a dropdown of videos that can be watched
            final Spinner spinner = (Spinner)findViewById(R.id.spinner2);

            //list of available videos
            final String[] videos = new String[]
                    {
                      "The Worst Campaign Ad Of The 2018 Mid-Terms Has Arrived",
                      "How to Cook Spinach In Space | Video"
                    };

            //presenting videos in dropdown
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, videos);
            spinner.setAdapter(adapter);

            //when selected video has been changed
            Button button = (Button)findViewById(R.id.button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //initialization
                    if(loadedVideo == null)
                    {
                        if(videos.length > 0) {
                            loadedVideo = getVideoID(videos[0]);
                            switchVideo(loadedVideo);
                        }
                    }

                    //selection has been changed
                    else if(loadedVideo != spinner.getSelectedItem().toString())
                    {
                        loadedVideo = getVideoID(spinner.getSelectedItem().toString());
                        switchVideo(loadedVideo);
                    }

                    //if button is pressed, but selection is still the same
                    else
                    {   }
                }
            });


        //---
        //setup default video as the first in array
        loadedVideo = getVideoID(videos[0]);
        LoadVideo(loadedVideo);
    }

    //get video link
    private String getVideoID(String name){
        switch (name){
            case "The Worst Campaign Ad Of The 2018 Mid-Terms Has Arrived":
                return "blsVKk0fnkU";
            case "How to Cook Spinach In Space | Video":
                return "iGiQZIb34_s";
            default:
                return "iGiQZIb34_s";
        }
    }

    //switch video with something else
    private void switchVideo(String Url){
        videoPlayer.cueVideo(Url);
    }

    //Load video and display in fragment
    public void LoadVideo(final String Url){
        YouTubePlayerFragment youTubePlayerSupportFragment = YouTubePlayerFragment.newInstance();

        youTubePlayerSupportFragment.initialize(APIKEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo(Url);
                videoPlayer = youTubePlayer;

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.add(R.id.fragment_1, youTubePlayerSupportFragment).commit();

    }

}
