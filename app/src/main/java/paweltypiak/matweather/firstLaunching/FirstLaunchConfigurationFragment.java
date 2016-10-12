package paweltypiak.matweather.firstLaunching;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class FirstLaunchConfigurationFragment extends Fragment{

    private FragmentTransaction fragmentTransaction;
    private FirstLaunchLocationFragment locationFragment;
    private FirstLaunchLoadingFragment loadingFragment;
    private FirstLaunchGeolocalizationMethodFragment geolocalizationMethodsFragment;
    private boolean isFirstLaunch;
    private boolean isAfterChoosingGeolocalizationMethod =false;
    private int selectedDefeaultLocationOption =-1;
    private int selectedGeolocalizationMethod =-1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtras();
        return inflater.inflate(R.layout.first_launch_configuraion_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAppIcon();
    }

    public static FirstLaunchConfigurationFragment newInstance(boolean isFirstLaunch,Activity activity) {
        FirstLaunchConfigurationFragment configurationFragment = new FirstLaunchConfigurationFragment();
        Bundle extras = new Bundle();
        extras.putBoolean(activity.getString(R.string.extras_is_first_launch_key), isFirstLaunch);
        configurationFragment.setArguments(extras);
        return configurationFragment;
    }

    private void getExtras(){
        //get information if it is first launch
        isFirstLaunch = getArguments().getBoolean(getString(R.string.extras_is_first_launch_key));
    }

    private void loadAppropiateFragment(){
        if(isFirstLaunch) insertNestedFragment(new FirstLaunchLanguageFragment(),"LanguageFragment");
        else initializeLoadingLocation(null);
    }

    private void setAppIcon(){
        final ImageView appIconImageView=(ImageView)getActivity().findViewById(R.id.first_launch_configuration_fragment_app_icon_image);
        Picasso.with(getActivity()).load(R.drawable.logo_intro).fit().centerInside().into(appIconImageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                loadAppropiateFragment();
            }
            @Override
            public void onError() {
            }
        });

    }

    public void insertNestedFragment(android.support.v4.app.Fragment nestedFragment,String tag) {
        //set nested fragment
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.first_launch_configuration_fragment_placeholder, nestedFragment,tag)
                .commit();
    }

    private Fragment getChildFragment(String tag){
        //get child fragment by tag
        Fragment childFragment =
                getChildFragmentManager().findFragmentByTag(tag);
        return childFragment;
    }

    public void insertLoadingFragment(int selectedGeolocalizationMethod,int selectedDefeaultLocationOption, String differentLocationName, CardView startCardViewButton) {
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        loadingFragment = FirstLaunchLoadingFragment.newInstance(getActivity(),isFirstLaunch,selectedGeolocalizationMethod,selectedDefeaultLocationOption,differentLocationName);
        fragmentTransaction.replace(R.id.first_launch_configuration_fragment_placeholder, loadingFragment, "LoadingFragment");
        fragmentTransaction.commit();
        if(startCardViewButton!=null)UsefulFunctions.setViewInvisible(startCardViewButton);
    }

    public void insertGeolocalizationMethodsFragment() {
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        geolocalizationMethodsFragment = new FirstLaunchGeolocalizationMethodFragment();
        fragmentTransaction.replace(R.id.first_launch_configuration_fragment_placeholder, geolocalizationMethodsFragment, "LocalizationOptionsFragment");
        fragmentTransaction.commit();
    }

    public int getDefeaultLocationOptionFromLocationFragment(){
        //get information if selected defeault location is current location, or different location
        try{
            locationFragment=(FirstLaunchLocationFragment)getChildFragment("LocationFragment");
            return locationFragment.getSelectedDefeaultLocationOption();
        }catch (Exception exception){
            return 0;
        }
    }

    public String getDifferentLocationNameFromLocationFragment(){
        //get name for selected location
        locationFragment=(FirstLaunchLocationFragment)getChildFragment("LocationFragment");
        String differentLocationNameString=locationFragment.getDifferentLocationName();
        return differentLocationNameString;
    }

    public void showNoDifferentLocationSelectedDialogInLocationFragment(){
        //show dialog with information about empty location name
        locationFragment.showNoDifferentLocationSelectedDialog();
    }

    public void initializeLoadingLocation(CardView nextCardViewButton){
        if(isFirstLaunch){
            if(isAfterChoosingGeolocalizationMethod ==true){
                //geolocalization after selected geololocalization method
                selectedGeolocalizationMethod = geolocalizationMethodsFragment.getSelectedGeolocalizationMethod();
                insertLoadingFragment(selectedGeolocalizationMethod,0, "",nextCardViewButton);
                UsefulFunctions.setViewInvisible(nextCardViewButton);
                isAfterChoosingGeolocalizationMethod =false;
            }
            else{
                selectedDefeaultLocationOption = getDefeaultLocationOptionFromLocationFragment();
                if(selectedDefeaultLocationOption ==0){
                    if(isAfterChoosingGeolocalizationMethod ==false){
                        //insert fragment with geolocalization methods
                        insertGeolocalizationMethodsFragment();
                        isAfterChoosingGeolocalizationMethod =true;
                    }
                }
                else{
                    String differentLocationName=getDifferentLocationNameFromLocationFragment();
                    if(differentLocationName.equals(getString(R.string.first_launch_defeault_location_different))){
                        //dialog with information that user didn't provided location name
                        showNoDifferentLocationSelectedDialogInLocationFragment();
                    }
                    else{
                        insertLoadingFragment(-1, selectedDefeaultLocationOption,differentLocationName,nextCardViewButton);
                    }
                }
            }
        }
        else {
            insertLoadingFragment(-1,-1,"",null);
        }
    }

    public int getSelectedDefeaultLocationOption() {
        return selectedDefeaultLocationOption;
    }
}
