package it.micheledallerive.covid_italia.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.Calendar;

import it.micheledallerive.covid_italia.LoadingActivity;
import it.micheledallerive.covid_italia.R;

public class NotificationUtils {

    public static Notification createNotification(Context c, NotificationManager managerCompat){

        // SETUP NOTIFICATION
        NotificationCompat.Builder builder = new NotificationCompat.Builder(c, "COVIDItalia");
        builder.setContentTitle("Aggiornamento quotidiano dati");
        builder.setAutoCancel(true);
        builder.setContentText("Premi per maggiori informazioni. Le notifiche si possono disattivare dalle impostazioni.");
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Premi per maggiori informazioni. Le notifiche si possono disattivare dalle impostazioni."));
        Drawable d = ResourcesCompat.getDrawable(c.getResources(), R.drawable.ic_today, c.getTheme());
        /*Bitmap icon = Utils.drawableToBitmap(d);
        builder.setLargeIcon(icon);*/
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setSmallIcon(R.drawable.ic_today_white);
        builder.setColor(c.getColor(R.color.colorPrimary));

        // SETUP NOTIFICATION CHANNEL
        NotificationChannel channel = new NotificationChannel("COVIDItalia", "COVIDItalia", NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.setDescription("COVIDItalia notification");
        channel.setLightColor(c.getColor(R.color.colorPrimary));
        channel.setShowBadge(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        managerCompat.createNotificationChannel(channel);

        // SETUP NOTIFICATION INTENT
        Intent notifyIntent = new Intent(c, LoadingActivity.class);
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        notifyIntent.putExtra("tabToOpen", 3);
        PendingIntent pendingIntent = PendingIntent.getActivity(c, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        builder.setFullScreenIntent(pendingIntent, true);


        return builder.build();
    }

    public static void startAlarm(Context c){
        AlarmManager manager = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 15);

        myIntent = new Intent(c, MyReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(c, 777, myIntent,PendingIntent.FLAG_IMMUTABLE);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent );
    }

    public static boolean alarmExists(Context c){
        Intent myIntent = new Intent(c, MyReceiver.class);
        return PendingIntent.getBroadcast(c, 777, myIntent, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE)!=null;
    }

    public static void cancelAlarm(Context c){
        AlarmManager manager = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent=new Intent(c, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c,777,myIntent,PendingIntent.FLAG_IMMUTABLE);
        manager.cancel(pendingIntent);
        pendingIntent.cancel();
    }
}
