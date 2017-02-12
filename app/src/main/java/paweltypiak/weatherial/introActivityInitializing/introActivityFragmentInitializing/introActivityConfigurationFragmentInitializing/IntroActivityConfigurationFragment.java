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
package paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityConfigurationFragmentInitializing;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.introActivityInitializing.IntroActivity;
import paweltypiak.weatherial.introActivityInitializing.IntroActivityFragmentInsertionInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.IntroActivityGeolocalizationMethodFragment;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLocationFragmentInitializing.IntroActivityDefaultLocationFragment;

public class IntroActivityConfigurationFragment extends Fragment{

    private boolean isFirstLaunch;
    private boolean isAfterChoosingGeolocalizationMethod =false;
    private int selectedDefaultLocationOption =-1;
    private int selectedGeolocalizationMethod =-1;

    private IntroActivityFragmentInsertionInitializer fragmentInsertionInitializer;

    public static IntroActivityConfigurationFragment newInstance(Activity activity,boolean isFirstLaunch ) {
        IntroActivityConfigurationFragment configurationFragment = new IntroActivityConfigurationFragment();
        Bundle extras = new Bundle();
        extras.putBoolean(activity.getString(R.string.extras_is_first_launch_key), isFirstLaunch);
        configurationFragment.setArguments(extras);
        return configurationFragment;
    }

    private void getExtras(){
        //get information if it is first launch
        isFirstLaunch = getArguments().getBoolean(getString(R.string.extras_is_first_launch_key));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtras();
        return inflater.inflate(R.layout.intro_activity_fragment_configuration, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFragmentInsertionInitializer();
        initializeSettingsFragmentInserting();
    }

    private void getFragmentInsertionInitializer(){
        fragmentInsertionInitializer=((IntroActivity)getActivity()).getFragmentInsertionInitializer();
    }

    private void initializeSettingsFragmentInserting(){
        final ImageView appIconImageView
                =(ImageView)getActivity().findViewById(R.id.intro_activity_configuration_fragment_app_icon_image);
        Picasso.with(getActivity()).
                load(R.drawable.logo_intro_activity).
                fit().
                centerInside().
                into(appIconImageView, new Callback() {

            @Override
            public void onSuccess() {
                if(isFirstLaunch) {
                    insertLanguageFragment();
                }
                else {
                    initializeLoadingLocation();
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void insertLanguageFragment(){
        fragmentInsertionInitializer.insertLanguageFragment(getActivity());
    }

    public void initializeLoadingLocation(){
        if(isFirstLaunch){
            if(isAfterChoosingGeolocalizationMethod ==true){
                //geolocalization after selected geololocalization method
                initializeGeolocalizationDownloadingAfterGeolocalizationMethodsSelecting();
            }
            else{
                selectedDefaultLocationOption = getDefaultLocationOptionFromLocationFragment();
                if(selectedDefaultLocationOption ==0){
                    showGeolocalizationMethodsFragment();
                }
                else{
                    initializeWeatherDataDownloadingForDifferentLocation();
                }
            }
        }
        else {
            insertLoadingFragment(-1,-1,"");
        }
    }

    private void initializeGeolocalizationDownloadingAfterGeolocalizationMethodsSelecting(){
        setNextButtonInvisible();
        selectedGeolocalizationMethod = getGeolocalizationMethodsFragment().getSelectedGeolocalizationMethod();
        isAfterChoosingGeolocalizationMethod =false;
        insertLoadingFragment(selectedGeolocalizationMethod,0, "");
    }

    private int getDefaultLocationOptionFromLocationFragment(){
        //get information if selected default location is current location, or different location
        try{
            return getDefaultLocationFragment().getSelectedDefaultLocationOption();
        }catch (Exception exception){
            return 0;
        }
    }

    private void showGeolocalizationMethodsFragment(){
        if(isAfterChoosingGeolocalizationMethod ==false){
            //insert fragment with geolocalization methods
            insertGeolocalizationMethodsFragment();
            isAfterChoosingGeolocalizationMethod =true;
        }
    }

    private void insertGeolocalizationMethodsFragment() {
        fragmentInsertionInitializer.insertGeolocalizationMethodsFragment(getActivity());
    }

    private void initializeWeatherDataDownloadingForDifferentLocation(){
        String differentLocationName=getDifferentLocationNameFromLocationFragment();
        if(differentLocationName.equals(getString(R.string.intro_activity_default_location_different))){
            //dialog with information that user didn't provided location name
            showNoDifferentLocationSelectedDialogInLocationFragment();
        }
        else{
            insertLoadingFragment(
                    -1,
                    selectedDefaultLocationOption,
                    differentLocationName);
            setNextButtonInvisible();
        }
    }

    private String getDifferentLocationNameFromLocationFragment(){
        //get name for selected location
        IntroActivityDefaultLocationFragment locationFragment= getDefaultLocationFragment();
        String differentLocationNameString=locationFragment.getDifferentLocationName();
        return differentLocationNameString;
    }

    private IntroActivityDefaultLocationFragment getDefaultLocationFragment(){
        IntroActivityDefaultLocationFragment locationFragment
                =(IntroActivityDefaultLocationFragment)fragmentInsertionInitializer.
                getSettingsFragment(getString(R.string.intro_activity_default_location_fragment_tag));
        return locationFragment;
    }

    private IntroActivityGeolocalizationMethodFragment getGeolocalizationMethodsFragment(){
        IntroActivityGeolocalizationMethodFragment geolocalizationMethodFragment
                =(IntroActivityGeolocalizationMethodFragment)fragmentInsertionInitializer.
                getSettingsFragment(getString(R.string.intro_activity_geolocaliztion_methods_fragment_tag));
        return geolocalizationMethodFragment;
    }

    private void showNoDifferentLocationSelectedDialogInLocationFragment(){
        //show dialog with information about empty location name
        getDefaultLocationFragment().showNoDifferentLocationSelectedDialog();
    }

    private void insertLoadingFragment(int selectedGeolocalizationMethod,
                                       int selectedDefaultLocationOption,
                                       String differentLocationName) {
        fragmentInsertionInitializer.insertLoadingFragment(
                getActivity(),
                selectedGeolocalizationMethod,
                selectedDefaultLocationOption,
                differentLocationName
                );
    }

    private void setNextButtonInvisible(){
        ((IntroActivity)getActivity()).getNextButtonInitializer().setNextButtonInvisible();
    }

    public int getSelectedDefaultLocationOption() {
        return selectedDefaultLocationOption;
    }
}
