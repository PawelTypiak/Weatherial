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
package paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.WeatherResultsForLocationDialogInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentDataDownloader;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentMainActivityStartingInitializer;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataParser;

public class IntroActivityLoadingFragmentWeatherResultsForLocationDialogInitializer {

    private Activity activity;
    private IntroActivityLoadingFragmentDataDownloader dataDownloader;
    private WeatherDataParser weatherDataParser;
    private AlertDialog weatherResultsForLocationDialog;

    public IntroActivityLoadingFragmentWeatherResultsForLocationDialogInitializer(Activity activity,
                                                                                  IntroActivityLoadingFragmentDataDownloader dataDownloader,
                                                                                  WeatherDataParser weatherDataParser){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
        this.weatherDataParser=weatherDataParser;
        weatherResultsForLocationDialog=initializeWeatherResultsForLocationDialog();
    }

    public AlertDialog getWeatherResultsForLocationDialog() {
        return weatherResultsForLocationDialog;
    }

    private AlertDialog initializeWeatherResultsForLocationDialog(){
        AlertDialog weatherResultsForLocationDialog
                = WeatherResultsForLocationDialogInitializer.getWeatherResultsForLocationDialog(
                activity,
                0,
                weatherDataParser,
                positiveButtonRunnable,
                getNeutralButtonRunnable(),
                getNegativeButtonRunnable()
        );
        return weatherResultsForLocationDialog;
    }

    private Runnable positiveButtonRunnable = new Runnable() {
        public void run() {
            new IntroActivityLoadingFragmentMainActivityStartingInitializer(
                    activity,
                    dataDownloader,
                    weatherDataParser
            );
            }
    };

    private Runnable getNeutralButtonRunnable(){
        return new ShowLocationFragmentRunnableInitializer(dataDownloader).
                getShowLocationFragmentRunnable();
    }

    private Runnable getNegativeButtonRunnable(){
        return new ShowLoadingFragmentExitDialogRunnable(activity,showWeatherResultsForLocationDialogRunnable);
    }

    private Runnable showWeatherResultsForLocationDialogRunnable = new Runnable() {
        public void run() {weatherResultsForLocationDialog.show();}
    };
}
