package org.skv.dailyenglish;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by keviny.seo on 2017. 4. 29..
 */

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;
    Intent push;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sendNotification("Wake Up! Wake Up!");
    }

    private void sendNotification(String msg) {
        Log.i("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        push = new Intent();
        push.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        push.setClass(this, MainActivity.class);
        push.putExtra("ringing", true);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                push, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Alarm").setSmallIcon(R.drawable.ic_add_alert_black_24dp)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);
        alamNotificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        alamNotificationBuilder.setFullScreenIntent(contentIntent, true);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.i("AlarmService", "Notification sent.");
    }
}
