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
package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.searchDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.alertDialogTools.KeyboardVisibilitySetter;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.InternetFailureDialogInitializer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.NoWeatherResultsForLocationDialogInitializer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.ProgressDialogInitializer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.WeatherResultsForLocationDialogInitializer;
import paweltypiak.weatherial.dataDownloading.jsonHandling.weatherServiceTagTreeInitializing.Channel;
import paweltypiak.weatherial.utils.UsefulFunctions;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataParser;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDownloadCallback;

class SearchRunnable implements Runnable, WeatherDownloadCallback {

    private Activity activity;
    private String location;
    private EditText locationEditText;
    private boolean isReused;
    private AlertDialog progressDialog;


    private SearchRunnable(Activity activity,String location){
        //constructor for reload searchDialog
        this.activity=activity;
        this.location=location;
        isReused =true;
    }
    SearchRunnable(Activity activity,View dialogView){
        //constructor for using searchDialog for the first time
        this.activity=activity;
        locationEditText=(EditText)dialogView.findViewById(R.id.search_edit_text);
        isReused =false;
    }

    public void run(){
        progressDialog
                = ProgressDialogInitializer.getProgressDialog(
                activity,
                activity.getString(R.string.retrieving_location_progress_message));
        progressDialog.show();
        if(isReused == false){
            location=locationEditText.getText().toString();
            location= UsefulFunctions.getFormattedString(location);
            KeyboardVisibilitySetter.hideKeyboard(activity,locationEditText);
        }
        new WeatherDataDownloader(location,this);
    }

    @Override
    public void weatherServiceSuccess(Channel channel) {
        showLocalizationResultsDialog(channel);
        progressDialog.dismiss();
    }

    private void showLocalizationResultsDialog(Channel channel){
        WeatherDataParser weatherDataParser=new WeatherDataParser(channel);
        AlertDialog localizationResultsDialog
                = WeatherResultsForLocationDialogInitializer.getWeatherResultsForLocationDialog(
                activity,
                1,
                weatherDataParser,
                new SetMainLayoutRunnable(activity,weatherDataParser),
                new ShowSearchDialogRunnable(activity),
                null);
        localizationResultsDialog.show();
    }

    @Override
    public void weatherServiceFailure(int errorCode) {
        if(errorCode==0){
            showInternetFailureDialog();
        }
        else if(errorCode==1) {
            showNoWeatherResultsForLocationDialog();
        }
        progressDialog.dismiss();
    }

    private void showInternetFailureDialog(){
        AlertDialog internetFailureDialog
                = InternetFailureDialogInitializer.getInternetFailureDialog(activity,
                1,
                new SearchRunnable(activity,location),
                null);
        internetFailureDialog.show();
    }

    private void showNoWeatherResultsForLocationDialog(){
        AlertDialog noWeatherResultsForLocation
                = NoWeatherResultsForLocationDialogInitializer.getNoWeatherResultsForLocationDialog(
                activity,
                1,
                new ShowSearchDialogRunnable(activity),
                null);
        noWeatherResultsForLocation.show();
    }
}
