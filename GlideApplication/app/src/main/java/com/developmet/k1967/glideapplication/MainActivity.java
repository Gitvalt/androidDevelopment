package com.developmet.k1967.glideapplication;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.*;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.Transition;

import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get picture element from UI
        ImageView picture = (ImageView)findViewById(R.id.imageViewer);

        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.placeholder_human);


        //load image from url and set it into picture-element
        Glide.with(this)
                .load("http://student.labranet.jamk.fi/~K1967/androidCourses/1")
                .apply(options)
                .into(picture);
        
    }

    //when fetch button is pressed
    public void fetchImage(View view){

        //get ui elements
        TextView urlString = (TextView)findViewById(R.id.urlInput);
        Button fetchButton = (Button)findViewById(R.id.confirmButton);

        //check if user has defined a url as input
        try {
            URL input = new URL(urlString.getText().toString());

            //on success set button textcolor to green and load the image
            fetchButton.setTextColor(Color.GREEN);

            Glide.with(this)
                    .load(urlString.getText().toString())
                    .into((ImageView)findViewById(R.id.imageViewer));
        }
        //if url was not valid
        catch (MalformedURLException urlE){
            //set button text color to RED
            fetchButton.setTextColor(Color.RED);
        }
    }
}
