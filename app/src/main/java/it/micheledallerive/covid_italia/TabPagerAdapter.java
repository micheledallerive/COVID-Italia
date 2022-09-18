package it.micheledallerive.covid_italia;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import it.micheledallerive.covid_italia.fragments.chartTabs.DeadTab;
import it.micheledallerive.covid_italia.fragments.chartTabs.InfectedTab;
import it.micheledallerive.covid_italia.fragments.chartTabs.RecoveredTab;

public class TabPagerAdapter extends FragmentStateAdapter {

    int tabCount;
    InfectedTab i;
    RecoveredTab r;
    DeadTab d;

    public TabPagerAdapter(FragmentActivity fa, int numberOfTabs) {
        super(fa);
        this.tabCount = numberOfTabs;
        i=new InfectedTab();
        r=new RecoveredTab();
        d=new DeadTab();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return i;
            case 1:
                return r;
            default:
                return d;
        }
    }

    @Override
    public int getItemCount() {
        return tabCount;
    }

    public void clearHighlights(){
        i.clearHighlight();
        r.clearHighlight();
        d.clearHighlight();
    }

    public void updateCharts(String s){
        try{i.updateCharts(s);}catch(Exception e){e.printStackTrace();}
        try{r.updateCharts(s);}catch(Exception e){e.printStackTrace();}
        try{d.updateCharts(s);}catch(Exception e){e.printStackTrace();}
    }

}