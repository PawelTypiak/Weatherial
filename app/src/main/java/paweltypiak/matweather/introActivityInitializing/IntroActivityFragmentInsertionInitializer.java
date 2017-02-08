package paweltypiak.matweather.introActivityInitializing;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import paweltypiak.matweather.R;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.IntroActivityGeolocalizationMethodFragment;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.IntroActivityLanguageFragment;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.IntroActivityUnitsFragment;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityConfigurationFragmentInitializing.IntroActivityConfigurationFragment;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.IntroActivityStartFragment;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.IntroActivityLoadingFragment;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityLocationFragmentInitializing.IntroActivityDefaultLocationFragment;

public class IntroActivityFragmentInsertionInitializer {

    private boolean isFirstLaunch;
    private FragmentManager fragmentMenager;
    private FragmentManager childFragmentMenager;
    private IntroActivityConfigurationFragment configurationFragment;

    public IntroActivityFragmentInsertionInitializer(boolean isFirstLaunch){
        this.isFirstLaunch=isFirstLaunch;
    }

    void insertStartFragment(Activity activity){
        IntroActivityStartFragment startFragment=new IntroActivityStartFragment();
        insertMainFragment(
                activity,
                startFragment,
                activity.getString(R.string.intro_activity_start_fragment_tag)
        );
    }

    void insertConfigurationFragment(Activity activity){
        configurationFragment= IntroActivityConfigurationFragment.newInstance(activity,isFirstLaunch);
        insertMainFragment(
                activity,
                configurationFragment,
                activity.getString(R.string.intro_activity_configuration_fragment_tag)
        );
    }

    private void insertMainFragment(Activity activity,Fragment fragment,String tag){
        if(fragmentMenager==null){
            fragmentMenager  = ((FragmentActivity)activity).
                    getSupportFragmentManager();
        }
        FragmentTransaction fragmentTransaction=fragmentMenager.beginTransaction();
        fragmentTransaction.replace(
                R.id.intro_activity_main_fragment_placeholder,
                fragment,
                tag);
        fragmentTransaction.commit();
    }

    public void insertLanguageFragment(Activity activity){
        IntroActivityLanguageFragment languageFragment
                =new IntroActivityLanguageFragment();
        insertSettingsFragment(
                languageFragment,
                activity.getString(R.string.intro_activity_language_fragment_tag)
        );
    }

    public void insertUnitsFragment(Activity activity){
        IntroActivityUnitsFragment unitsFragment
                =new IntroActivityUnitsFragment();
        insertSettingsFragment(
                unitsFragment,
                activity.getString(R.string.intro_activity_units_fragment_tag)
        );
    }

    public void insertDefaultLocationFragment(Activity activity){
        IntroActivityDefaultLocationFragment defaultLocationFragment
                =new IntroActivityDefaultLocationFragment();
        insertSettingsFragment(
                defaultLocationFragment,
                activity.getString(R.string.intro_activity_defeault_location_fragment_tag)
        );
    }

    public void insertGeolocalizationMethodsFragment(Activity activity) {
        IntroActivityGeolocalizationMethodFragment geolocalizationMethodsFragment
                = new IntroActivityGeolocalizationMethodFragment();
        FragmentTransaction fragmentTransaction
                = configurationFragment.getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(
                R.id.intro_activity_configuration_fragment_placeholder,
                geolocalizationMethodsFragment,
                activity.getString(R.string.intro_activity_geolocaliztion_methods_fragment_tag));
        fragmentTransaction.commit();
        insertSettingsFragment(
                geolocalizationMethodsFragment,
                activity.getString(R.string.intro_activity_geolocaliztion_methods_fragment_tag)
        );
    }

    public void insertLoadingFragment(Activity activity,
                                       int selectedGeolocalizationMethod,
                                       int selectedDefeaultLocationOption,
                                       String differentLocationName) {
        IntroActivityLoadingFragment loadingFragment
                = IntroActivityLoadingFragment.newInstance(
                activity,
                isFirstLaunch,
                selectedDefeaultLocationOption,
                selectedGeolocalizationMethod,differentLocationName);
        insertSettingsFragment(
                loadingFragment,
                activity.getString(R.string.intro_activity_loading_fragment_tag)
        );
    }

    private void insertSettingsFragment(android.support.v4.app.Fragment settingsFragment, String tag) {
        //set nested fragment
        if(childFragmentMenager==null){
            childFragmentMenager  = configurationFragment.getChildFragmentManager();
        }
        FragmentTransaction fragmentTransaction = childFragmentMenager.beginTransaction();
        fragmentTransaction.replace(
                R.id.intro_activity_configuration_fragment_placeholder,
                settingsFragment,
                tag)
                .commit();
    }

    public Fragment getSettingsFragment(String tag){
        //get child fragment by tag
        Fragment childFragment = childFragmentMenager.findFragmentByTag(tag);
        return childFragment;
    }

    public IntroActivityConfigurationFragment getConfigurationFragment() {
        return configurationFragment;
    }
}
