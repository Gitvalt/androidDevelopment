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

    private ListView listView;

    private String mediaPath;

    private List<String> songs = new ArrayList<String>();

    private MediaPlayer mediaPlayer = new MediaPlayer();

    private LoadSongsTask task;

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer.isPlaying()) mediaPlayer.reset();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);
        //mediaPath = "/storage/14F2-231A/Android/media/MusicFiles";
        mediaPath = Environment.getExternalStorageDirectory().getPath() + "/Music/";

        // item listener
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

        task = new LoadSongsTask();
        task.execute();
    }

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