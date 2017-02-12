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
package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.noDifferentLocationSelectedDialogInitializing;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.weatherial.dialogsInitializing.alertDialogTools.AlertDialogButtonsCustomizer;

public class NoDifferentLocationSelectedDialogInitializer {

    public static AlertDialog getNoDifferentLocationSelectedDialog(Activity activity,
                                                                    AlertDialog searchDialog){
        return initializeNoDifferentLocationSelectedDialog(
                activity,
                searchDialog
        );
    }

    private static AlertDialog initializeNoDifferentLocationSelectedDialog(Activity activity,
                                                                          AlertDialog searchDialog){
        View dialogView=initializeDialogView(activity);
        initializeMessageTextView(activity,dialogView);
        AlertDialog noDifferentLocationSelectedDialog =buildAlertDialog(
                activity,
                dialogView,
                searchDialog
        );
        setAlertDialogOnShowListener(activity,noDifferentLocationSelectedDialog);
        return noDifferentLocationSelectedDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        return dialogView;
    }

    private static void initializeMessageTextView(Activity activity,View dialogView){
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.no_different_location_selected_dialog_message));
    }

    private static AlertDialog buildAlertDialog(Activity activity,
                                                View dialogView,
                                                AlertDialog searchDialog){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.failure_dialog_title),
                R.drawable.warning_icon,
                null,
                false,
                activity.getString(R.string.no_different_location_selected_dialog_positive_button),
                new ShowDifferentLocationDialogRunnable(searchDialog),
                null,
                null,
                activity.getString(R.string.no_different_location_selected_dialog_negative_button),
                null);
        return alertDialogBuilder.getAlertDialog();
    }

    private static void setAlertDialogOnShowListener(final Activity activity,
                                                     final AlertDialog noDifferentLocationSelectedDialog){
        noDifferentLocationSelectedDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,noDifferentLocationSelectedDialog);
            }
        });
    }
}
