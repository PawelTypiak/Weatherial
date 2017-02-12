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
package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.addToFavouritesDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import paweltypiak.weatherial.MainActivity;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.weatherial.dialogsInitializing.alertDialogTools.AlertDialogButtonsCustomizer;
import paweltypiak.weatherial.dialogsInitializing.alertDialogTools.EditTextCustomizer;
import paweltypiak.weatherial.dialogsInitializing.alertDialogTools.KeyboardVisibilitySetter;

public class AddToFavouritesDialogInitializer {

    public static AlertDialog getAddToFavouritesDialog(Activity activity){
        return initializeAddToFavouritesDialog(activity);
    }

    private static AlertDialog initializeAddToFavouritesDialog(Activity activity){
        View dialogView=initializeDialogView(activity);
        setDialogViewClickableAndFocusable(dialogView);
        EditText[] editTextArray=initializeEditTexts(activity,dialogView);
        AlertDialog addToFavouritesDialog= buildAlertDialog(activity,dialogView);
        setDialogOnShowListener(activity,editTextArray,addToFavouritesDialog);
        setDialogOnDismissListener(activity,addToFavouritesDialog);
        return addToFavouritesDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_location,null);
        return dialogView;
    }

    private static void setDialogViewClickableAndFocusable(View dialogView){
        dialogView.setFocusableInTouchMode(true);
        dialogView.setClickable(true);
    }

    private static EditText[] initializeEditTexts(Activity activity, View dialogView){
        String [] location=((MainActivity)activity).
                getMainActivityLayoutInitializer().
                getAppBarLayoutInitializer().
                getAppBarLayoutDataInitializer().
                getAppBarLocationName();
        EditText titleEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_title_edit_text);
        titleEditText.setText(location[0]);
        EditText subheaderEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_subtitle_edit_text);
        subheaderEditText.setText(location[1]);
        EditText[] editTextArray={titleEditText,subheaderEditText};
        return editTextArray;
    }

    private static AlertDialog buildAlertDialog(Activity activity, View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.add_location_dialog_title),
                R.drawable.favourites_icon,
                null,
                false,
                activity.getString(R.string.add_location_dialog_positive_button),
                new AddToFavouritesRunnable(activity,dialogView),
                null,
                null,
                activity.getString(R.string.add_location_dialog_negative_button),
                null);
        return alertDialogBuilder.getAlertDialog();
    }

    private static void setDialogOnShowListener(final Activity activity,
                                                final EditText[] editTextArray,
                                                final AlertDialog addToFavouritesDialog
                                                ){
        addToFavouritesDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,addToFavouritesDialog);
                EditTextCustomizer.customizeEditText(activity,addToFavouritesDialog,editTextArray[0]);
                EditTextCustomizer.customizeEditText(activity,addToFavouritesDialog,editTextArray[1]);
            }
        });
    }

    private static void setDialogOnDismissListener(final Activity activity, AlertDialog addToFavouritesDialog){
        addToFavouritesDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                KeyboardVisibilitySetter.hideKeyboard(activity,null);
            }
        });
    }
}
