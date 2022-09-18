package it.micheledallerive.covid_italia.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    static SharedPreferences prefs=null;
    static SharedPreferences.Editor editor;

    public static void setup(Context c){
        prefs = Utils.getActivity(c).getPreferences(Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static SharedPreferences getSharedPreferences(){
        return prefs;
    }

    public static boolean isSettedUp(){
        return (prefs!=null);
    }

    public static SharedPreferences.Editor getEditor(){
        return editor;
    }

}
