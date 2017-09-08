package com.proffesionalprogramming.k1967.loadingpictureapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

import static com.proffesionalprogramming.k1967.loadingpictureapp.R.id.imageView;
import static com.proffesionalprogramming.k1967.loadingpictureapp.R.id.progressBar;
import static com.proffesionalprogramming.k1967.loadingpictureapp.R.id.textView;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private ProgressBar progressBar;

    private final String PATH = "http://student.labranet.jamk.fi/~K1967/web-ohjelmointi/harjoitus2/tehtava4/img/";
    private String[] images = {"kuva1.png", "kuva2.jpg", "kuva3.jpg"};
    private int imageIndex;

    private DownloadImageTask task;

    private float x1, x2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get views
        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView)findViewById(R.id.textView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        //start showing images
        imageIndex = 0;
        showImage();
    }

    public void showImage(){
        task = new DownloadImageTask();
        task.execute(PATH + images[imageIndex]);
    }

    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){

            //finger is pressed to the screen
            case MotionEvent.ACTION_DOWN:
                //save the x-coordinate of the place
                x1 = event.getX();
                break;

            //finger is lifted from the screen
            case MotionEvent.ACTION_UP:
                //get the screen location
                x2 = event.getX();

                //if the first place touched was smaller (on the left side) of the last spot
                if(x1 < x2)
                {
                    //swiping from left to right => show previous picture
                    imageIndex--;
                    //if image was the first in the array, then show the last picture
                    if(imageIndex < 0) imageIndex = images.length - 1;
                }
                else
                {
                    //swiping from right to left => show the next picture
                    imageIndex++;

                    //if the current image was last of the array then show the first of array
                    if(imageIndex > (images.length - 1)) imageIndex = 0;
                }
                showImage();
                break;
        }
        return false;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        // this is done in UI thread, nothing this time
        @Override
        protected void onPreExecute() {
            // show loading progress bar
            progressBar.setVisibility(View.VISIBLE);
        }

        // this is background thread, load image and pass it to onPostExecute
        @Override
        protected Bitmap doInBackground(String... urls) {
            URL imageUrl;
            Bitmap bitmap = null;
            try {
                imageUrl = new URL(urls[0]);
                InputStream in = imageUrl.openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("<<LOADIMAGE>>", e.getMessage());
            }
            return bitmap;
        }

        // this is done in UI thread
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            textView.setText("Image " + (imageIndex + 1) + "/" + images.length);
            // hide loading progress bar
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}

