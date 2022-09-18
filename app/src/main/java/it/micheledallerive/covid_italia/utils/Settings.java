package it.micheledallerive.covid_italia.utils;

import android.content.Context;

import it.micheledallerive.covid_italia.notification.NotificationUtils;

public class Settings {

    public static void toggleNotifications(Context c){
        boolean val = Preferences.getSharedPreferences().getBoolean(Constants.Preferences.SEND_NOTIFICATIONS, true);
        Preferences.getEditor().putBoolean(Constants.Preferences.SEND_NOTIFICATIONS, !val).commit();
        //Log.e("VAL", !val+"");
        NotificationUtils.cancelAlarm(c);
    }

    public static boolean areNotificationsEnabled(){
        return Preferences.getSharedPreferences().getBoolean(Constants.Preferences.SEND_NOTIFICATIONS, true);
    }

}
