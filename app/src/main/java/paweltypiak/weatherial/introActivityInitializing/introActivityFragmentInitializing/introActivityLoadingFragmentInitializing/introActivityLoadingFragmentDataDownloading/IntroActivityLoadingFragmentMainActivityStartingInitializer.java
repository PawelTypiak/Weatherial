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
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.MainActivity;
import paweltypiak.weatherial.utils.FavouritesEditor;
import paweltypiak.weatherial.utils.SharedPreferencesModifier;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataParser;

public class IntroActivityLoadingFragmentMainActivityStartingInitializer {

    public IntroActivityLoadingFragmentMainActivityStartingInitializer(Activity activity,
                                                                       IntroActivityLoadingFragmentDataDownloader dataDownloader,
                                                                       WeatherDataParser weatherDataParser){
        updateLayout(activity,dataDownloader);
        if(dataDownloader.isFirstLaunch()) {
            saveDefaultLocationData(activity,dataDownloader,weatherDataParser);
        }
        startMainActivity(activity,weatherDataParser);
    }

    private void updateLayout(Activity activity,
                              IntroActivityLoadingFragmentDataDownloader dataDownloader){
        updateTextView(activity,dataDownloader);
        updateProgressBar(dataDownloader);
    }

    private void updateTextView(Activity activity,
                                IntroActivityLoadingFragmentDataDownloader dataDownloader){
        TextView messageTextView=dataDownloader.getMessageTextView();
        messageTextView.setText(activity.getString(R.string.loading_content_progress_message));
        messageTextView.setVisibility(View.VISIBLE);
    }

    private void updateProgressBar(IntroActivityLoadingFragmentDataDownloader dataDownloader){
        ProgressBar progressBar=dataDownloader.getLoadingProgressBar();
        progressBar.setVisibility(View.GONE);
    }

    private void saveDefaultLocationData(Activity activity,
                                         IntroActivityLoadingFragmentDataDownloader dataDownloader,
                                         WeatherDataParser weatherDataParser){
        int selectedDefaultLocationOption=dataDownloader.getSelectedDefaultLocationOption();
        if(selectedDefaultLocationOption ==0){
            saveDataForGeolocalization(activity,dataDownloader);
        }
        else{
            saveDataForDifferentLocation(activity,weatherDataParser);
        }
        SharedPreferencesModifier.setNextLaunch(activity);
    }

    private void saveDataForGeolocalization(Activity activity,
                                            IntroActivityLoadingFragmentDataDownloader dataDownloader){
        SharedPreferencesModifier.setGeolocalizationMethod(activity, dataDownloader.getSelectedDefaultLocalizationMethod());
        SharedPreferencesModifier.setDefaultLocationGeolocalization(activity);
    }

    private void saveDataForDifferentLocation(Activity activity,
                                              WeatherDataParser weatherDataParser){
        String[] locationAddress=getDownloadedLocationAddress(weatherDataParser);
        setDefaultLocationAddress(activity,locationAddress);
        saveNewFavouritesItem(activity,locationAddress);
    }

    private String[] getDownloadedLocationAddress(WeatherDataParser weatherDataParser){
        String city=weatherDataParser.getCity();
        String region=weatherDataParser.getRegion();
        String country=weatherDataParser.getCountry();
        String[] locationAddress={city,region,country};
        return locationAddress;
    }

    private void setDefaultLocationAddress(Activity activity,
                                           String[] locationAddress){
        String city=locationAddress[0];
        String region=locationAddress[1];
        String country=locationAddress[2];
        String locationAddressString=city+", "+region+", "+country;
        SharedPreferencesModifier.setDefaultLocationConstant(activity,locationAddressString);
    }

    private void saveNewFavouritesItem(Activity activity,
                                       String[] locationAddress){
        //saving selected location in favourites
        String city=locationAddress[0];
        String region=locationAddress[1];
        String country=locationAddress[2];
        String locationNameTitle=city;
        String locationNameSubtitle=region+", "+country;
        String locationAddressString=locationNameTitle+", "+locationNameSubtitle;
        FavouritesEditor.saveNewFavouritesItem(activity,locationNameTitle,locationNameSubtitle,locationAddressString);
    }

    private void startMainActivity(Activity activity,
                                   WeatherDataParser weatherDataParser){
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(activity.getString(R.string.extras_data_initializer_key),weatherDataParser);
        activity.startActivity(intent);
        activity.finish();
    }
}
