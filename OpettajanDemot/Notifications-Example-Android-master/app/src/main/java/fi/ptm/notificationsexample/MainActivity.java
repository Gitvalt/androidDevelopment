package fi.ptm.notificationsexample;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

/**
 * Created by PTM on 22/04/15.
 */
public class MainActivity extends Activity {

    private int notification_id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchPublicNotification(View view) {
        createNotification(Notification.VISIBILITY_PUBLIC, getString(R.string.public_text));
    }

    public void launchPrivateNotification(View view) {
        createNotification(Notification.VISIBILITY_PRIVATE, getString(R.string.private_text));
    }

    public void launchSecretNotification(View view) {
        createNotification(Notification.VISIBILITY_SECRET, getString(R.string.secret_text));
    }

    public void launchHeadsUpNotification(View view) {
        // create pending intent to launch web site
        Intent actionIntent = new Intent(Intent.ACTION_VIEW);
        actionIntent.setData(Uri.parse("http://pasimanninen.com"));
        PendingIntent actionPendingIntent = PendingIntent.getActivity(this, 0, actionIntent, 0);

        // create action intent to open application in device (ResultActivity)
        Intent contentIntent = new Intent(this,ResultActivity.class);
        // Adds the back stack - see manifest
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // adds the Intent to the top of the stack
        stackBuilder.addParentStack(ResultActivity.class);
        // adds the Intent to the top of the stack
        stackBuilder.addNextIntent(contentIntent);
        // gets a PendingIntent containing the entire back stack
        PendingIntent contentPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // create a notification
        Notification notification = new Notification.Builder(this)
                .setCategory(Notification.CATEGORY_PROMO)
                .setContentTitle("PTM notification")
                .setContentText("Something cool happens now on PTM.")
                .setSmallIcon(R.drawable.ptm)
                .setAutoCancel(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .addAction(android.R.drawable.ic_menu_view, "View details", actionPendingIntent)
                .setContentIntent(contentPendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).build(); // wait, vibrate, wait, ...
        // cancel the notification after it is launched
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // connect notification manager and create unique notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification_id++;
        notificationManager.notify(notification_id, notification);
    }

    public void launchBigViewNotification(View view) {
        // create action intent to open application in device (SpecialActivity)
        Intent specialIntent = new Intent(this,SpecialActivity.class);
        specialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // just launch a special activity (not a full stack of activities)
        PendingIntent specialPendingIntent = PendingIntent.getActivity(this, 0, specialIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // create a new notification
        Notification notification  = new Notification.Builder(this)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setContentTitle("PTM notification")
                .setContentText("Something cool happens now on PTM.")
                .setStyle(new Notification.BigTextStyle().bigText("Big Android seminar today. Remember register at PTM website! Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged."))
                .setSmallIcon(R.drawable.ptm)
                .setContentIntent(specialPendingIntent)
                .setAutoCancel(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC).build();
        // connect notification manager and create a notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification_id++;
        notificationManager.notify(notification_id, notification);
    }

    public void createNotification(int visibility, String text) {
        // create a new notification
        Notification notification  = new Notification.Builder(this)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setContentTitle("PTM notification")
                .setContentText(text)
                .setSmallIcon(R.drawable.ptm)
                .setAutoCancel(true)
                .setVisibility(visibility).build();
        // connect notification manager
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // make a new notification with a new unique id
        notification_id++;
        notificationManager.notify(notification_id, notification);
    }

}
