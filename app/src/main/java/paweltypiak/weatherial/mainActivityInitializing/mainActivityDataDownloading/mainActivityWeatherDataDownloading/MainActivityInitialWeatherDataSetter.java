package paweltypiak.weatherial.mainActivityInitializing.mainActivityDataDownloading.mainActivityWeatherDataDownloading;

import android.app.Activity;
import android.os.Bundle;

import paweltypiak.weatherial.R;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.weatherial.usefulClasses.SharedPreferencesModifier;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataParser;

class MainActivityInitialWeatherDataSetter {

    static void setInitialWeatherData(Activity activity,
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
        //delivering information if default location is constant, or is from geolocalization
        boolean isDefaultLocationConstant= SharedPreferencesModifier.isDefaultLocationConstant(activity);
        if(isDefaultLocationConstant) {
            layoutInitializer.updateLayoutOnWeatherDataChange(activity,weatherDataParser,true,false);
        }
        else{
            layoutInitializer.updateLayoutOnWeatherDataChange(activity,weatherDataParser,true,true);
        }
    }
}
