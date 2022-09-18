package it.micheledallerive.covid_italia.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import it.micheledallerive.covid_italia.R;
import it.micheledallerive.covid_italia.utils.Settings;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    Switch notificationsSwitch;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false).getRoot();
        notificationsSwitch = v.findViewById(R.id.notifications_switch);
        notificationsSwitch.setChecked(Settings.areNotificationsEnabled());
        notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> Settings.toggleNotifications(getContext()));
        return v;
    }
}
