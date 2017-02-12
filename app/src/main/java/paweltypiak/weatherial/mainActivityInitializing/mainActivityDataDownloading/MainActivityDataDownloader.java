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
package paweltypiak.weatherial.mainActivityInitializing.mainActivityDataDownloading;

import android.app.Activity;
import android.os.Bundle;

import paweltypiak.weatherial.mainActivityInitializing.mainActivityDataDownloading.mainActivityGeolocalizationDownloading.MainActivityGeolocalizationDownloader;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityDataDownloading.mainActivityWeatherDataDownloading.MainActivityWeatherDataDownloader;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;

public class MainActivityDataDownloader {

    private MainActivityWeatherDataDownloader weatherDataDownloader;
    private MainActivityGeolocalizationDownloader geolocalizationDownloader;

    public MainActivityDataDownloader(Activity activity,
                                      Bundle savedInstanceState,
                                      MainActivityLayoutInitializer layoutInitializer){
        this.geolocalizationDownloader=new MainActivityGeolocalizationDownloader(activity,this);
        this.weatherDataDownloader
                =new MainActivityWeatherDataDownloader(activity, savedInstanceState,layoutInitializer,this);
    }

    public MainActivityWeatherDataDownloader getWeatherDataDownloader() {
        return weatherDataDownloader;
    }

    public MainActivityGeolocalizationDownloader getGeolocalizationDownloader() {
        return geolocalizationDownloader;
    }
}
