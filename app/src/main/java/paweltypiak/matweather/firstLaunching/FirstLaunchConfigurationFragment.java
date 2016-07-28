package paweltypiak.matweather.firstLaunching;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import paweltypiak.matweather.R;
import paweltypiak.matweather.UsefulFunctions;

public class FirstLaunchConfigurationFragment extends Fragment{

    private FragmentTransaction fragmentTransaction;
    private FirstLaunchLocationFragment locationFragment;
    private FirstLaunchLoadingFragment loadingFragment;
    private boolean isFirstLaunch;

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
        else initializeLoadingLocation();
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

    public void insertLoadingFragment(int choosenLocationOption, String differentLocationName) {
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        loadingFragment = FirstLaunchLoadingFragment.newInstance(isFirstLaunch,choosenLocationOption,differentLocationName,getActivity());
        fragmentTransaction.replace(R.id.first_launch_configuration_fragment_placeholder, loadingFragment, "LoadingFragment");
        fragmentTransaction.commit();
    }

    public int getChoosenOptionFromLocationFragment(){
        locationFragment=(FirstLaunchLocationFragment)getChildFragment("LocationFragment");
        int choosenOption=locationFragment.getChoosenLocationOption();
        return choosenOption;
    }

    public String getDifferentLocationNameFromLocationFragment(){
        String differentLocationNameString=locationFragment.getDifferentLocationName();
        return differentLocationNameString;
    }

    public void showEmptyLocationNameDialogInLocationFragment(){
        locationFragment.showEmptyLocationNameDialog();
    }

    public void initializeLoadingLocation(){
        if(isFirstLaunch){
            int choosenLocationOption=getChoosenOptionFromLocationFragment();
            if(choosenLocationOption==1){
                Toast.makeText(getActivity(), "Work in progress, choose other option",
                        Toast.LENGTH_LONG).show();
            }
            else{
                String differentLocationName=getDifferentLocationNameFromLocationFragment();
                if(differentLocationName.equals(getString(R.string.first_launch_layout_location_different))){
                    showEmptyLocationNameDialogInLocationFragment();
                }
                else{
                    insertLoadingFragment(choosenLocationOption,differentLocationName);
                }
            }
        }
        else insertLoadingFragment(0,"");

    }

}
