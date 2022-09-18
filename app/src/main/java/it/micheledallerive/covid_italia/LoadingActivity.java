package it.micheledallerive.covid_italia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import it.micheledallerive.covid_italia.data.NationalData;
import it.micheledallerive.covid_italia.data.RegionData;
import it.micheledallerive.covid_italia.notification.NotificationUtils;
import it.micheledallerive.covid_italia.objects.Error;
import it.micheledallerive.covid_italia.utils.Constants;
import it.micheledallerive.covid_italia.utils.Preferences;
import it.micheledallerive.covid_italia.utils.Utils;

public class LoadingActivity extends AppCompatActivity {

    private static boolean alreadyStarted=false;
    private final Timer timer = new Timer();

    private void startErrorActivity(Context c, Error error){
        //Log.e("ERROR", error.toString());
        Intent i = new Intent(c, ErrorActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("error", error.getMessage());
        startActivity(i);
        finish();
}

    private void setupNotification(Context c){
        //Log.e("EXISTS", NotificationUtils.alarmExists(c)+"");
        NotificationUtils.cancelAlarm(c);// TODO ELIMINA
        //Log.e("EXISTS", NotificationUtils.alarmExists(c)+"");
        //Log.e("NOTIFICATION", "SETTING UP NOTIFICATIONS");
        //if(!NotificationUtils.alarmExists(c))
        NotificationUtils.startAlarm(c);
        //Log.e("EXISTS", NotificationUtils.alarmExists(c)+"");
    }

    private void setupPreferences(Context c){
        Preferences.setup(c);
    }

    private void setupConsigli(TextView consigli){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    int randomNum = (int) (Math.random() * ((Constants.CONSIGLI.length - 1) + 1));
                    String consiglio = Constants.CONSIGLI[randomNum];
                    consigli.setText(consiglio);
                });
            }
        },0, 7500);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if(!alreadyStarted){alreadyStarted=true;}
        final Context c = this;
        setContentView(R.layout.activity_loading);
        //Log.e("Update", "Started loading activity");

        setupConsigli(findViewById(R.id.consigli));

        if(savedInstanceState==null){
            setupPreferences(c);
            if(Utils.isFirstTime() || !NotificationUtils.alarmExists(this))
                setupNotification(c);
            //Utils.sendNotification(c);
            NationalData.getData(false,new Callback() {
                @Override
                public void onSuccess(Object obj) {
                    //Log.e("Update", "Success 1");
                    RegionData.getData(false, new Callback() {
                        @Override
                        public void onSuccess(Object obj) {
                            timer.cancel();
                            //Log.e("Update", "Loading activity callback triggered");
                            Intent i = new Intent(c, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            if(getIntent().getExtras()!=null)
                                i.putExtras(getIntent().getExtras());
                            startActivity(i);
                            finish();
                            // INITIALIZE ADS
//                            MobileAds.initialize(c, new OnInitializationCompleteListener() {
//                                @Override
//                                public void onInitializationComplete(InitializationStatus initializationStatus) {
//                                }
//                            });
                        }

                        @Override
                        public void onError(Error error) {
                            startErrorActivity(c, error);
                        }
                    });
                }

                @Override
                public void onError(Error error) {
                    startErrorActivity(c, error);
                }
            });
        }
    }
}
