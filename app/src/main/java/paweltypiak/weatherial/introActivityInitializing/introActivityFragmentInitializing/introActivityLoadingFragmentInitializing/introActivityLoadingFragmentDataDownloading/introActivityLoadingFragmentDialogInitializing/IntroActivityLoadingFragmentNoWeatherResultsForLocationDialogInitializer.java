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
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.NoWeatherResultsForLocationDialogInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentDataDownloader;

public class IntroActivityLoadingFragmentNoWeatherResultsForLocationDialogInitializer {

    private Activity activity;
    private IntroActivityLoadingFragmentDataDownloader dataDownloader;
    private AlertDialog noWeatherResultsForLocationDialog;

    public IntroActivityLoadingFragmentNoWeatherResultsForLocationDialogInitializer(Activity activity,
                                                                                    IntroActivityLoadingFragmentDataDownloader dataDownloader){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
        initializeNoWeatherResultsForLocationDialog();
    }

    public AlertDialog getNoWeatherResultsForLocationDialog() {
        return noWeatherResultsForLocationDialog;
    }

    private void initializeNoWeatherResultsForLocationDialog(){
        noWeatherResultsForLocationDialog
                = NoWeatherResultsForLocationDialogInitializer.getNoWeatherResultsForLocationDialog(
                activity,
                0,
                getPositiveButtonRunnable(),
                getNegativeButtonRunnable()
        );
    }

    private Runnable getPositiveButtonRunnable(){
        boolean isFirstLaunch=dataDownloader.isFirstLaunch();
        if(isFirstLaunch) {
            ShowLocationFragmentRunnableInitializer runnableInitializer
                    =new ShowLocationFragmentRunnableInitializer(dataDownloader);
            return runnableInitializer.getShowLocationFragmentRunnable();
        }
        else {
            ShowLoadingFragmentChangeLocationAfterFailureDialogRunnableInitializer runnableInitializer
                    =new ShowLoadingFragmentChangeLocationAfterFailureDialogRunnableInitializer(activity,dataDownloader);
            return runnableInitializer.getShowChangeLocationAfterFailureDialogRunnable();
        }
    }

    private Runnable getNegativeButtonRunnable(){
       return new ShowLoadingFragmentExitDialogRunnable(activity,showNoWeatherResultsForLocationRunnable);
    }

    private Runnable showNoWeatherResultsForLocationRunnable = new Runnable() {
        public void run() {noWeatherResultsForLocationDialog.show();}
    };
}
