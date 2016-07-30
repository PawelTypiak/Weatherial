package paweltypiak.matweather.firstLaunching;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import paweltypiak.matweather.DialogInitializer;
import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.R;
import paweltypiak.matweather.UsefulFunctions;
import paweltypiak.matweather.jsonHandling.Geocoding;
import paweltypiak.matweather.localizationDataDownloading.GeocodingCallback;
import paweltypiak.matweather.localizationDataDownloading.LocalizationDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDownloadCallback;
import paweltypiak.matweather.weatherDataProcessing.WeatherDataInitializer;
import paweltypiak.matweather.jsonHandling.Channel;
import paweltypiak.matweather.weatherDataProcessing.WeatherDataSetter;

public class FirstLaunchLoadingFragment extends Fragment implements WeatherDownloadCallback,GeocodingCallback{
    private int choosenOption;
    private String differentLocationName;
    private DialogInitializer dialogInitializer;
    private AlertDialog noLocalizationResultsDialog;
    private AlertDialog localizationResultsDialog;
    private AlertDialog internetFailureDialog;
    private AlertDialog serviceFailureDialog;
    private AlertDialog localizationFailureDialog;
    private AlertDialog permissionDeniedDialog;
    private AlertDialog firstLaunchLocalizationFailureDialog;
    private AlertDialog firstLaunchPermissionDeniedDialog;
    private WeatherDataInitializer dataInitializer;
    private String location;
    private ProgressBar loadingBar;
    private TextView messageTextView;
    private View marginView;
    private ChooseLocationAgainListener locationListener;
    private boolean isFirstLaunch;
    private LocalizationDownloader localizationDownloader;


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
        if(isFirstLaunch) initializeFirstLaunch();
        else initializeNextLaunch();

    }

    public static FirstLaunchLoadingFragment newInstance(boolean isFirstLaunch, int choosenOption, String differentLocationName, Activity activity) {
        FirstLaunchLoadingFragment loadingFragment = new FirstLaunchLoadingFragment();
        Bundle extras = new Bundle();
        extras.putInt(activity.getString(R.string.extras_choosen_location_option_key), choosenOption);
        extras.putString(activity.getString(R.string.extras_different_location_name_key), differentLocationName);
        extras.putBoolean(activity.getString(R.string.extras_is_first_launch_key),isFirstLaunch);
        loadingFragment.setArguments(extras);
        return loadingFragment;
    }

    private void getExtras(){
        choosenOption = getArguments().getInt(getString(R.string.extras_choosen_location_option_key), 0);
        differentLocationName = getArguments().getString(getString(R.string.extras_different_location_name_key), null);
        isFirstLaunch=getArguments().getBoolean(getString(R.string.extras_is_first_launch_key));
    }

    private void getLayoutResources(){
        loadingBar=(ProgressBar)getActivity().findViewById(R.id.first_launch_loading_fragment_progress_circle);
        messageTextView=(TextView)getActivity().findViewById(R.id.first_launch_loading_fragment_message);
        marginView=getActivity().findViewById(R.id.first_launch_loading_fragment_margin_view);
    }

    private void initializeFirstLaunch(){

        if(choosenOption==1) {
            messageTextView.setText(R.string.first_launch_layout_loading_header_searching_localization);
            localizationDownloader=new LocalizationDownloader(
                    getActivity(),
                    this,
                    firstLaunchLocalizationFailureDialog,
                    firstLaunchPermissionDeniedDialog,
                    messageTextView,
                    loadingBar
                    );
            Log.d("option", "option1");
        }
        else {
            location=differentLocationName;
            downloadWeatherData(location);
            Log.d("option", "option2");
        }

    }

    private void initializeNextLaunch(){

        String firstLocation=UsefulFunctions.getSharedPreferences(getActivity()).getString(getString(R.string.shared_preferences_first_location_key),"0");
        if(firstLocation.equals("1")){}// getCurrentLocation();
        else {
            location=UsefulFunctions.getSharedPreferences(getActivity()).getString(getString(R.string.shared_preferences_first_location_key),"0");
            downloadWeatherData(location);
        }
    }



    private void downloadWeatherData(String location){
        UsefulFunctions.setViewVisible(loadingBar);
        if(isFirstLaunch==true) messageTextView.setText(getString(R.string.first_launch_layout_loading_header_searching_localization));
        else messageTextView.setText(getString(R.string.first_launch_layout_loading_header_downloading_data));
        UsefulFunctions.setViewVisible(messageTextView);
        WeatherDataDownloader downloader=new WeatherDataDownloader(location,this);   //downloading weather data for Poznan
    }

    @Override
    public void ServiceSuccess(Channel channel) {
        dataInitializer = new WeatherDataInitializer(getActivity(),channel); //initializing weather data from JSON
        UsefulFunctions.setViewGone(loadingBar);
        if(isFirstLaunch && choosenOption==2){
            localizationResultsDialog=dialogInitializer.initializeLocalizationResultsDialog(1,dataInitializer,loadMainActivityRunnable,showLocationFragmentRunnable,showExitLocalizationResultDialogRunnable);
            localizationResultsDialog.show();

            UsefulFunctions.setViewInvisible(messageTextView);
        }
        else loadMainActivityRunnable.run();
    }

    @Override
    public void ServiceFailure(int errorCode) {
        //failure handling
        UsefulFunctions.setViewInvisible(loadingBar);
        UsefulFunctions.setViewInvisible(messageTextView);
        if(errorCode==1) {internetFailureDialog.show();}
        else{
            if(isFirstLaunch) noLocalizationResultsDialog.show();
            else {serviceFailureDialog.show();} //servisfailure
        }
    }

    @Override
    public void geocodeSuccess(Geocoding location) {

        downloadWeatherData(location.getAddress());
    }

    @Override
    public void geocodeFailure(Exception exception) {
        // GeoCoding failed, try loading weather data from the cache
        Log.d("location", "geocodeFailure: ");

    }

   /* @Override
    public void geocodeSuccess(LocationResult location) {
        // completed geocoding successfully
        weatherService.refreshWeather(location.getAddress());
        Log.d("location", location.getAddress());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.pref_cached_location), location.getAddress());
        editor.apply();
    }

    @Override
    public void geocodeFailure(Exception exception) {
        // GeoCoding failed, try loading weather data from the cache
        Log.d("location", "geocodeFailure: ");
        cacheService.load(this);
    }*/



    private void initializeDialogs(){
        dialogInitializer=new DialogInitializer(getActivity());
        noLocalizationResultsDialog=dialogInitializer.initializeNoLocalizationResultsDialog(1,showLocationFragmentRunnable,showExitNoLocalizationResultDialogRunnable);
        internetFailureDialog=dialogInitializer.initializeInternetFailureDialog(true,downloadRunnable,showExitInternetFailureDialogRunnable);
        serviceFailureDialog=dialogInitializer.initializeServiceFailureDialog(downloadRunnable,showExitServiceFailureDialogRunnable);
        localizationFailureDialog=dialogInitializer.initializeLocalizationFailureDialog(tmp,showExitLocalizationFailureDialogRunnable);
        firstLaunchLocalizationFailureDialog=dialogInitializer.initializeLocalizationFailureDialog(showLocationFragmentRunnable,showExitFirstLaunchLocalizationFailureDialogRunnable);
        permissionDeniedDialog=dialogInitializer.initializePermissionDeniedDialog(tmp,showExitPermissionDeniedDialogRunnable);
        firstLaunchPermissionDeniedDialog=dialogInitializer.initializePermissionDeniedDialog(showLocationFragmentRunnable,showExitFirstLaunchPermissionDeniedDialogRunnable);

    }


    private Runnable tmp=new Runnable() {
        @Override
        public void run() {
            Log.d("lista", "lista");
        }

    };

    private Runnable showExitLocalizationFailureDialogRunnable=new Runnable() {
        @Override
        public void run() {
            AlertDialog exitLocalizationFailureDialog=dialogInitializer.initializeExitDialog(true,showLocalizationFailureDialogRunnable);
            exitLocalizationFailureDialog.show();
        }

    };

    private Runnable showExitPermissionDeniedDialogRunnable=new Runnable() {
        @Override
        public void run() {
            AlertDialog exitPermissionDeniedDialog=dialogInitializer.initializeExitDialog(true,showPermissionDeniedDialogRunnable);
            exitPermissionDeniedDialog.show();
        }

    };

    private Runnable showExitFirstLaunchLocalizationFailureDialogRunnable=new Runnable() {
        @Override
        public void run() {
            AlertDialog exitFirstLaunchLocalizationFailureDialog=dialogInitializer.initializeExitDialog(true,showFirstLaunchLocalizationFailureDialogRunnable);
            exitFirstLaunchLocalizationFailureDialog.show();
        }

    };

    private Runnable showExitFirstLaunchPermissionDeniedDialogRunnable=new Runnable() {
        @Override
        public void run() {
            AlertDialog exitFirstLaunchPermissionDeniedDialog=dialogInitializer.initializeExitDialog(true,showFirstLaunchPermissionDeniedDialogRunnable);
            exitFirstLaunchPermissionDeniedDialog.show();
        }
    };

    private Runnable showExitNoLocalizationResultDialogRunnable = new Runnable() {
        public void run() {{
            AlertDialog exitNoLocalizationResultDialog=dialogInitializer.initializeExitDialog(true,showNoLocalizationResultDialogRunnable);
            exitNoLocalizationResultDialog.show();
        }}
    };

    private Runnable showExitLocalizationResultDialogRunnable = new Runnable() {
        public void run() {
            AlertDialog exitLocalizationResultDialog=dialogInitializer.initializeExitDialog(true,showLocalizationResultDialogRunnable);
            exitLocalizationResultDialog.show();}
    };

    private Runnable showExitInternetFailureDialogRunnable = new Runnable() {
        public void run() {
            AlertDialog exitInternetFailureDialog=dialogInitializer.initializeExitDialog(true,showInternetFailureDialogRunnable);
            exitInternetFailureDialog.show();
        }
    };

    private Runnable showExitServiceFailureDialogRunnable = new Runnable() {
        public void run() {
            AlertDialog exitServiceFailureDialog=dialogInitializer.initializeExitDialog(true,showServiceFailureDialogRunnable);
            exitServiceFailureDialog.show();}
    };

    private Runnable showInternetFailureDialogRunnable = new Runnable() {
        public void run() {internetFailureDialog.show();}
    };
    private Runnable showServiceFailureDialogRunnable = new Runnable() {public void run() {serviceFailureDialog.show();}};
    private Runnable showLocalizationResultDialogRunnable = new Runnable() {
        public void run() {localizationResultsDialog.show();}
    };
    private Runnable showLocalizationFailureDialogRunnable = new Runnable() {public void run() {localizationFailureDialog.show();}};
    private Runnable showPermissionDeniedDialogRunnable = new Runnable() {public void run() {permissionDeniedDialog.show();}};
    private Runnable showFirstLaunchLocalizationFailureDialogRunnable = new Runnable() {public void run() {localizationFailureDialog.show();}};
    private Runnable showFirstLaunchPermissionDeniedDialogRunnable = new Runnable() {public void run() {permissionDeniedDialog.show();}};
    private Runnable showNoLocalizationResultDialogRunnable = new Runnable() {public void run() {noLocalizationResultsDialog.show();}};

    private Runnable loadMainActivityRunnable = new Runnable() {
        public void run() {
            //UsefulFunctions.setViewVisible(loadingBar);
            UsefulFunctions.setViewVisible(marginView);
            messageTextView.setText(getString(R.string.first_launch_layout_loading_header_loading_content));
            if(isFirstLaunch&&choosenOption==2) {
                SharedPreferences sharedPreferences=UsefulFunctions.getSharedPreferences(getActivity());
                sharedPreferences.edit().putString(getString(R.string.shared_preferences_first_location_key),differentLocationName).commit();

                UsefulFunctions.setViewVisible(messageTextView);
            }
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(getString(R.string.extras_data_initializer_key),dataInitializer);
            startActivity(intent);
            getActivity().finish();
            return;
        }
    };

    private Runnable downloadRunnable = new Runnable() {
        public void run() {
            downloadWeatherData(location);}
    };
    private Runnable showLocationFragmentRunnable = new Runnable() {
        public void run() {locationListener.showLocationFragment();}
    };
    public interface ChooseLocationAgainListener {void showLocationFragment();}
}
