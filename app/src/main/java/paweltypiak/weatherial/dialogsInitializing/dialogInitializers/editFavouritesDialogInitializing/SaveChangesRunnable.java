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
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import paweltypiak.weatherial.MainActivity;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.layoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.weatherial.utils.FavouritesEditor;
import paweltypiak.weatherial.utils.SharedPreferencesModifier;
import paweltypiak.weatherial.utils.UsefulFunctions;

class SaveChangesRunnable implements Runnable {

    private Activity activity;
    private View dialogView;

    SaveChangesRunnable(Activity activity, View dialogView) {
        this.activity=activity;
        this.dialogView = dialogView;
    }

    public void run() {
        updateFavouriteLocationName();
        updateDefaultLocation();
    }

    private void updateFavouriteLocationName(){
        String[] locationName=getEditedLocationName();
        FavouritesEditor.editFavouriteLocationName(activity,locationName[0],locationName[1]);
        updateAppBarLocationName(locationName[0],locationName[1]);
    }

    private String[] getEditedLocationName(){
        String title= getEditedLocationNameTitle();
        String subtitle= getEditedLocationNameSubtitle();
        String[] locationName={title,subtitle};
        return locationName;
    }

    private String getEditedLocationNameTitle(){
        EditText titleEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_title_edit_text);
        String title=titleEditText.getText().toString();
        return UsefulFunctions.getFormattedString(title);
    }

    private String getEditedLocationNameSubtitle(){
        EditText subtitleEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_subtitle_edit_text);
        String subtitle=subtitleEditText.getText().toString();
        return UsefulFunctions.getFormattedString(subtitle);
    }

    private void updateAppBarLocationName(String title, String  subtitle){
        ((MainActivity)activity).getMainActivityLayoutInitializer().getAppBarLayoutInitializer().
                getAppBarLayoutDataInitializer().
                updateLocationName(title,subtitle);
    }

    private void updateDefaultLocation(){
        CheckBox checkBox=(CheckBox)dialogView.findViewById(R.id.edit_location_dialog_checkbox);
        if(checkBox.isChecked()){
            String currentLocationAddress= getLocationAddress();
            SharedPreferencesModifier.setDefaultLocationConstant(activity,currentLocationAddress);
        }
        else{
            if(FavouritesEditor.isDefaultLocationEqual(activity,null)) {
                SharedPreferencesModifier.setDefaultLocationGeolocalization(activity);
            }
        }
    }

    private String getLocationAddress(){
        String city= OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCity();
        String region=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getRegion();
        String country=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCountry();
        String address=city+", "+region+", "+country;
        return address;
    }
}
