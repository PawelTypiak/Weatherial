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
package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.weatherLayoutInitializing;

import android.app.Activity;

import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.weatherLayoutInitializing.swipeRefreshLayoutInitializing.SwipeRefreshLayoutInitializer;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataFormatter;

public class WeatherLayoutInitializer {

    private Activity activity;
    private GeneralWeatherLayoutInitializer generalWeatherLayoutInitializer;
    private SwipeRefreshLayoutInitializer swipeRefreshLayoutInitializer;
    private WeatherBasicInfoLayoutInitializer weatherBasicInfoLayoutInitializer;
    private WeatherDetailsLayoutInitializer weatherDetailsLayoutInitializer;
    private WeatherForecastLayoutInitializer weatherForecastLayoutInitializer;

    public WeatherLayoutInitializer(Activity activity,
                                    MainActivityLayoutInitializer mainActivityLayoutInitializer){
        this.activity = activity;
        initializeGeneralWeatherLayout(activity,mainActivityLayoutInitializer);
        initializeSwipeRefreshLayout(activity, mainActivityLayoutInitializer);
        initializeWeatherGeneralInfoLayout(activity, mainActivityLayoutInitializer);
        initializeWeatherDetailsLayout(activity);
        initializeWeatherForecastLayout(activity);
    }

    private void initializeGeneralWeatherLayout(Activity activity,
                                                MainActivityLayoutInitializer mainActivityLayoutInitializer){
        generalWeatherLayoutInitializer=new GeneralWeatherLayoutInitializer(
                activity,
                mainActivityLayoutInitializer);
    }

    private void initializeSwipeRefreshLayout(Activity activity,
                                              MainActivityLayoutInitializer mainActivityLayoutInitializer) {
        swipeRefreshLayoutInitializer = new SwipeRefreshLayoutInitializer(
                activity,
                mainActivityLayoutInitializer,this);
    }

    private void initializeWeatherGeneralInfoLayout(Activity activity, MainActivityLayoutInitializer
            mainActivityLayoutInitializer) {
        weatherBasicInfoLayoutInitializer = new WeatherBasicInfoLayoutInitializer(activity,
                mainActivityLayoutInitializer,this);
    }

    private void initializeWeatherDetailsLayout(Activity activity) {
        weatherDetailsLayoutInitializer = new WeatherDetailsLayoutInitializer(activity);
    }

    private void initializeWeatherForecastLayout(Activity activity) {
        weatherForecastLayoutInitializer = new WeatherForecastLayoutInitializer(activity);
    }

    public void updateWeatherLayoutData(WeatherDataFormatter weatherDataFormatter) {
        updateWeatherGeneralInfoLayoutData(weatherDataFormatter);
        updateWeatherDetailsLayoutData(weatherDataFormatter);
        updateWeatherForecastLayoutData(weatherDataFormatter);
    }

    private void updateWeatherGeneralInfoLayoutData(WeatherDataFormatter weatherDataFormatter) {
        weatherBasicInfoLayoutInitializer.updateWeatherBasicInfoLayoutData(activity, weatherDataFormatter, this);
    }

    private void updateWeatherDetailsLayoutData(WeatherDataFormatter weatherDataFormatter) {
        weatherDetailsLayoutInitializer.updateWeatherDetailsLayoutData(activity, weatherDataFormatter);
    }

    private void updateWeatherForecastLayoutData(WeatherDataFormatter weatherDataFormatter) {
        weatherForecastLayoutInitializer.updateWeatherForecastLayoutData(activity, weatherDataFormatter);
    }

    public void updateWeatherLayoutTheme(boolean isDay) {
        WeatherLayoutThemeColorsUpdater themeColorsUpdater = new WeatherLayoutThemeColorsUpdater(activity, isDay);
        updateWeatherGeneralInfoLayoutTheme(themeColorsUpdater);
        updateWeatherDetailsLayoutTheme(activity, themeColorsUpdater, isDay);
        updateWeatherForecastLayoutTheme(activity, themeColorsUpdater);
    }

    private void updateWeatherGeneralInfoLayoutTheme(WeatherLayoutThemeColorsUpdater themeColorsUpdater) {
        weatherBasicInfoLayoutInitializer.updateWeatherBasicInfoLayoutTheme(activity, themeColorsUpdater);
    }

    private void updateWeatherDetailsLayoutTheme(Activity activity, WeatherLayoutThemeColorsUpdater
            themeColorsUpdater, boolean isDay) {
        weatherDetailsLayoutInitializer.updateWeatherDetailsLayoutTheme(activity, themeColorsUpdater, isDay);
    }

    private void updateWeatherForecastLayoutTheme(Activity activity, WeatherLayoutThemeColorsUpdater
            themeColorsUpdater) {
        weatherForecastLayoutInitializer.updateWeatherForecastLayoutTheme(activity, themeColorsUpdater);
    }

    public GeneralWeatherLayoutInitializer getGeneralWeatherLayoutInitializer() {
        return generalWeatherLayoutInitializer;
    }

    public WeatherDetailsLayoutInitializer getWeatherDetailsLayoutInitializer() {
        return weatherDetailsLayoutInitializer;
    }

    public SwipeRefreshLayoutInitializer getSwipeRefreshLayoutInitializer() {
        return swipeRefreshLayoutInitializer;
    }
}
