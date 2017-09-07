package in.msitprogram.quickmark.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import in.msitprogram.quickmark.R;

/**
 * Created by amareshjana on 07/06/17.
 */

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    private Handler handler;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        Log.e("Notification", extras.toString());
        String title = extras.getString("title");
        String content = extras.getString("message");

        //sending the msg
        sendNotification(title,content);

    }

    public void sendNotification(String title, String content) {

        // Use NotificationCompat.Builder to set up our notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        //icon appears in device notification bar and right hand corner of notification
        builder.setSmallIcon(R.drawable.msit_logo);


        // Set the intent that will fire when the user taps the notification.
//        builder.setContentIntent(pendingIntent);

        // Large icon appears on the left of the notification
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.msit_logo));

        // Content title, which appears in large type at the top of the notification
        builder.setContentTitle(title);

//        if(mAppPreference.getSoundState()){
            //need to turnoff the notification by checking with setting buttons
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
//        }


        // Content text, which appears in smaller text below the title
        builder.setContentText(content);

        // The subtext, which appears under the text on newer devices.
        // This will show-up in the devices with Android 4.2 and above only
        builder.setSubText("Notification");
        builder.setAutoCancel(true);


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Will display the notification in the notification bar
        notificationManager.notify(NOTIFICATION_ID , builder.build());
    }

}
