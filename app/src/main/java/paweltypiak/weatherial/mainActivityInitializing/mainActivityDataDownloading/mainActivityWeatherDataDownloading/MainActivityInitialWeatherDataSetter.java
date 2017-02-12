/*******************************************************************************
 * Copyright 2017 Paweł Typiak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package paweltypiak.weatherial.mainActivityInitializing.mainActivityDataDownloading.mainActivityWeatherDataDownloading;

import android.app.Activity;
import android.os.Bundle;

import paweltypiak.weatherial.R;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.weatherial.utils.SharedPreferencesModifier;
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
