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
package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.editFavouritesDialogInitializing;

import android.app.Activity;
import paweltypiak.weatherial.MainActivity;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.layoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.weatherial.utils.FavouritesEditor;
import paweltypiak.weatherial.utils.SharedPreferencesModifier;

class DeleteFromFavouritesRunnable implements Runnable{

    private Activity activity;

    DeleteFromFavouritesRunnable(Activity activity){
        this.activity=activity;
    }

    @Override
    public void run() {
        FavouritesEditor.deleteFavouritesItem(activity);
        updateAppBarLayout();
        updateDefaultLocation();
    }

    private void updateAppBarLayout(){
        setFloatingActionButtonOnClickIndicator();
        uncheckNavigationDrawerMenuItem();
        updateLocationName();
    }

    private void setFloatingActionButtonOnClickIndicator(){
        ((MainActivity)activity).getMainActivityLayoutInitializer()
                .getAppBarLayoutInitializer()
                .getAppBarLayoutButtonsInitializer()
                .getFloatingActionButtonInitializer()
                .setFloatingActionButtonOnClickIndicator(0);
    }

    private void uncheckNavigationDrawerMenuItem(){
        ((MainActivity)activity).getMainActivityLayoutInitializer()
                .getAppBarLayoutInitializer()
                .getAppBarLayoutButtonsInitializer()
                .getNavigationDrawerInitializer()
                .uncheckNavigationDrawerMenuItem(1);
    }

    private void updateLocationName(){
        String city= OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCity();
        String region=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getRegion();
        String country=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCountry();
        ((MainActivity)activity).getMainActivityLayoutInitializer().getAppBarLayoutInitializer().
                getAppBarLayoutDataInitializer().
                updateLocationName(city,region+", "+ country);
    }

    private void updateDefaultLocation(){
        if(FavouritesEditor.isDefaultLocationEqual(activity,null)) {
            SharedPreferencesModifier.setDefaultLocationGeolocalization(activity);
        }
    }
}