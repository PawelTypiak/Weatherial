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
package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.mapsDialogInitializing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

class MapsIntentRunnable implements Runnable{

    private Activity activity;
    private String label;
    private double longitude;
    private double latitude;

    MapsIntentRunnable(Activity activity,
                                        String label,
                                        double latitude,
                                        double longitude){
        this.activity=activity;
        this.label=label;
        this.longitude=longitude;
        this.latitude=latitude;
    }

    @Override
    public void run() {
        initializeMapsIntent(activity, latitude, longitude,label);
    }

    private static void initializeMapsIntent(Context context, double latitude, double longitude , String label){
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
}
