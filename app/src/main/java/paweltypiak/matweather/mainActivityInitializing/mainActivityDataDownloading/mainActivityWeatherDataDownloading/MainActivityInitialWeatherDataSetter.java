package paweltypiak.matweather.mainActivityInitializing.mainActivityDataDownloading.mainActivityWeatherDataDownloading;

import android.app.Activity;
import android.os.Bundle;

import paweltypiak.matweather.R;
import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.dataDownloading.weatherDataDownloading.WeatherDataParser;

public class MainActivityInitialWeatherDataSetter {

    public static void setInitialWeatherData(Activity activity,
                                             Bundle savedInstanceState,
                                             MainActivityLayoutInitializer layoutInitializer){
        WeatherDataParser weatherDataParser=getInitialWeatherData(activity,savedInstanceState);
        updateLayoutForDefaultLocation(activity,layoutInitializer,weatherDataParser);
    }


    private static WeatherDataParser getInitialWeatherData(Activity activity,Bundle savedInstanceState){
        //loading initial weather data
        WeatherDataParser weatherDataParser=null;
        if(savedInstanceState==null){
            Bundle extras = activity.getIntent().getExtras();
            if (extras != null) {
                //after launch
                weatherDataParser =extras.getParcelable(activity.getString(R.string.extras_data_initializer_key));
            }
        }
        else {
            //after recreate
            weatherDataParser =  savedInstanceState.getParcelable(activity.getString(R.string.extras_data_initializer_key));
        }
        return weatherDataParser;
    }

    private static void updateLayoutForDefaultLocation(Activity activity,
                                                       MainActivityLayoutInitializer layoutInitializer,
                                                       WeatherDataParser weatherDataParser){
        //delivering information if defeault location is constant, or is from geolocalization
        boolean isDefeaultLocationConstant= SharedPreferencesModifier.isDefaultLocationConstant(activity);
        if(isDefeaultLocationConstant) {
            layoutInitializer.updateLayoutOnWeatherDataChange(activity,weatherDataParser,true,false);
        }
        else{
            layoutInitializer.updateLayoutOnWeatherDataChange(activity,weatherDataParser,true,true);
        }
    }
}
