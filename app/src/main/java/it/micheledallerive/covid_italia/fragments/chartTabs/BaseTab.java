package it.micheledallerive.covid_italia.fragments.chartTabs;

import android.view.View;
import android.widget.ViewSwitcher;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButtonToggleGroup;

import it.micheledallerive.covid_italia.R;

public class BaseTab extends Fragment {

    public void setupNuoviSwitch(View v){
        final ViewSwitcher viewSwitcher = v.findViewById(R.id.view_switcher);
        View chart1 = v.findViewById(R.id.line_chart2);
        View bar = v.findViewById(R.id.bar_chart);

        MaterialButtonToggleGroup toggleGroup = v.findViewById(R.id.toggle_button);
        toggleGroup.check(R.id.unit_button);
        toggleGroup.setSingleSelection(true);
        toggleGroup.setSelectionRequired(true);
        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) ->{
            if(isChecked){
                if (checkedId == R.id.unit_button) {
                    if(viewSwitcher.getCurrentView()!=bar)
                        viewSwitcher.showNext();
                } else {
                    if(viewSwitcher.getCurrentView()!=chart1)
                        viewSwitcher.showPrevious();
                }
            }
        });
    }

}
