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
package paweltypiak.weatherial.settingsActivityInitializing;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.settingsActivityInitializing.dialogPreferencesInitializing.languageVersionDialogPreferenceInitializing.OnLanguageVersionPreferenceChangeListener;
import paweltypiak.weatherial.settingsActivityInitializing.preferencesFragmentsInitializing.MainPreferencesFragment;
import paweltypiak.weatherial.settingsActivityInitializing.preferencesFragmentsInitializing.unitsPreferencesFragmentInitializing.OnUnitsPreferenceFragmentInsertionListener;
import paweltypiak.weatherial.settingsActivityInitializing.preferencesFragmentsInitializing.unitsPreferencesFragmentInitializing.UnitsPreferencesFragment;
import paweltypiak.weatherial.utils.UsefulFunctions;

public class SettingsActivity extends AppCompatPreferenceActivity
        implements OnLanguageVersionPreferenceChangeListener,
        OnUnitsPreferenceFragmentInsertionListener {

    private android.support.v7.app.ActionBar actionBar;
    private static boolean languagePreferencesChanged=false;
    private static boolean unitsPreferencesChanged=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        insertMainPreferencesFragment();
    }

    private void setActionBar(){
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.nav_drawer_settings));
        actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.colorPrimary)));
    }

    @Override
    public void updateActionBarTitle(String title) {
        actionBar.setTitle(title);
    }

    private void insertMainPreferencesFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(
                        android.R.id.content,
                        new MainPreferencesFragment(),
                        getString(R.string.settings_activity_main_preferences_fragment_tag))
                .commit();
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return MainPreferencesFragment.class.getName().equals(fragmentName) ||
                UnitsPreferencesFragment.class.getName().equals(fragmentName);
    }

    @Override
    public void onBackPressed() {
        if(actionBar.getTitle().equals(getString(R.string.preferences_units_title)))
            actionBar.setTitle(getString(R.string.nav_drawer_settings));
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        UsefulFunctions.setTaskDescription(this);
        super.onResume();
    }

    //get information about preferences changes which need to reload interface - units and language
    public static boolean isLanguagePreferencesChanged() {
        return languagePreferencesChanged;
    }

    public static boolean isUnitsPreferencesChanged() {
        return unitsPreferencesChanged;
    }

    public static void setLanguagePreferencesChanged(boolean languagePreferencesChanged) {
        SettingsActivity.languagePreferencesChanged = languagePreferencesChanged;
    }

    public static void setUnitsPreferencesChanged(boolean unitsPreferencesChanged) {
        SettingsActivity.unitsPreferencesChanged = unitsPreferencesChanged;
    }

    //recreate fragment after language change
    @Override
    public void recreateSettings(){
        recreate();
    }

    public OnLanguageVersionPreferenceChangeListener getRefreshSettingsFragmentListener() {
        return this;
    }
}