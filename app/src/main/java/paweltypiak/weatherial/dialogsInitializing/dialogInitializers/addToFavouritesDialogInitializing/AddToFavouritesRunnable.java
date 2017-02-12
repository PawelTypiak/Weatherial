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
package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.addToFavouritesDialogInitializing;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import paweltypiak.weatherial.MainActivity;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.layoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.AppBarLayoutInitializer;
import paweltypiak.weatherial.utils.FavouritesEditor;
import paweltypiak.weatherial.utils.SharedPreferencesModifier;
import paweltypiak.weatherial.utils.UsefulFunctions;

class AddToFavouritesRunnable implements Runnable {

    private Activity activity;
    private View dialogView;

    AddToFavouritesRunnable(Activity activity,
                            View dialogView) {
        this.activity=activity;
        this.dialogView = dialogView;
    }

    public void run() {
        saveNewFavouritesItem();
        updateDefaultLocation();
        updateAppBarLayout();
    }

    private void saveNewFavouritesItem(){
        String title=getTitle();
        String subtitle=getSubtitle();
        FavouritesEditor.saveNewFavouritesItem(activity,title,subtitle,null);
    }

    private String getTitle(){
        EditText titleEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_title_edit_text);
        String title=titleEditText.getText().toString();
        return UsefulFunctions.getFormattedString(title);
    }

    private String getSubtitle(){
        EditText subtitleEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_subtitle_edit_text);
        String subtitle=subtitleEditText.getText().toString();
        return UsefulFunctions.getFormattedString(subtitle);
    }

    private void updateDefaultLocation(){
        CheckBox checkBox=(CheckBox)dialogView.findViewById(R.id.edit_location_dialog_checkbox);
        if(checkBox.isChecked()){
            String city= OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCity();
            String region=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getRegion();
            String country=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCountry();
            String currentLocationAddressString=city+", "+region+", "+ country;
            SharedPreferencesModifier.setDefaultLocationConstant(activity,currentLocationAddressString);
        }
    }

    private void updateAppBarLayout(){
        AppBarLayoutInitializer appBarLayoutInitializer
                =((MainActivity)activity).
                getMainActivityLayoutInitializer().
                getAppBarLayoutInitializer();
        updateLocationName(appBarLayoutInitializer);
        setFloatingActionButtonOnClickIndicator(appBarLayoutInitializer);
        checkNavigationDrawerMenuItem(appBarLayoutInitializer);

    }

    private void updateLocationName(AppBarLayoutInitializer appBarLayoutInitializer){
        String [] locationName=FavouritesEditor.getFavouriteLocationNameForAppbar(activity);
        appBarLayoutInitializer.
                getAppBarLayoutDataInitializer().
                updateLocationName(locationName[0],locationName[1]);
    }

    private void setFloatingActionButtonOnClickIndicator(AppBarLayoutInitializer appBarLayoutInitializer){
        appBarLayoutInitializer.
                getAppBarLayoutButtonsInitializer().
                getFloatingActionButtonInitializer().
                setFloatingActionButtonOnClickIndicator(1);
    }

    private void checkNavigationDrawerMenuItem(AppBarLayoutInitializer appBarLayoutInitializer){
        appBarLayoutInitializer.
                getAppBarLayoutButtonsInitializer().
                getNavigationDrawerInitializer().
                checkNavigationDrawerMenuItem(1);
    }
}