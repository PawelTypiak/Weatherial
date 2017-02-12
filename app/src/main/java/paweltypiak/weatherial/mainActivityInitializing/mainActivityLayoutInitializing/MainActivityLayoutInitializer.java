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
package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing;

import android.app.Activity;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.layoutUpdating.OnTimeChangeLayoutUpdater;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.layoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.weatherLayoutInitializing.WeatherLayoutInitializer;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.AppBarLayoutInitializer;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataParser;

public class MainActivityLayoutInitializer {

    private OnTimeChangeLayoutUpdater onTimeChangeLayoutUpdater;
    private AppBarLayoutInitializer appBarLayoutInitializer;
    private WeatherLayoutInitializer weatherLayoutInitializer;
    private OnWeatherDataChangeLayoutUpdater onWeatherDataChangeLayoutUpdater;

    public MainActivityLayoutInitializer(Activity activity){
        initializeAppBarLayout(activity);
        initializeWeatherLayout(activity);
        initializeOnTimeChangeLayoutUpdater(activity);
    }

    private void initializeAppBarLayout(Activity activity){
        appBarLayoutInitializer=new AppBarLayoutInitializer(activity);
    }

    private void initializeWeatherLayout(Activity activity){
        weatherLayoutInitializer=new WeatherLayoutInitializer(
                activity,
                this);
    }

    private void initializeOnTimeChangeLayoutUpdater(Activity activity){
        onTimeChangeLayoutUpdater=new OnTimeChangeLayoutUpdater(
                activity,
                this);
    }

    public void updateLayoutOnWeatherDataChange(final Activity activity,
                                                final WeatherDataParser weatherDataParser,
                                                final boolean doSetAppBar,
                                                final boolean isGeolocalizationMode){
        onWeatherDataChangeLayoutUpdater =new OnWeatherDataChangeLayoutUpdater(
                activity,
                this,
                weatherDataParser,
                doSetAppBar,
                isGeolocalizationMode);
    }

    public AppBarLayoutInitializer getAppBarLayoutInitializer() {
        return appBarLayoutInitializer;
    }

    public WeatherLayoutInitializer getWeatherLayoutInitializer() {
        return weatherLayoutInitializer;
    }

    public OnTimeChangeLayoutUpdater getOnTimeChangeLayoutUpdater() {
        return onTimeChangeLayoutUpdater;
    }

    public OnWeatherDataChangeLayoutUpdater getOnWeatherDataChangeLayoutUpdater() {
        return onWeatherDataChangeLayoutUpdater;
    }
}
