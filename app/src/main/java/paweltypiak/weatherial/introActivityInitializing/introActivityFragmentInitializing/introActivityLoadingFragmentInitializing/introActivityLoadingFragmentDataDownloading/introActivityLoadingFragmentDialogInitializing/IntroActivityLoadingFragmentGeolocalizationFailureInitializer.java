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
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.GeolocalizationFailureDialogInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentDataDownloader;

public class IntroActivityLoadingFragmentGeolocalizationFailureInitializer {

    private Activity activity;
    private IntroActivityLoadingFragmentDataDownloader dataDownloader;
    private AlertDialog geolocalizationFailureDialog;

    public IntroActivityLoadingFragmentGeolocalizationFailureInitializer(Activity activity,
                                                                         IntroActivityLoadingFragmentDataDownloader dataDownloader){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
        initializeGeolocalizationFailureDialog();
    }

    public AlertDialog getGeolocalizationFailureDialog() {
        return geolocalizationFailureDialog;
    }

    private void initializeGeolocalizationFailureDialog(){
        geolocalizationFailureDialog
                = GeolocalizationFailureDialogInitializer.getGeolocalizationFailureDialog(
                activity,
                0,
                getPositiveButtonRunnable(),
                getNegativeButtonRunnable()
                );
    }

    private Runnable getPositiveButtonRunnable(){
        boolean isFirstLaunch=dataDownloader.isFirstLaunch();
        if(isFirstLaunch) {
            return new ShowLocationFragmentRunnableInitializer(dataDownloader)
                    .getShowLocationFragmentRunnable();
        }
        else {
            return new ShowLoadingFragmentChangeLocationAfterFailureDialogRunnableInitializer(activity,dataDownloader)
                    .getShowChangeLocationAfterFailureDialogRunnable();
        }
    }

    private Runnable getNegativeButtonRunnable(){
        return new ShowLoadingFragmentExitDialogRunnable(activity,showGeolocalizationFailureDialogRunnable);
    }

    private Runnable showGeolocalizationFailureDialogRunnable = new Runnable() {
        public void run() {geolocalizationFailureDialog.show();}
    };
}
