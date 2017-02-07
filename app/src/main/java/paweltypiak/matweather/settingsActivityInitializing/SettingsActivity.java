package paweltypiak.matweather.settingsActivityInitializing;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v4.content.ContextCompat;

import paweltypiak.matweather.R;
import paweltypiak.matweather.settingsActivityInitializing.dialogPreferencesInitializing.LanguageVersionDialogPreference;
import paweltypiak.matweather.settingsActivityInitializing.preferencesFragmentsInitializing.MainPreferencesFragment;
import paweltypiak.matweather.settingsActivityInitializing.preferencesFragmentsInitializing.unitsPreferencesFragmentInitializing.OnUnitsPreferenceFragmentInsertionListener;
import paweltypiak.matweather.settingsActivityInitializing.preferencesFragmentsInitializing.unitsPreferencesFragmentInitializing.UnitsPreferencesFragment;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class SettingsActivity extends AppCompatPreferenceActivity
        implements LanguageVersionDialogPreference.RecreateSettingsListener,
        OnUnitsPreferenceFragmentInsertionListener
{
    private static android.support.v7.app.ActionBar actionBar;
    private static boolean languagePreferencesChanged=false;
    private static boolean unitsPreferencesChanged=false;
    private static LanguageVersionDialogPreference.RecreateSettingsListener refreshSettingsFragmentListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshSettingsFragmentListener=this;
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
    public void recreateSettings(){
        recreate();
    }

    public static LanguageVersionDialogPreference.RecreateSettingsListener getRefreshSettingsFragmentListener() {
        // TODO: convert to non-static
        return refreshSettingsFragmentListener;
    }

    /*public static class MainPreferencesFragment extends PreferenceFragment{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            setHasOptionsMenu(true);
            findPreference(getString(R.string.preferences_units_key)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    fragmentManager.beginTransaction()
                            .replace(
                                    android.R.id.content,
                                    new UnitsPreferencesFragment(),
                                    getString(R.string.settings_activity_units_preferences_fragment_tag))
                            .addToBackStack(null)
                            .commit();
                    actionBar.setTitle(getString(R.string.preferences_units_title));
                    return true;
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = super.onCreateView(inflater, container, savedInstanceState);
            if(v != null) {
                ListView lv = (ListView) v.findViewById(android.R.id.list);
                lv.setPadding(0, 0, 0, 0);
            }
            return v;
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

    public static class UnitsPreferencesFragment extends PreferenceFragment {
        //fragment with units preferences
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
            addPreferencesFromResource(R.xml.preferences_units);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = super.onCreateView(inflater, container, savedInstanceState);
            if(v != null) {
                ListView lv = (ListView) v.findViewById(android.R.id.list);
                lv.setPadding(0, 0, 0, 0);
            }
            //setCustomIcon();
            return v;
        }

        private void setCustomIcon(){
            TemperatureUnitsDialogPreference test = (TemperatureUnitsDialogPreference) findPreference("temp_key");
            Drawable icon= UsefulFunctions.getColoredDrawable(getActivity(), R.drawable.temperature_unit_icon, ContextCompat.getColor(getActivity(),R.color.colorPrimary));
            test.setIcon(icon);
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
    }*/
}