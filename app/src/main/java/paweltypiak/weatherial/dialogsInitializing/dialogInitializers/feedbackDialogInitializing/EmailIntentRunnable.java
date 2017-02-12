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
package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.feedbackDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.noEmailApplicationDialogInitializing.NoEmailApplicationDialogInitializer;

class EmailIntentRunnable implements Runnable {

    private Activity activity;

    EmailIntentRunnable(Activity activity) {
        this.activity=activity;
    }

    public void run() {
        initializeEmailIntent(activity);
    }

    private static void initializeEmailIntent(Activity activity){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        String subject=activity.getString(R.string.clipboard_feedback_mail_subject);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        String address=activity.getString(R.string.app_email_address);
        intent.setData(Uri.parse("mailto:"+address));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            activity.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            AlertDialog dialog=NoEmailApplicationDialogInitializer.getNoEmailApplicationDialog(activity);
            dialog.show();
        }
    }
}
