package com.androidprogramming.k1967.dialogandnotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    int notification_id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //(Button)notf_Button = (Button)findViewById(R.id.notificationButton);

    }

    public void DialogClick(View view){
        ExitDialogFrag fraf = new ExitDialogFrag();
        fraf.show(getFragmentManager(), "exit");
    }

    public void createNotification(int visibility, String text){

        String channelID = "my_channel_1";

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notification_id++;

        NotificationChannel channel = new NotificationChannel(channelID, "tester", NotificationManager.IMPORTANCE_MAX);
        manager.createNotificationChannel(channel);


        Notification notf = new Notification.Builder(MainActivity.this)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setContentTitle(text)
                .setContentText(text)
                .setChannelId(channelID)
                .setSmallIcon(R.drawable.ptm)
                .setAutoCancel(true)
                .setVisibility(visibility).build();


        manager.notify(notification_id, notf);



    }

    public void notfClick(View view){
        createNotification(Notification.VISIBILITY_PUBLIC, "moi!");
    }

    public void ToastClick(View view){

        Random rand = new Random();
        int n = rand.nextInt(4);
        String msg = "";

        switch (n){
            case 0:
                msg = "Hello user";
                break;
            case 1:
                msg = "Nice day we are having";
                break;
            case 2:
                msg = "There are limited number of possible responses";
                break;
            case 3:
                msg = "Cake is a lie";
                break;
            case 4:
                msg = "101100101 01100101 0111001";
                break;
            default:
                msg = "Message is empty";
                break;
        }

        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
