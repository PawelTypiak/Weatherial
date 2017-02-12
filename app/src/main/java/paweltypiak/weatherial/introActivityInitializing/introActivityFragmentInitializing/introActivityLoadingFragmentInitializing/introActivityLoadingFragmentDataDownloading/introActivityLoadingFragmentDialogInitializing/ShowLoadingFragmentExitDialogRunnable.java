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
package paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.exitDialogInitializing.ExitDialogInitializer;

class ShowLoadingFragmentExitDialogRunnable implements Runnable {

    private Activity activity;
    private Runnable showDialogRunnable;

    ShowLoadingFragmentExitDialogRunnable(Activity activity,
                                          Runnable showDialogRunnable) {
        this.activity=activity;
        this.showDialogRunnable = showDialogRunnable;
    }

    public void run() {
        AlertDialog exitDialog= ExitDialogInitializer.getExitDialog(activity,0, showDialogRunnable);
        exitDialog.show();
    }
}
