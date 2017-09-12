package com.androidprogramming.k1967.dialogandnotification;

import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity implements customDialog.CustomDialogListener {

    //id system for notifications
    int notification_id = 1;

    private ActionMode mAction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    //when "get dialog" button is clicked
    public void DialogClick(View view){
        dialogFuntion();
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



    //when menu button is pressed
    public void menuButtonClick(View view){

        if(mAction != null){

        }

        mAction = MainActivity.this.startActionMode(mActionModeCallback);
        view.setSelected(true);
    }


    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            MenuInflater inflater = mode.getMenuInflater();

            inflater.inflate(R.menu.menulayout, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()){
                case R.id.toast_msg:
                    toastFunction();
                    mode.finish();
                    return true;
                case R.id.notification_msg:
                    notificationFunction();
                    mode.finish();
                    return true;
                case R.id.customDialog_msg:
                    dialogFuntion();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };



    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menulayout, menu);
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }



    //handle creating a new notification
    public void createNotification(int visibility, String title, String contentText, String Category){

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notification_id++;

        /*
        When using API 26 or newert

        //creating channel where notification are shown
        String channelID = "my_channel_1";

        NotificationChannel channel = new NotificationChannel(channelID, "tester", NotificationManager.IMPORTANCE_MAX);
        manager.createNotificationChannel(channel);
        */


        //creating notification
        Notification notf = new Notification.Builder(MainActivity.this)
                .setCategory(Category)
                .setContentTitle(title)
                .setContentText(contentText)
                //.setChannelId(channelID)
                .setSmallIcon(R.drawable.ptm)
                .setAutoCancel(true)
                .setVisibility(visibility).build();


        //show the notification
        manager.notify(notification_id, notf);
    }



    //when "get notification" button is clicked
    public void notfClick(View view){
        notificationFunction();
    }

    //when "get Toast" button is clicked
    public void ToastClick(View view){

        toastFunction();
    }


    private void toastFunction(){

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

    private void dialogFuntion(){

        //create and show custom dialog
        customDialog dialog = new customDialog();
        dialog.show(getFragmentManager(), "someTag");

    }

    private void notificationFunction(){
        createNotification(Notification.VISIBILITY_PUBLIC, "Moi käyttäjä", "Toimiikohan tämä?", Notification.CATEGORY_MESSAGE);

        try {

            final Runnable task = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000, 0);
                        createNotification(Notification.VISIBILITY_PUBLIC, "Thread testing...", "Should have appeared 5 seconds after first notification", Notification.CATEGORY_PROGRESS);
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
}
