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
package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.yahooRedirectDialogInitializing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

class WebIntentRunnable implements Runnable {

    private Activity activity;
    private String url;

    WebIntentRunnable(Activity activity,String url) {
        this.activity=activity;
        this.url=url;
    }
    public void run() {
        initializeWebIntent(activity,url);
    }

    private static void initializeWebIntent(Context context, String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
}