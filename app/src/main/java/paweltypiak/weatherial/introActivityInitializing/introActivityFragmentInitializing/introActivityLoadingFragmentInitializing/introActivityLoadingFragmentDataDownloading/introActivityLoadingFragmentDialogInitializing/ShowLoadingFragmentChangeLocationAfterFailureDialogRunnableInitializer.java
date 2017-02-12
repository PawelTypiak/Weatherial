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
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.favouritesDialogInitializing.FavouritesDialogInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentDataDownloader;
import paweltypiak.weatherial.utils.FavouritesEditor;

class ShowLoadingFragmentChangeLocationAfterFailureDialogRunnableInitializer {

    private Activity activity;
    private IntroActivityLoadingFragmentDataDownloader dataDownloader;

    ShowLoadingFragmentChangeLocationAfterFailureDialogRunnableInitializer(Activity activity,
                                                                           IntroActivityLoadingFragmentDataDownloader dataDownloader){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
    }

    Runnable getShowChangeLocationAfterFailureDialogRunnable() {
        return showChangeLocationAfterFailureDialogRunnable;
    }

    private Runnable showChangeLocationAfterFailureDialogRunnable =new Runnable() {
        @Override
        public void run() {
            showDialog(getChangeLocationAfterFailureDialog());
        }

    };

    private AlertDialog getChangeLocationAfterFailureDialog(){
        FavouritesDialogInitializer favouritesDialogInitializer
                = new FavouritesDialogInitializer(
                activity,
                0,
                positiveButtonRunnable,
                getNegativeButtonRunnable());
        return favouritesDialogInitializer.getFavouritesDialog();
    }

    private Runnable positiveButtonRunnable = new Runnable() {
        public void run() {
            dataDownloader.setChangedLocation(getChangedLocation());
            dataDownloader.initializeNextLaunchAfterFailure();
        }
    };

    private Runnable getNegativeButtonRunnable(){
        return new ShowLoadingFragmentExitDialogRunnable(activity,showChangeLocationAfterFailureDialogRunnable);
    }

    private String getChangedLocation(){
        int selectedLocationID= FavouritesEditor.getSelectedFavouriteLocationID();
        int numberOfFavourites=FavouritesEditor.getNumberOfFavourites(activity);
        if(selectedLocationID==numberOfFavourites) {
            return null;
        }
        else {
            return FavouritesEditor.getSelectedFavouriteLocationAddress(activity);
        }
    }

    private void showDialog(AlertDialog alertDialog){
        alertDialog.show();
        dataDownloader.setLoadingViewsVisibility(false);
    }
}
