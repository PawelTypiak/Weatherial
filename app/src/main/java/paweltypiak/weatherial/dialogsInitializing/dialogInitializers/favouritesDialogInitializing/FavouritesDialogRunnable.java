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
package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.favouritesDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.weatherial.MainActivity;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.InternetFailureDialogInitializer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.ProgressDialogInitializer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.ServiceFailureDialogInitializer;
import paweltypiak.weatherial.dataDownloading.jsonHandling.weatherServiceTagTreeInitializing.Channel;
import paweltypiak.weatherial.utils.FavouritesEditor;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataParser;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDownloadCallback;

class FavouritesDialogRunnable implements Runnable,WeatherDownloadCallback {

    private Activity activity;
    private AlertDialog progressDialog;

    FavouritesDialogRunnable(Activity activity) {
        this.activity= activity;
    }

    public void run() {
        String address= FavouritesEditor.getSelectedFavouriteLocationAddress(activity);
        new WeatherDataDownloader(address,this);
        progressDialog
                = ProgressDialogInitializer.getProgressDialog(
                activity,
                activity.getString(R.string.downloading_weather_data_progress_message));
        progressDialog.show();
    }

    @Override
    public void weatherServiceSuccess(Channel channel) {
        WeatherDataParser dataInitializer=new WeatherDataParser(channel);
        ((MainActivity)activity).getMainActivityLayoutInitializer().
                updateLayoutOnWeatherDataChange(activity,dataInitializer,true,false);
        progressDialog.dismiss();
    }

    @Override
    public void weatherServiceFailure(int errorCode) {
        if(errorCode==0)   {
            AlertDialog internetFailureDialog=
                    InternetFailureDialogInitializer.getInternetFailureDialog(
                            activity,
                            1,
                            new FavouritesDialogRunnable(activity),
                            null);
            internetFailureDialog.show();
        }
        else {
            AlertDialog serviceFailureDialog=
                    ServiceFailureDialogInitializer.getServiceFailureDialog(
                            activity,
                            1,
                            new FavouritesDialogRunnable(activity),
                            null);
            serviceFailureDialog.show();
        }
        progressDialog.dismiss();
    }
}