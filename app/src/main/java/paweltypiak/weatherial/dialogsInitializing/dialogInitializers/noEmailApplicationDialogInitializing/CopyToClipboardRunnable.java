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
package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.noEmailApplicationDialogInitializing;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;
import paweltypiak.weatherial.R;

class CopyToClipboardRunnable implements Runnable {

    private Activity activity;

    CopyToClipboardRunnable(Activity activity) {
        this.activity=activity;
    }

    public void run() {
        copyToClipboard(activity);
    }

    private static void copyToClipboard(Activity activity){
        ClipboardManager myClipboard;
        ClipData myClip;
        myClipboard = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
        String text=activity.getString(R.string.author_email_address);
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(
                activity,
                activity.getString(R.string.feedback_dialog_clipboard_toast_message),
                Toast.LENGTH_SHORT).show();
    }
}
