package it.micheledallerive.covid_italia.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 777;

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.e("B", "BUILDING");
        NotificationManager managerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Log.e("managernull", (managerCompat==null)+"");
        assert managerCompat != null;
        managerCompat.notify(NOTIFICATION_ID, NotificationUtils.createNotification(context, managerCompat));
    }
}