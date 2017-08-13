package org.skv.dailyenglish;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by keviny.seo on 2017. 4. 29..
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {

    private static Ringtone ringtone;
    NotificationManager nm;
    Intent push;
    PendingIntent fullScreenPendingIntent;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i("AlarmReceiver", "onReceive Alarm!!");
        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        if (!ringtone.isPlaying())
            ringtone.play();

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

        //this will update the UI with message
        // MainActivity inst = MainActivity.instance();
        // if (inst != null) {
        //     inst.dialogSimple();
        // }
    }

    public static void stopRinging() {
        ringtone.stop();
    }
}
