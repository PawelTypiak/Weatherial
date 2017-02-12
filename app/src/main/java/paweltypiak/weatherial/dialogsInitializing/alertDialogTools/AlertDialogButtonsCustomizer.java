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
package paweltypiak.weatherial.dialogsInitializing.alertDialogTools;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import paweltypiak.weatherial.R;

public class AlertDialogButtonsCustomizer {

    public static void setDialogButtonDisabled(AlertDialog alertDialog, Context context){
        //disable alert dialog positive button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }
    }

    public static void setDialogButtonEnabled(AlertDialog alertDialog, Context context){
        //enable alert dialog positive button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }
    }

    public static void setDialogButtonsTextFont(Activity activity, AlertDialog alertDialog){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Typeface robotoMediumFontTypeface=Typeface.createFromAsset(activity.getAssets(), "Roboto-Medium.ttf");
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(robotoMediumFontTypeface);
            alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTypeface(robotoMediumFontTypeface);
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTypeface(robotoMediumFontTypeface);
        }
    }
}
