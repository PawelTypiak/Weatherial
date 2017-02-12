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
package paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing.IntroActivityLoadingFragmentNoWeatherResultsForLocationDialogInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing.IntroActivityLoadingFragmentWeatherInternetFailureDialogInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing.IntroActivityLoadingFragmentWeatherResultsForLocationDialogInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing.IntroActivityLoadingFragmentWeatherServiceFailureDialogInitializer;
import paweltypiak.weatherial.dataDownloading.jsonHandling.weatherServiceTagTreeInitializing.Channel;
import paweltypiak.weatherial.utils.SharedPreferencesModifier;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataParser;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDownloadCallback;

public class IntroActivityLoadingFragmentWeatherDataDownloader implements WeatherDownloadCallback {

    private Activity activity;
    private AlertDialog weatherInternetFailureDialog;
    private AlertDialog weatherServiceFailureDialog;
    private AlertDialog noWeatherResultsForLocationDialog;
    private IntroActivityLoadingFragmentDataDownloader dataDownloader;

    public IntroActivityLoadingFragmentWeatherDataDownloader(Activity activity,
                                                             IntroActivityLoadingFragmentDataDownloader dataDownloader){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
        initializeDialogs();
    }

    private void initializeDialogs(){
        getWeatherInternetFailureDialog();
        getWeatherServiceFailureDialog();
        getNoWeatherResultsForLocationDialog();
    }

    private void getWeatherInternetFailureDialog(){
        IntroActivityLoadingFragmentWeatherInternetFailureDialogInitializer weatherInternetFailureDialogInitializer
                =new IntroActivityLoadingFragmentWeatherInternetFailureDialogInitializer(activity,dataDownloader);
        weatherInternetFailureDialog=weatherInternetFailureDialogInitializer.getWeatherInternetFailureDialog();
    }

    private void getWeatherServiceFailureDialog(){
        IntroActivityLoadingFragmentWeatherServiceFailureDialogInitializer weatherServiceFailureDialogInitializer
                =new IntroActivityLoadingFragmentWeatherServiceFailureDialogInitializer(activity,dataDownloader);
        weatherServiceFailureDialog=weatherServiceFailureDialogInitializer.getWeatherServiceFailureDialog();
    }

    private void getNoWeatherResultsForLocationDialog(){
        IntroActivityLoadingFragmentNoWeatherResultsForLocationDialogInitializer noWeatherResultsForLocationDialogInitializer
                =new IntroActivityLoadingFragmentNoWeatherResultsForLocationDialogInitializer(activity,dataDownloader);
        noWeatherResultsForLocationDialog=noWeatherResultsForLocationDialogInitializer.getNoWeatherResultsForLocationDialog();
    }

    public void downloadWeatherData(String location){
        TextView messageTextView=dataDownloader.getMessageTextView();
        if(dataDownloader.isFirstLaunch()&& dataDownloader.getSelectedDefaultLocationOption() ==1) {
            messageTextView.setText(activity.getString(R.string.retrieving_location_progress_message));
        }
        else messageTextView.setText(activity.getString(R.string.downloading_weather_data_progress_message));
        dataDownloader.setLoadingViewsVisibility(true);
        new WeatherDataDownloader(location,this);
    }

    @Override
    public void weatherServiceSuccess(Channel channel) {
        WeatherDataParser weatherDataParser = new WeatherDataParser(channel);
        if(dataDownloader.isFirstLaunch()&& dataDownloader.getSelectedDefaultLocationOption() ==1){
            showWeatherResultsForLocationDialog(weatherDataParser);
        }
        else {
            startMainActivity(weatherDataParser);
        }
    }

    private void showWeatherResultsForLocationDialog(WeatherDataParser weatherDataParser){
        IntroActivityLoadingFragmentWeatherResultsForLocationDialogInitializer weatherResultsForLocationDialogInitializer
                =new IntroActivityLoadingFragmentWeatherResultsForLocationDialogInitializer(activity,dataDownloader,weatherDataParser);
        AlertDialog weatherResultsForLocationDialog=weatherResultsForLocationDialogInitializer.getWeatherResultsForLocationDialog();
        showDialog(weatherResultsForLocationDialog);
    }

    private void startMainActivity(WeatherDataParser weatherDataParser){
        new IntroActivityLoadingFragmentMainActivityStartingInitializer(
                activity,
                dataDownloader,
                weatherDataParser
        );
    }

    @Override
    public void weatherServiceFailure(int errorCode) {
        if(errorCode==0) {showDialog(weatherInternetFailureDialog);}
        else if(errorCode==1){
            if(dataDownloader.isFirstLaunch()) {
                showDialog(noWeatherResultsForLocationDialog);
            }
            else {
                if(!SharedPreferencesModifier.isDefaultLocationConstant(activity)
                        || (dataDownloader.getChangedLocation()==null&&dataDownloader.isNextLaunchAfterFailure()==true)){
                    showDialog(noWeatherResultsForLocationDialog);
                }
                else{
                    showDialog(weatherServiceFailureDialog);
                }
            }
        }
    }

    private void showDialog(AlertDialog alertDialog){
        alertDialog.show();
        dataDownloader.setLoadingViewsVisibility(false);
    }
}
