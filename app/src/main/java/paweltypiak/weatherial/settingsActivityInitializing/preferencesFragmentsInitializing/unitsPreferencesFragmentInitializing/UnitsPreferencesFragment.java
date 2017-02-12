/*******************************************************************************
 * Copyright 2017 Paweł Typiak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package paweltypiak.weatherial.settingsActivityInitializing.preferencesFragmentsInitializing.unitsPreferencesFragmentInitializing;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.settingsActivityInitializing.dialogPreferencesInitializing.unitsDialogPreferences.PressureUnitsDialogPreference;
import paweltypiak.weatherial.settingsActivityInitializing.dialogPreferencesInitializing.unitsDialogPreferences.SpeedUnitsDialogPreference;
import paweltypiak.weatherial.settingsActivityInitializing.dialogPreferencesInitializing.unitsDialogPreferences.TemperatureUnitsDialogPreference;
import paweltypiak.weatherial.settingsActivityInitializing.dialogPreferencesInitializing.unitsDialogPreferences.TimeUnitsDialogPreference;
import paweltypiak.weatherial.utils.UsefulFunctions;

public class UnitsPreferencesFragment extends PreferenceFragment {

    private OnUnitsPreferenceFragmentInsertionListener fragmentInsertionListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getOnUnitsPreferenceFragmentInsertionListener(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getOnUnitsPreferenceFragmentInsertionListener(activity);
        }
    }

    private void getOnUnitsPreferenceFragmentInsertionListener(Context context){
        if (context instanceof OnUnitsPreferenceFragmentInsertionListener) {
            fragmentInsertionListener = (OnUnitsPreferenceFragmentInsertionListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement fragmentInsertionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        addPreferencesFromResource(R.xml.preferences_units);
        updateActionBarTitle();
    }

    private void updateActionBarTitle(){
        fragmentInsertionListener.updateActionBarTitle(getString(R.string.preferences_units_title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setListViewPadding(view);
        setPreferencesIcons();
        return view;
    }

    private void setListViewPadding(View view){
        if(view != null) {
            ListView lv = (ListView) view.findViewById(android.R.id.list);
            lv.setPadding(0, 0, 0, 0);
        }
    }

    private void setPreferencesIcons(){
        setTemperaturePreferenceIcon();
        setSpeedPreferenceIcon();
        setPressurePreferenceIcon();
        setTimePreferenceIcon();
    }

    private void setTemperaturePreferenceIcon(){
        TemperatureUnitsDialogPreference preference
                = (TemperatureUnitsDialogPreference) findPreference(getString(R.string.preferences_temperature_unit_key));
        Drawable icon = UsefulFunctions.getColoredDrawable(
                getActivity(),
                R.drawable.temperature_preferences_icon,
                ContextCompat.getColor(getActivity(),
                        R.color.colorPrimary)
        );
        preference.setIcon(icon);
    }

    private void setSpeedPreferenceIcon(){
        SpeedUnitsDialogPreference preference
                = (SpeedUnitsDialogPreference) findPreference(getString(R.string.preferences_speed_unit_key));
        Drawable icon= UsefulFunctions.getColoredDrawable(
                getActivity(),
                R.drawable.speed_preferences_icon,
                ContextCompat.getColor(getActivity(),
                        R.color.colorPrimary)
        );
        preference.setIcon(icon);
    }

    private void setPressurePreferenceIcon(){
        PressureUnitsDialogPreference preference
                = (PressureUnitsDialogPreference) findPreference(getString(R.string.preferences_pressure_unit_key));
        Drawable icon= UsefulFunctions.getColoredDrawable(
                getActivity(),
                R.drawable.pressure_preferences_icon,
                ContextCompat.getColor(getActivity(), R.color.colorPrimary)
        );
        preference.setIcon(icon);
    }

    private void setTimePreferenceIcon(){
        TimeUnitsDialogPreference preference = (TimeUnitsDialogPreference) findPreference(getString(R.string.preferences_time_unit_key));
        Drawable icon= UsefulFunctions.getColoredDrawable(
                getActivity(),
                R.drawable.time_preferences_icon,
                ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        preference.setIcon(icon);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onOptionsItemSelected(item)) {
                getActivity().onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
