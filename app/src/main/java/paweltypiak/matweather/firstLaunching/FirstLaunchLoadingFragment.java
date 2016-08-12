package paweltypiak.matweather.firstLaunching;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import paweltypiak.matweather.usefulClasses.DialogInitializer;
import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.FavouritesEditor;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;
import paweltypiak.matweather.localizationDataDownloading.GeocodingCallback;
import paweltypiak.matweather.localizationDataDownloading.GeocodingDownloader;
import paweltypiak.matweather.localizationDataDownloading.CurrentCoordinatesDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDownloadCallback;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataInitializer;
import paweltypiak.matweather.jsonHandling.Channel;

public class FirstLaunchLoadingFragment extends Fragment implements WeatherDownloadCallback,GeocodingCallback{
    private int choosenLocationOption;
    private String differentLocationName;
    private DialogInitializer dialogInitializer;
    private AlertDialog noLocationResultsDialog;
    private AlertDialog locationResultsDialog;
    private AlertDialog internetFailureWeatherDialog;
    private AlertDialog internetFailureLocalizationDialog;
    private AlertDialog serviceFailureDialog;
    private AlertDialog localizationFailureDialog;
    private AlertDialog permissionDeniedDialog;
    private AlertDialog localizationOptionsDialog;
    private AlertDialog changeLocationAfterFailureDialog;
    private WeatherDataInitializer dataInitializer;
    private String location;
    private ProgressBar loadingBar;
    private TextView messageTextView;
    private View marginView;
    private ChooseLocationAgainListener locationListener;
    private boolean isFirstLaunch;
    private int choosenLocalizationOption;
    private CurrentCoordinatesDownloader currentCoordinatesDownloader;
    private SharedPreferences sharedPreferences;
    private boolean isFirstLocationGeolocalization;
    private String firstLocation;
    private boolean isNextLaunchAfterFailure=false;
    private String changedLocation;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChooseLocationAgainListener) {
            locationListener = (ChooseLocationAgainListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtras();
        return inflater.inflate(R.layout.first_launch_loading_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeDialogs();
        getLayoutResources();
        getSharedPreferences();
        if(isFirstLaunch) initializeFirstLaunch();
        else initializeNextLaunch();
    }

    public static FirstLaunchLoadingFragment newInstance(boolean isFirstLaunch, int choosenLocalizationOption, int choosenLocationOption, String differentLocationName, Activity activity) {
        FirstLaunchLoadingFragment loadingFragment = new FirstLaunchLoadingFragment();
        Bundle extras = new Bundle();
        extras.putInt(activity.getString(R.string.extras_choosen_localization_option_key),choosenLocalizationOption);
        extras.putInt(activity.getString(R.string.extras_choosen_location_option_key), choosenLocationOption);
        extras.putString(activity.getString(R.string.extras_different_location_name_key), differentLocationName);
        extras.putBoolean(activity.getString(R.string.extras_is_first_launch_key),isFirstLaunch);
        loadingFragment.setArguments(extras);
        return loadingFragment;
    }

    private void getExtras(){
        choosenLocalizationOption=getArguments().getInt(getString(R.string.extras_choosen_localization_option_key), 0);
        choosenLocationOption = getArguments().getInt(getString(R.string.extras_choosen_location_option_key), 0);
        differentLocationName = getArguments().getString(getString(R.string.extras_different_location_name_key), null);
        isFirstLaunch=getArguments().getBoolean(getString(R.string.extras_is_first_launch_key));
    }

    private void getLayoutResources(){
        loadingBar=(ProgressBar)getActivity().findViewById(R.id.first_launch_loading_fragment_progress_circle);
        messageTextView=(TextView)getActivity().findViewById(R.id.first_launch_loading_fragment_message);
        marginView=getActivity().findViewById(R.id.first_launch_loading_fragment_margin_view);
    }

    private void getSharedPreferences(){
        sharedPreferences=SharedPreferencesModifier.getSharedPreferences(getActivity());
    }

    private void downloadLocalization(){
        UsefulFunctions.setViewVisible(loadingBar);
        UsefulFunctions.setViewVisible(messageTextView);
        currentCoordinatesDownloader =new CurrentCoordinatesDownloader(
                getActivity(),
                this,
                localizationFailureDialog,
                permissionDeniedDialog,
                null,
                messageTextView,
                loadingBar,
                choosenLocalizationOption
        );
    }

    private void initializeFirstLaunch(){
        if(choosenLocationOption ==1) {
            downloadLocalization();
            Log.d("option", "option1");
        }
        else {
            location=differentLocationName;
            downloadWeatherData(location);
            Log.d("option", "option2");
        }
    }

    private void initializeNextLaunch(){
        if(!SharedPreferencesModifier.isFirstLocationConstant(getActivity())){
            Log.d("next", "geolokalizacja");
            choosenLocalizationOption=SharedPreferencesModifier.getLocalizationOption(getActivity());
            Log.d("localizationoption", ""+choosenLocalizationOption);
            if(choosenLocalizationOption==0){
                UsefulFunctions.setViewInvisible(loadingBar);
                UsefulFunctions.setViewInvisible(messageTextView);
                localizationOptionsDialog.show();
            }
            else{
                Log.d("laduje", "initializeNextLaunch: ");
                downloadLocalization();
            }
        }
        else {
            Log.d("next", "different");
            location=SharedPreferencesModifier.getFirstLocation(getActivity());
            downloadWeatherData(location);
        }
    }
    private void initializeNextLaunchAfterFailure(){
        isNextLaunchAfterFailure=true;
        if(changedLocation==null){
            if(choosenLocalizationOption==0){
                UsefulFunctions.setViewInvisible(loadingBar);
                UsefulFunctions.setViewInvisible(messageTextView);
                localizationOptionsDialog.show();
            }
            else {
                Log.d("laduje", "initializeNextLaunch: ");
                downloadLocalization();
            }
        }
        else{
            downloadWeatherData(changedLocation);
        }
    }

    private void downloadWeatherData(String location){
        UsefulFunctions.setViewVisible(loadingBar);
        messageTextView.setText(getString(R.string.downloading_weather_data_progress_message));
        UsefulFunctions.setViewVisible(messageTextView);
        new WeatherDataDownloader(location,this);
    }

    @Override
    public void weatherServiceSuccess(Channel channel) {
        dataInitializer = new WeatherDataInitializer(getActivity(),channel); //initializing weather data from JSON
        if(isFirstLaunch && choosenLocationOption ==2){
            locationResultsDialog =dialogInitializer.initializeLocationResultsDialog(1,dataInitializer,loadMainActivityRunnable,showLocationFragmentRunnable,new showExitDialogRunnable(showLocalizationResultDialogRunnable));
            showDialog(locationResultsDialog);
        }
        else loadMainActivityRunnable.run();
    }

    @Override
    public void weatherServiceFailure(int errorCode) {
        //failure handling
        if(errorCode==1) {showDialog(internetFailureWeatherDialog);}
        else{
            if(isFirstLaunch) {
                showDialog(noLocationResultsDialog);
            }
            else {
                if(!SharedPreferencesModifier.isFirstLocationConstant(getActivity())||(changedLocation==null&&isNextLaunchAfterFailure==true)){
                    showDialog(noLocationResultsDialog);
                }
                else{
                    showDialog(serviceFailureDialog);
                }
            }
        }
    }

    @Override
    public void geocodingServiceSuccess(String location) {
        Log.d("location", "geocodingServiceSuccess: ");
        this.location=location;
        downloadWeatherData(this.location);
    }

    @Override
    public void geocodingServiceFailure(int errorCode) {
        Log.d("geocode failure", "geocodingServiceFailure: "+errorCode);
        if(errorCode==1){
            showDialog(internetFailureLocalizationDialog);
        }
        else{
            showDialog(localizationFailureDialog);
        }
    }

    private void initializeDialogs(){
        dialogInitializer=new DialogInitializer(getActivity());
        internetFailureWeatherDialog=dialogInitializer.initializeInternetFailureDialog(true, downloadWeatherRunnable, new showExitDialogRunnable(showInternetFailureWeatherDialogRunnable));
        internetFailureLocalizationDialog =dialogInitializer.initializeInternetFailureDialog(true, downloadLocationRunnable, new showExitDialogRunnable(showInternetFailureLocationDialogRunnable));
        serviceFailureDialog=dialogInitializer.initializeServiceFailureDialog(true,downloadWeatherRunnable,new showExitDialogRunnable(showServiceFailureDialogRunnable));
        if(isFirstLaunch){
            noLocationResultsDialog =dialogInitializer.initializeNoLocationResultsDialog(1,showLocationFragmentRunnable,new showExitDialogRunnable(showNoLocalizationResultDialogRunnable));
            localizationFailureDialog=dialogInitializer.initializeLocalizationFailureDialog(1,showLocationFragmentRunnable,new showExitDialogRunnable(showLocalizationFailureDialogRunnable));
            permissionDeniedDialog=dialogInitializer.initializePermissionDeniedDialog(1,showLocationFragmentRunnable,new showExitDialogRunnable(showPermissionDeniedDialogRunnable));
        }
        else{
            localizationOptionsDialog=dialogInitializer.initializeLocalizationOptionsDialog(localizationOptionsDialogRunnable);
            noLocationResultsDialog =dialogInitializer.initializeNoLocationResultsDialog(1, showChangeLocationAfterFailureDialogRunnable,new showExitDialogRunnable(showNoLocalizationResultDialogRunnable));
            localizationFailureDialog =dialogInitializer.initializeLocalizationFailureDialog(1,showChangeLocationAfterFailureDialogRunnable, new showExitDialogRunnable(showLocalizationFailureDialogRunnable));
            permissionDeniedDialog =dialogInitializer.initializePermissionDeniedDialog(1,showChangeLocationAfterFailureDialogRunnable, new showExitDialogRunnable(showPermissionDeniedDialogRunnable));
        }
    }

    private Runnable showChangeLocationAfterFailureDialogRunnable =new Runnable() {
        @Override
        public void run() {
            changeLocationAfterFailureDialog =dialogInitializer.initializeFavouritesDialog(1, changeLocationAfterFailureRunnable,new showExitDialogRunnable(showChangeLocationAfterFailureDialogRunnable));
            showDialog(changeLocationAfterFailureDialog);
        }

    };
    private Runnable changeLocationAfterFailureRunnable = new Runnable() {
        public void run() {
            int choosenLocationID=FavouritesEditor.getChoosenLocationID();
            int numberOfFavourites=FavouritesEditor.getNumberOfFavourites(getActivity());
            if(choosenLocationID==numberOfFavourites) changedLocation=null;
            else changedLocation=FavouritesEditor.getChoosenFavouriteLocationAddress(getActivity());
            initializeNextLaunchAfterFailure();
        }
    };
    private Runnable localizationOptionsDialogRunnable = new Runnable() {
        public void run() {
            if(isNextLaunchAfterFailure) initializeNextLaunchAfterFailure();
            else initializeNextLaunch();
        }
    };

    private Runnable showInternetFailureWeatherDialogRunnable = new Runnable() {
        public void run() {internetFailureWeatherDialog.show();}
    };
    private Runnable showInternetFailureLocationDialogRunnable = new Runnable() {
        public void run() {internetFailureLocalizationDialog.show();}
    };
    private Runnable showServiceFailureDialogRunnable = new Runnable() {public void run() {serviceFailureDialog.show();}};
    private Runnable showLocalizationResultDialogRunnable = new Runnable() {
        public void run() {locationResultsDialog.show();}
    };
    private Runnable showLocalizationFailureDialogRunnable = new Runnable() {public void run() {localizationFailureDialog.show();}};
    private Runnable showPermissionDeniedDialogRunnable = new Runnable() {public void run() {permissionDeniedDialog.show();}};
    private Runnable showNoLocalizationResultDialogRunnable = new Runnable() {public void run() {noLocationResultsDialog.show();}};
    private Runnable showLocationFragmentRunnable = new Runnable() {
        public void run() {locationListener.showLocationFragment();}
    };
    private class showExitDialogRunnable implements Runnable {
        private Runnable showDialogRunnable;
        public showExitDialogRunnable(Runnable showDialogRunnable) {
            this.showDialogRunnable = showDialogRunnable;
        }
        public void run() {
            AlertDialog exitDialog=dialogInitializer.initializeExitDialog(true, showDialogRunnable);
            exitDialog.show();
        }
    }
    private void showDialog(AlertDialog alertDialog){
        alertDialog.show();
        UsefulFunctions.setViewInvisible(messageTextView);
        UsefulFunctions.setViewGone(loadingBar);
    }
    private Runnable downloadWeatherRunnable = new Runnable() {
        public void run() {
            downloadWeatherData(location);}
    };
    private Runnable downloadLocationRunnable = new Runnable() {
        public void run() {
            GeocodingDownloader geocodingDownloader=new GeocodingDownloader(currentCoordinatesDownloader.getLocation(),FirstLaunchLoadingFragment.this,messageTextView,getActivity());
            UsefulFunctions.setViewVisible(messageTextView);
            UsefulFunctions.setViewVisible(loadingBar);
        }
    };
    private Runnable loadMainActivityRunnable = new Runnable() {
        public void run() {
            messageTextView.setText(getString(R.string.loading_content_progress_message));
            UsefulFunctions.setViewGone(loadingBar);
            UsefulFunctions.setViewVisible(marginView);
            UsefulFunctions.setViewVisible(messageTextView);
            if(isFirstLaunch) {
                if(choosenLocationOption==1){
                    Log.d("end", ""+choosenLocationOption);
                    SharedPreferencesModifier.setLocalizationOption(getActivity(),choosenLocalizationOption);
                    SharedPreferencesModifier.setFirstLocationGeolocalization(getActivity());
                }
                else{
                    Log.d("end", ""+choosenLocationOption);
                    Log.d("firstlocation", ""+differentLocationName);
                    String city=dataInitializer.getCity();
                    String region=dataInitializer.getRegion();
                    String country=dataInitializer.getCountry();
                    String locationName=city+", "+region+", "+country;
                    SharedPreferencesModifier.setFirstLocationConstant(getActivity(),locationName);
                    saveNewFavouritesItem();

                }
                SharedPreferencesModifier.setNextLaunch(getActivity());
            }
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(getString(R.string.extras_data_initializer_key),dataInitializer);
            startActivity(intent);
            getActivity().finish();
            return;
        }
    };

    private void saveNewFavouritesItem(){
        String currentLocationCity=dataInitializer.getCity();
        String currentLocationRegion=dataInitializer.getRegion();
        String currentLocationCountry=dataInitializer.getCountry();
        String currentLocationAddress=currentLocationCity+", "+currentLocationRegion+", "+currentLocationCountry;
        String header=currentLocationCity;
        String subheader=currentLocationRegion+", "+currentLocationCountry;
        String currentLocationLatitude= Double.toString(dataInitializer.getLatitude());
        String currentLocationLongitude=Double.toString(dataInitializer.getLongitude());
        String currentLocationCoordinates=currentLocationLatitude+"%"+currentLocationLongitude;
        FavouritesEditor.saveNewFavouritesItem(getActivity(),header,subheader,currentLocationAddress,currentLocationCoordinates);
    }

    public interface ChooseLocationAgainListener {void showLocationFragment();}
}
