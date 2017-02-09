package paweltypiak.weatherial.settingsActivityInitializing.preferencesFragmentsInitializing;

import android.app.FragmentManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.settingsActivityInitializing.dialogPreferencesInitializing.DefaultLocationDialogPreference;
import paweltypiak.weatherial.settingsActivityInitializing.dialogPreferencesInitializing.GeolocalizationMethodDialogPreference;
import paweltypiak.weatherial.settingsActivityInitializing.dialogPreferencesInitializing.languageVersionDialogPreferenceInitializing.LanguageVersionDialogPreference;
import paweltypiak.weatherial.settingsActivityInitializing.preferencesFragmentsInitializing.unitsPreferencesFragmentInitializing.UnitsPreferencesFragment;
import paweltypiak.weatherial.usefulClasses.UsefulFunctions;

public class MainPreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setHasOptionsMenu(true);
        findPreference(getString(R.string.preferences_units_key)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                insertUnitsPreferencesFragment();
                return true;
            }
        });
    }

    private void insertUnitsPreferencesFragment(){
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(
                        android.R.id.content,
                        new UnitsPreferencesFragment(),
                        getString(R.string.settings_activity_units_preferences_fragment_tag))
                .addToBackStack(null)
                .commit();
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
        setDefaultLocationPreferenceIcon();
        setGeolocalizationMethodPreferenceIcon();
        setUnitsPreferenceIcon();
        setLanguageVersionIcon();
    }

    private void setDefaultLocationPreferenceIcon(){
        DefaultLocationDialogPreference preference
                = (DefaultLocationDialogPreference) findPreference(
                getString(R.string.preferences_default_location_key));
        Drawable icon
                = UsefulFunctions.getColoredDrawable(
                getActivity(),
                R.drawable.default_location_preferences_icon,
                ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        preference.setIcon(icon);
    }

    private void setGeolocalizationMethodPreferenceIcon(){
        GeolocalizationMethodDialogPreference preference
                = (GeolocalizationMethodDialogPreference) findPreference(
                getString(R.string.preferences_geolocalization_method_key));
        Drawable icon
                = UsefulFunctions.getColoredDrawable(
                getActivity(),
                R.drawable.geolocalization_method_preferences_icon,
                ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        preference.setIcon(icon);
    }

    private void setUnitsPreferenceIcon(){
        Preference preference=
                findPreference(getString(R.string.preferences_units_key));
        Drawable icon
                = UsefulFunctions.getColoredDrawable(
                getActivity(),
                R.drawable.units_preferences_icon,
                ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        preference.setIcon(icon);
    }

    private void setLanguageVersionIcon(){
        LanguageVersionDialogPreference preference=
                (LanguageVersionDialogPreference)findPreference(getString(R.string.preferences_language_version_key));
        Drawable icon
                = UsefulFunctions.getColoredDrawable(
                getActivity(),
                R.drawable.language_version_preferences_icon,
                ContextCompat.getColor(getActivity(), R.color.colorPrimary));
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
