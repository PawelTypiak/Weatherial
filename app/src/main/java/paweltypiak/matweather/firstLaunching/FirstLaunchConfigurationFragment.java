package paweltypiak.matweather.firstLaunching;

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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import paweltypiak.matweather.R;
import paweltypiak.matweather.UsefulFunctions;

public class FirstLaunchConfigurationFragment extends Fragment{

    private FragmentTransaction fragmentTransaction;
    private FirstLaunchLocationFragment locationFragment;
    private FirstLaunchLoadingFragment loadingFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_launch_configuraion_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        insertNestedFragment(new FirstLaunchLanguageFragment(),"LanguageFragment");
        setAppIcon();
    }

    private void setAppIcon(){
        ImageView appIconImageView;
        appIconImageView=(ImageView)getActivity().findViewById(R.id.first_launch_configuration_fragment_app_icon_image);
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
        loadingFragment = FirstLaunchLoadingFragment.newInstance(choosenLocationOption,differentLocationName,getActivity());
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
        int choosenLocationOption=getChoosenOptionFromLocationFragment();
        String differentLocationName;
        if(choosenLocationOption==1){
            Toast.makeText(getActivity(), "Work in progress, choose other option",
                    Toast.LENGTH_LONG).show();
        }
        else{
            differentLocationName=getDifferentLocationNameFromLocationFragment();
            if(differentLocationName.equals(getString(R.string.first_launch_layout_location_different))){
                showEmptyLocationNameDialogInLocationFragment();
            }
            else{
                insertLoadingFragment(choosenLocationOption,differentLocationName);
            }
        }
    }

}
