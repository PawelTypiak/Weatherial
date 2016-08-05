package paweltypiak.matweather.firstLaunching;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
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
    private FirstLaunchLocalizationOptionsFragment localizationOptionsFragment;
    private boolean isFirstLaunch;
    private boolean afterLocalizationOptionsFragment=false;
    private int choosenLocationOption=0;
    private int choosenLocalizationOption=0;

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
        if(isFirstLaunch) insertNestedFragment(new FirstLaunchLanguageFragment(),"LanguageFragment");
        else initializeLoadingLocation(null);
    }
    public static FirstLaunchConfigurationFragment newInstance(boolean isFirstLaunch,Activity activity) {
        FirstLaunchConfigurationFragment configurationFragment = new FirstLaunchConfigurationFragment();
        Bundle extras = new Bundle();
        extras.putBoolean(activity.getString(R.string.extras_is_first_launch_key), isFirstLaunch);
        configurationFragment.setArguments(extras);
        return configurationFragment;
    }
    private void getExtras(){
        isFirstLaunch = getArguments().getBoolean(getString(R.string.extras_is_first_launch_key));
    }

    private void setAppIcon(){
        final ImageView appIconImageView=(ImageView)getActivity().findViewById(R.id.first_launch_configuration_fragment_app_icon_image);
        Picasso.with(getActivity()).load(R.drawable.app_icon).fit().centerInside().into(appIconImageView);
    }

    public void insertNestedFragment(android.support.v4.app.Fragment nestedFragment,String tag) {
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.first_launch_configuration_fragment_placeholder, nestedFragment,tag)
                .commit();
    }

    private Fragment getChildFragment(String tag){
        Fragment childFragment =
                getChildFragmentManager().findFragmentByTag(tag);
        return childFragment;
    }

    public void insertLoadingFragment(int choosenLocalizationOption,int choosenLocationOption, String differentLocationName, CardView startCardViewButton) {
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        loadingFragment = FirstLaunchLoadingFragment.newInstance(isFirstLaunch,choosenLocalizationOption,choosenLocationOption,differentLocationName,getActivity());
        fragmentTransaction.replace(R.id.first_launch_configuration_fragment_placeholder, loadingFragment, "LoadingFragment");
        fragmentTransaction.commit();
        if(startCardViewButton!=null)UsefulFunctions.setViewInvisible(startCardViewButton);
    }

    public void insertLocalizationOptionsFragment() {
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        localizationOptionsFragment = new FirstLaunchLocalizationOptionsFragment();
        fragmentTransaction.replace(R.id.first_launch_configuration_fragment_placeholder, localizationOptionsFragment, "LocalizationOptionsFragment");
        fragmentTransaction.commit();
    }

    public int getChoosenOptionFromLocationFragment(){
        try{
            locationFragment=(FirstLaunchLocationFragment)getChildFragment("LocationFragment");
            return locationFragment.getChoosenLocationOption();
        }catch (Exception exception){
            return 0;
        }
    }

    public String getDifferentLocationNameFromLocationFragment(){
        locationFragment=(FirstLaunchLocationFragment)getChildFragment("LocationFragment");
        String differentLocationNameString=locationFragment.getDifferentLocationName();
        return differentLocationNameString;
    }

    public void showEmptyLocationNameDialogInLocationFragment(){
        locationFragment.showEmptyLocationNameDialog();
    }

    public void initializeLoadingLocation(CardView startCardViewButton){
        if(isFirstLaunch){
            if(afterLocalizationOptionsFragment==true){
                choosenLocalizationOption=localizationOptionsFragment.getChoosenLocalizationOption();
                insertLoadingFragment(choosenLocalizationOption,1, "",startCardViewButton);
                UsefulFunctions.setViewInvisible(startCardViewButton);
                afterLocalizationOptionsFragment=false;
            }
            else{
                choosenLocationOption=getChoosenOptionFromLocationFragment();
                if(choosenLocationOption==1){
                    Log.d("afterlocopt", ""+afterLocalizationOptionsFragment);
                    if(afterLocalizationOptionsFragment==false){
                        insertLocalizationOptionsFragment();
                        afterLocalizationOptionsFragment=true;
                    }
                }
                else{
                    String differentLocationName=getDifferentLocationNameFromLocationFragment();
                    if(differentLocationName.equals(getString(R.string.first_launch_layout_location_different))){
                        showEmptyLocationNameDialogInLocationFragment();
                    }
                    else{
                        insertLoadingFragment(0,choosenLocationOption,differentLocationName,startCardViewButton);
                    }
                }
            }
        }
        else {
            Log.d("nextlaunch", "nextlaunch");
            insertLoadingFragment(0,0,"",null);
        }
    }

    public int getChoosenLocationOption() {
        return choosenLocationOption;
    }
}
