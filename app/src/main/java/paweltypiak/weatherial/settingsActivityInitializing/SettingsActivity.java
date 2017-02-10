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
import paweltypiak.weatherial.usefulClasses.UsefulFunctions;

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