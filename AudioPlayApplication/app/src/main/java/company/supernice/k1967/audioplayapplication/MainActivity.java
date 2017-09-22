package company.supernice.k1967.audioplayapplication;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.Manifest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //listview containing the available songs
    private ListView listView;

    //path to the mediafiles
    private String mediaPath;

    //list of found songs
    private List<String> songs = new ArrayList<String>();

    private MediaPlayer mediaPlayer = new MediaPlayer();

    //the song loader task
    private LoadSongsTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);

        // different phones indentify sdcard differently for example: "mount" or "sdcard"...
        mediaPath = Environment.getExternalStorageDirectory().getPath() + "/Music/";

        // item listener (when listview item is pressed)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(songs.get(position));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }
                catch (IOException e) {
                    Toast.makeText(getBaseContext(), "Cannot start audio !", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //setup the task and execute it
        task = new LoadSongsTask();
        task.execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer.isPlaying()) mediaPlayer.reset();
    }

    //the song loading task
    private class LoadSongsTask extends AsyncTask<Void, String, Void> {


        private List<String> loadedSongs = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ArrayAdapter<String> songList = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, loadedSongs);

            listView.setAdapter(songList);

            songs = loadedSongs;

            Toast.makeText(getApplicationContext(), "Songs=" + songs.size(), Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... url) {
            updateSongListRecursive(new File(mediaPath));
            return null;
        }

        public void updateSongListRecursive(File path){

            if(path.isDirectory())
            {
                try
                {
                    //if no music was found
                    if(path.listFiles() == null){
                        Toast.makeText(getBaseContext(), "No music found",  Toast.LENGTH_LONG).show();
                    }
                    else
                    {

                        int fileCount = path.listFiles().length;

                        for (int i = 0; i < fileCount; i++) {
                            File file = path.listFiles()[i];
                            updateSongListRecursive(file);
                        }

                    }
                }
                catch (Exception e)
                {
                    String error = e.getMessage();
                }
            }

            //if defined mediapath is not a directory
            else
            {
                String name = path.getAbsolutePath();
                publishProgress(name);

                if(name.endsWith(".mp3"))
                {
                    loadedSongs.add(name);
                }
            }

        }
    }
}
