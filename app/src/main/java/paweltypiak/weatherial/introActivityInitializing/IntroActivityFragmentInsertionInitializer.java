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
package paweltypiak.weatherial.introActivityInitializing;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.IntroActivityGeolocalizationMethodFragment;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.IntroActivityLanguageFragment;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.IntroActivityUnitsFragment;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityConfigurationFragmentInitializing.IntroActivityConfigurationFragment;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.IntroActivityStartFragment;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.IntroActivityLoadingFragment;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLocationFragmentInitializing.IntroActivityDefaultLocationFragment;

public class IntroActivityFragmentInsertionInitializer {

    private boolean isFirstLaunch;
    private FragmentManager fragmentManager;
    private FragmentManager childFragmentManager;
    private IntroActivityConfigurationFragment configurationFragment;
    private boolean isLoadingFragmentInserted=false;

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
        if(fragmentManager ==null){
            fragmentManager = ((FragmentActivity)activity).
                    getSupportFragmentManager();
        }
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
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
                activity.getString(R.string.intro_activity_default_location_fragment_tag)
        );
        isLoadingFragmentInserted=false;
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
                                       int selectedDefaultLocationOption,
                                       String differentLocationName) {
        IntroActivityLoadingFragment loadingFragment
                = IntroActivityLoadingFragment.newInstance(
                activity,
                isFirstLaunch,
                selectedDefaultLocationOption,
                selectedGeolocalizationMethod,differentLocationName);
        insertSettingsFragment(
                loadingFragment,
                activity.getString(R.string.intro_activity_loading_fragment_tag)
        );
        isLoadingFragmentInserted=true;
    }

    private void insertSettingsFragment(android.support.v4.app.Fragment settingsFragment, String tag) {
        //set nested fragment
        if(childFragmentManager ==null){
            childFragmentManager = configurationFragment.getChildFragmentManager();
        }
        FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();
        fragmentTransaction.replace(
                R.id.intro_activity_configuration_fragment_placeholder,
                settingsFragment,
                tag)
                .commit();
    }

    public Fragment getSettingsFragment(String tag){
        //get child fragment by tag
        Fragment childFragment = childFragmentManager.findFragmentByTag(tag);
        return childFragment;
    }

    IntroActivityConfigurationFragment getConfigurationFragment() {
        return configurationFragment;
    }

    boolean isLoadingFragmentInserted() {
        return isLoadingFragmentInserted;
    }
}
