package paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityConfigurationFragmentInitializing;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import paweltypiak.matweather.R;
import paweltypiak.matweather.introActivityInitializing.IntroActivity;
import paweltypiak.matweather.introActivityInitializing.IntroActivityFragmentInsertionInitializer;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.IntroActivityGeolocalizationMethodFragment;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityLocationFragmentInitializing.IntroActivityDefaultLocationFragment;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class IntroActivityConfigurationFragment extends Fragment{

    private boolean isFirstLaunch;
    private boolean isAfterChoosingGeolocalizationMethod =false;
    private int selectedDefeaultLocationOption =-1;
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
                load(R.drawable.logo_intro).
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
                selectedDefeaultLocationOption = getDefeaultLocationOptionFromLocationFragment();
                if(selectedDefeaultLocationOption ==0){
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
        insertLoadingFragment(selectedGeolocalizationMethod,0, "");
        isAfterChoosingGeolocalizationMethod =false;
    }

    private int getDefeaultLocationOptionFromLocationFragment(){
        //get information if selected defeault location is current location, or different location
        try{
            return getDefaultLocationFragment().getSelectedDefeaultLocationOption();
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
        setNextButtonInvisible();
        String differentLocationName=getDifferentLocationNameFromLocationFragment();
        if(differentLocationName.equals(getString(R.string.first_launch_defeault_location_different))){
            //dialog with information that user didn't provided location name
            showNoDifferentLocationSelectedDialogInLocationFragment();
        }
        else{
            insertLoadingFragment(
                    -1,
                    selectedDefeaultLocationOption,
                    differentLocationName);
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
                getSettingsFragment(getString(R.string.intro_activity_defeault_location_fragment_tag));
        return locationFragment;
    }

    private IntroActivityGeolocalizationMethodFragment getGeolocalizationMethodsFragment(){
        IntroActivityGeolocalizationMethodFragment geolocalizationMethodFragment
                =(IntroActivityGeolocalizationMethodFragment)fragmentInsertionInitializer.
                getSettingsFragment(getString(R.string.intro_activity_geolocaliztion_methods_fragment_tag));
        return geolocalizationMethodFragment;
    }

    public void showNoDifferentLocationSelectedDialogInLocationFragment(){
        //show dialog with information about empty location name
        getDefaultLocationFragment().showNoDifferentLocationSelectedDialog();
    }

    private void insertLoadingFragment(int selectedGeolocalizationMethod,
                                       int selectedDefeaultLocationOption,
                                       String differentLocationName) {
        fragmentInsertionInitializer.insertLoadingFragment(
                getActivity(),
                selectedGeolocalizationMethod,
                selectedDefeaultLocationOption,
                differentLocationName
                );
    }

    private void setNextButtonInvisible(){
        ((IntroActivity)getActivity()).getNextButtonInitializer().setNextButtonInvisible();
    }

    public int getSelectedDefeaultLocationOption() {
        return selectedDefeaultLocationOption;
    }
}
