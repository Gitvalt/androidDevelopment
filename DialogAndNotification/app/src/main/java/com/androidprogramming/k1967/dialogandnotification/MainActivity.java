package com.androidprogramming.k1967.dialogandnotification;

import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity implements customDialog.CustomDialogListener {

    //id system for notifications
    int notification_id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //when "get dialog" button is clicked
    public void DialogClick(View view){

        //create and show custom dialog
        customDialog dialog = new customDialog();
        dialog.show(getFragmentManager(), "someTag");

    }

    //listens for the custom dialogs responses!
        @Override
        public void onDialogPositiveClick(boolean response) {
            Toast.makeText(getApplicationContext(), ":)", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDialogNegativeClick(boolean response) {
            Toast.makeText(getApplicationContext(), ":(", Toast.LENGTH_SHORT).show();
        }
    //---

    //handle creating a new notification
    public void createNotification(int visibility, String title, String contentText){

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notification_id++;

        //creating channel where notification are shown
        String channelID = "my_channel_1";

        NotificationChannel channel = new NotificationChannel(channelID, "tester", NotificationManager.IMPORTANCE_MAX);
        manager.createNotificationChannel(channel);

        //creating notification
        Notification notf = new Notification.Builder(MainActivity.this)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setContentTitle(title)
                .setContentText(contentText)
                .setChannelId(channelID)
                .setSmallIcon(R.drawable.ptm)
                .setAutoCancel(true)
                .setVisibility(visibility).build();


        //show the notification
        manager.notify(notification_id, notf);
    }

    //when "get notification" button is clicked
    public void notfClick(View view){

        createNotification(Notification.VISIBILITY_PUBLIC, "Moi käyttäjä", "Toimiikohan tämä?");

        try {

            final Runnable task = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000, 0);
                        createNotification(Notification.VISIBILITY_PUBLIC, "Thread testing...", "Should have appeared 5 seconds after first notification");
                    }
                    catch (Exception ex){
                        String msg = ex.getMessage();
                    }
                }
            };

            Thread thread = new Thread(task);
            thread.start();

        } catch (Exception ex){
            String msg = ex.getMessage();
        }

    }

    //when "get Toast" button is clicked
    public void ToastClick(View view){

        Random rand = new Random();
        int n = rand.nextInt(4);
        String msg;

        //every time "Give Toast" -button is pressed a random message is shown to user

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

        //show the message
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
