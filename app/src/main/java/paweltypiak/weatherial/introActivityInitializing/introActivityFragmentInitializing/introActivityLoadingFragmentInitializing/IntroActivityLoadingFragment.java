package paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityConfigurationFragmentInitializing.OnSettingsFragmentViewCreatedListener;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLocationFragmentInitializing.ShowLocationFragmentAgainListener;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentDataDownloader;
import paweltypiak.weatherial.R;

public class IntroActivityLoadingFragment
        extends Fragment
        implements
        OnRequestLocalizationPermissionsListener {

    private boolean isFirstLaunch;
    private int selectedDefaultLocationOption;
    private int selectedDefaultLocalizationMethod;
    private String differentLocationName;
    private IntroActivityLoadingFragmentDataDownloader dataDownloader;
    private ShowLocationFragmentAgainListener showLocationFragmentAgainListener;
    private OnSettingsFragmentViewCreatedListener onSettingsFragmentViewCreatedListener;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;

    public static IntroActivityLoadingFragment newInstance(
            Activity activity,
            boolean isFirstLaunch,
            int selectedDefaultLocationOption,
            int selectedGeolocalizationMethod,
            String differentLocationName) {
        IntroActivityLoadingFragment loadingFragment = new IntroActivityLoadingFragment();
        Bundle extras = new Bundle();
        extras.putBoolean(activity.getString(R.string.extras_is_first_launch_key),isFirstLaunch);
        extras.putInt(activity.getString(R.string.extras_selected_default_location_option_key), selectedDefaultLocationOption);
        extras.putInt(activity.getString(R.string.extras_selected_geolocalization_method_key),selectedGeolocalizationMethod);
        extras.putString(activity.getString(R.string.extras_different_location_name_key), differentLocationName);
        loadingFragment.setArguments(extras);
        return loadingFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getListeners(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getListeners(activity);
        }
    }

    private void getListeners(Context context){
        getShowLocationFragmentAgainListener(context);
        getOnSettingsFragmentViewCreatedListener(context);
    }

    private void getShowLocationFragmentAgainListener(Context context){
        if (context instanceof ShowLocationFragmentAgainListener) {
            showLocationFragmentAgainListener = (ShowLocationFragmentAgainListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement showLocationFragmentAgainListener");
        }
    }

    private void getOnSettingsFragmentViewCreatedListener(Context context){
        if (context instanceof OnSettingsFragmentViewCreatedListener) {
            onSettingsFragmentViewCreatedListener = (OnSettingsFragmentViewCreatedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement onSettingsFragmentViewCreatedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtras();
        return inflater.inflate(R.layout.intro_activity_fragment_loading, parent, false);
    }

    private void getExtras(){
        isFirstLaunch=getArguments().getBoolean(getString(R.string.extras_is_first_launch_key));
        selectedDefaultLocationOption = getArguments().getInt(getString(R.string.extras_selected_default_location_option_key), -1);
        selectedDefaultLocalizationMethod =getArguments().getInt(getString(R.string.extras_selected_geolocalization_method_key), -1);
        differentLocationName = getArguments().getString(getString(R.string.extras_different_location_name_key), null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeDataDownload();
        if(isFirstLaunch!=true){
            onSettingsFragmentViewCreatedListener.fadeInConfigurationFragmentLayout();
        }
    }

    private void initializeDataDownload(){
        dataDownloader=new IntroActivityLoadingFragmentDataDownloader(
                getActivity(),
                showLocationFragmentAgainListener,
                this,
                isFirstLaunch,
                selectedDefaultLocalizationMethod,
                selectedDefaultLocationOption,
                differentLocationName
        );
    }

    @Override
    public void requestLocalizationPermissions() {
        requestPermissions( new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        dataDownloader.getGeolocalizationDownloader().onRequestLocationPermissionsResult(requestCode,grantResults);
    }
}
