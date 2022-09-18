package it.micheledallerive.covid_italia;

import static android.view.View.INVISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import it.micheledallerive.covid_italia.fragments.ChartFragment2;
import it.micheledallerive.covid_italia.fragments.MapFragment;
import it.micheledallerive.covid_italia.fragments.NewsFragment;
import it.micheledallerive.covid_italia.fragments.SettingsFragment;
import it.micheledallerive.covid_italia.fragments.TodayFragment;
import it.micheledallerive.covid_italia.utils.OnSwipeTouchListener;
import it.micheledallerive.covid_italia.utils.Preferences;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    Context c;
    boolean animating = false;

    private void transact(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment).commit();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        transact(new MapFragment());

        c=this;

        navigationView = findViewById(R.id.bottom_navigation);

        if (getIntent().hasExtra("tabToOpen")) {
            Fragment f = null;
            if (getIntent().getIntExtra("tabToOpen", -1) == 3) {
                f = new TodayFragment();
                navigationView.setSelectedItemId(R.id.menu_today);
            }
            if (f != null)
                transact(f);
        }
        navigationView.setOnNavigationItemSelectedListener(item -> {
            if(navigationView.getSelectedItemId()==item.getItemId()) return false;
            Fragment f = null;
            if(!TodayFragment.canClick) return false;
            if (item.getItemId() == R.id.menu_news) {
                f = new NewsFragment();
            } else if (item.getItemId() == R.id.menu_chart) {
                f = new ChartFragment2();
            } else if (item.getItemId() == R.id.menu_map) {
                f = new MapFragment();
            } else if (item.getItemId() == R.id.menu_today) {
                f = new TodayFragment();
            } else {
                f = new SettingsFragment();
            }

            transact(f);
            return true;
        });

        final ImageView chiudiAvviso = findViewById(R.id.avviso_chiudi);
        final ConstraintLayout avviso = findViewById(R.id.avviso_dati);
        final TextView neverShowAgain = findViewById(R.id.never_show_again);

        //Utils.sendNotification(this);

        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);

        if (Preferences.isSettedUp() && Preferences.getSharedPreferences().getBoolean("showAvviso", true)){
            avviso.setVisibility(View.VISIBLE);
            avviso.startAnimation(slide_down);

            
            avviso.setOnTouchListener(new OnSwipeTouchListener(this){
                public void onSwipeTop() {
                    if(!animating) {
                        animating = true;
                        hide(avviso, slide_up);
                    }
                }
                public void onSwipeRight() {
                    if(!animating) {
                        animating = true;
                        Animation slide_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.exit_to_right);
                        hide(avviso, slide_right);
                    }
                }
                public void onSwipeLeft() {
                    if(!animating) {
                        animating = true;
                        Animation slide_left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.exit_to_left);
                        hide(avviso, slide_left);
                    }
                }
            });

            chiudiAvviso.setOnClickListener(v -> {
                if(!animating){
                    animating=true;
                    hide(avviso, slide_up);
                }
            });
            neverShowAgain.setOnClickListener(v->{
                if(!animating) {
                    animating = true;
                    hide(avviso, slide_up);
                    if (Preferences.isSettedUp())
                        Preferences.getEditor().putBoolean("showAvviso", false).commit();
                }
            });
        }
    }

    private void hide(View avviso, Animation anim){
        avviso.startAnimation(anim);
        avviso.postOnAnimation(()->avviso.setVisibility(INVISIBLE));
    }
}
