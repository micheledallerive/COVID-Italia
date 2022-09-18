package it.micheledallerive.covid_italia.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MyService.class);
        context.startService(i);
        Toast.makeText(context, "Booting completed", Toast.LENGTH_LONG).show();
    }
}
