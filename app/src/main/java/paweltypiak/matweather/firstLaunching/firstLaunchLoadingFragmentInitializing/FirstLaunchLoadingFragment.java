package paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import paweltypiak.matweather.firstLaunching.ShowLocationFragmentAgainListener;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDataDownloader;
import paweltypiak.matweather.R;

public class FirstLaunchLoadingFragment
        extends Fragment
        implements
        OnRequestLocalizationPermissionsListener {

    private boolean isFirstLaunch;
    private int selectedDefeaultLocationOption;
    private int selectedDefeaultLocalizationMethod;
    private String differentLocationName;
    private FirstLaunchLoadingFragmentDataDownloader dataDownloader;
    private ShowLocationFragmentAgainListener showLocationFragmentAgainListener;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;

    public static FirstLaunchLoadingFragment newInstance(
            Activity activity,
            boolean isFirstLaunch,
            int selectedDefeaultLocationOption,
            int selectedGeolocalizationMethod,
            String differentLocationName) {
        FirstLaunchLoadingFragment loadingFragment = new FirstLaunchLoadingFragment();
        Bundle extras = new Bundle();
        extras.putBoolean(activity.getString(R.string.extras_is_first_launch_key),isFirstLaunch);
        extras.putInt(activity.getString(R.string.extras_selected_defeault_location_option_key), selectedDefeaultLocationOption);
        extras.putInt(activity.getString(R.string.extras_selected_geolocalization_method_key),selectedGeolocalizationMethod);
        extras.putString(activity.getString(R.string.extras_different_location_name_key), differentLocationName);
        loadingFragment.setArguments(extras);
        return loadingFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShowLocationFragmentAgainListener) {
            showLocationFragmentAgainListener = (ShowLocationFragmentAgainListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtras();
        return inflater.inflate(R.layout.fragment_first_launch_loading, parent, false);
    }

    private void getExtras(){
        isFirstLaunch=getArguments().getBoolean(getString(R.string.extras_is_first_launch_key));
        selectedDefeaultLocationOption = getArguments().getInt(getString(R.string.extras_selected_defeault_location_option_key), -1);
        selectedDefeaultLocalizationMethod =getArguments().getInt(getString(R.string.extras_selected_geolocalization_method_key), -1);
        differentLocationName = getArguments().getString(getString(R.string.extras_different_location_name_key), null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeDataDownload();
    }

    private void initializeDataDownload(){
        dataDownloader=new FirstLaunchLoadingFragmentDataDownloader(
                getActivity(),
                showLocationFragmentAgainListener,
                this,
                isFirstLaunch,
                selectedDefeaultLocalizationMethod,
                selectedDefeaultLocationOption,
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
