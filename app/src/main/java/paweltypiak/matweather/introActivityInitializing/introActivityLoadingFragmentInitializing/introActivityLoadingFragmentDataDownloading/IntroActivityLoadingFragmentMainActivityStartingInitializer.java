package paweltypiak.matweather.introActivityInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;
import paweltypiak.matweather.R;
import paweltypiak.matweather.mainActivityInitializing.MainActivity;
import paweltypiak.matweather.usefulClasses.FavouritesEditor;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;
import paweltypiak.matweather.dataDownloading.weatherDataDownloading.WeatherDataParser;

public class IntroActivityLoadingFragmentMainActivityStartingInitializer {

    public IntroActivityLoadingFragmentMainActivityStartingInitializer(Activity activity,
                                                                       IntroActivityLoadingFragmentDataDownloader dataDownloader,
                                                                       WeatherDataParser weatherDataParser){
        updateLayout(activity,dataDownloader);
        if(dataDownloader.isFirstLaunch()) {
            saveDefeaultLocationData(activity,dataDownloader,weatherDataParser);
        }
        startMainActivity(activity,weatherDataParser);
    }

    private static void updateLayout(Activity activity,
                              IntroActivityLoadingFragmentDataDownloader dataDownloader){
        TextView messageTextView=dataDownloader.getMessageTextView();
        messageTextView.setText(activity.getString(R.string.loading_content_progress_message));
        UsefulFunctions.setViewVisible(messageTextView);
        UsefulFunctions.setViewGone(dataDownloader.getLoadingProgressBar());
        UsefulFunctions.setViewVisible(dataDownloader.getMarginView());
    }

    private static void saveDefeaultLocationData(Activity activity,
                                          IntroActivityLoadingFragmentDataDownloader dataDownloader,
                                          WeatherDataParser weatherDataParser){
        int selectedDefeaultLocationOption=dataDownloader.getSelectedDefeaultLocationOption();
        if(selectedDefeaultLocationOption ==0){
            saveDataForGeolocalization(activity,dataDownloader);
        }
        else{
            saveDataForDifferentLocation(activity,weatherDataParser);
        }
        SharedPreferencesModifier.setNextLaunch(activity);
    }

    private static void saveDataForGeolocalization(Activity activity,
                                            IntroActivityLoadingFragmentDataDownloader dataDownloader){
        SharedPreferencesModifier.setGeolocalizationMethod(activity, dataDownloader.getSelectedDefeaultLocalizationMethod());
        SharedPreferencesModifier.setDefeaultLocationGeolocalization(activity);
    }

    private static void saveDataForDifferentLocation(Activity activity,
                                              WeatherDataParser weatherDataParser){
        String[] locationAddress=getDownloadedLocationAddress(weatherDataParser);
        setDefeaultLocationAddress(activity,locationAddress);
        saveNewFavouritesItem(activity,locationAddress);
    }

    private static String[] getDownloadedLocationAddress(WeatherDataParser weatherDataParser){
        String city=weatherDataParser.getCity();
        String region=weatherDataParser.getRegion();
        String country=weatherDataParser.getCountry();
        String[] locationAddress={city,region,country};
        return locationAddress;
    }

    private static void setDefeaultLocationAddress(Activity activity,
                                            String[] locationAddress){
        String city=locationAddress[0];
        String region=locationAddress[1];
        String country=locationAddress[2];
        String locationAddressString=city+", "+region+", "+country;
        SharedPreferencesModifier.setDefeaultLocationConstant(activity,locationAddressString);
    }

    private static void saveNewFavouritesItem(Activity activity,
                                       String[] locationAddress){
        //saving selected location in favourites
        String city=locationAddress[0];
        String region=locationAddress[1];
        String country=locationAddress[2];
        String locationNameTitle=city;
        String locationNameSubtitle=region+", "+country;
        String locationAddressString=locationNameTitle+", "+locationNameSubtitle;
        FavouritesEditor.saveNewFavouritesItem(activity,locationNameTitle,locationNameSubtitle,locationAddressString);
    }

    private static void startMainActivity(Activity activity,
                                   WeatherDataParser weatherDataParser){
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(activity.getString(R.string.extras_data_initializer_key),weatherDataParser);
        activity.startActivity(intent);
        activity.finish();
    }
}
