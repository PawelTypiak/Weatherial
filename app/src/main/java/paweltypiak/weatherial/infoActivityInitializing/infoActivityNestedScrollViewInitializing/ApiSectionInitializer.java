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
package paweltypiak.weatherial.infoActivityInitializing.infoActivityNestedScrollViewInitializing;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import paweltypiak.weatherial.R;

class ApiSectionInitializer {

    ApiSectionInitializer(Activity activity,
                                 InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        setWeatherApiLayoutListeners(activity,nestedScrollViewInitializer);
        setGeocodingApiLayoutListeners(activity,nestedScrollViewInitializer);
    }

    private void setWeatherApiLayoutListeners(final Activity activity,
                                              final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        LinearLayout weatherApiLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_api_weather_layout);
        String weatherApiAddress=activity.getString(R.string.yahoo_weather_api_address);
        setWeatherApiLayoutOnClickListener(activity,nestedScrollViewInitializer,weatherApiLayout,weatherApiAddress);
        setWeatherApiLayoutOnLongClickListener(activity,nestedScrollViewInitializer,weatherApiLayout,weatherApiAddress);
    }

    private void setWeatherApiLayoutOnClickListener(final Activity activity,
                                                    final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer,
                                                    LinearLayout weatherApiLayout,
                                                    final String weatherApiString){
        weatherApiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollViewInitializer.initializeWebIntent(activity,weatherApiString);
            }
        });
    }

    private void setWeatherApiLayoutOnLongClickListener(final Activity activity,
                                                        final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer,
                                                        LinearLayout weatherApiLayout,
                                                        final String weatherApiString){
        weatherApiLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                nestedScrollViewInitializer.copyAddressToClipboard(activity,weatherApiString);
                return true;
            }
        });
    }

    private void setGeocodingApiLayoutListeners(final Activity activity,
                                              final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        LinearLayout geocodingApiLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_api_geocoding_layout);
        String geocodingApiAddress=activity.getString(R.string.google_geocoding_api_address);
        setGeocodingApiLayoutOnClickListener(activity,nestedScrollViewInitializer,geocodingApiLayout,geocodingApiAddress);
        setGeocodingApiLayoutOnLongClickListener(activity,nestedScrollViewInitializer,geocodingApiLayout,geocodingApiAddress);
    }

    private void setGeocodingApiLayoutOnClickListener(final Activity activity,
                                                      final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer,
                                                      LinearLayout geocodingApiLayout,
                                                      final String geocodingApiAddress){
        geocodingApiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollViewInitializer.initializeWebIntent(activity,geocodingApiAddress);
            }
        });
    }

    private void setGeocodingApiLayoutOnLongClickListener(final Activity activity,
                                                          final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer,
                                                          LinearLayout geocodingApiLayout,
                                                          final String geocodingApiAddress){
        geocodingApiLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                nestedScrollViewInitializer.copyAddressToClipboard(activity,geocodingApiAddress);
                return true;
            }
        });
    }
}
