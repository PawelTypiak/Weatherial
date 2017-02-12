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
package paweltypiak.weatherial.dialogsInitializing.dialogInitializers;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.weatherial.dialogsInitializing.alertDialogTools.AlertDialogButtonsCustomizer;

public class GeolocalizationProviderUnavailableDialogInitializer {

    public static AlertDialog getGeolocalizationProviderUnavailableDialog(Activity activity,
                                                                          int type,
                                                                          Runnable positiveButtonRunnable){
        return initializeGeolocalizationProviderUnavailableDialog(
                activity,
                type,
                positiveButtonRunnable
        );
    }

    private static AlertDialog initializeGeolocalizationProviderUnavailableDialog(Activity activity,
                                                                                  int type,
                                                                                  Runnable positiveButtonRunnable){
        View dialogView=initializeDialogView(activity);
        initializeMessageTextView(activity,dialogView,type);
        AlertDialog geolocalizationProviderUnavailableDialog=buildAlertDialog(
                activity,
                type,
                positiveButtonRunnable,
                dialogView
        );
        setAlertDialogOnShowListener(activity,geolocalizationProviderUnavailableDialog);
        return geolocalizationProviderUnavailableDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        return dialogView;
    }

    private static void initializeMessageTextView(Activity activity,View dialogView,int type){
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        String message=null;
        if(type==0){
            message=activity.getString(R.string.gps_unavailable_dialog_message);
        }
        else if(type==1){
            message=activity.getString(R.string.network_unavailable_dialog_message);
        }
        messageTextView.setText(message);
    }

    private static AlertDialog buildAlertDialog(Activity activity,
                                                int type,
                                                Runnable positiveButtonRunnable,
                                                View dialogView) {
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                getTitle(activity,type),
                R.drawable.warning_icon,
                null,
                true,
                getPositiveButtonText(activity,type),
                positiveButtonRunnable,
                null,
                null,
                null,
                null);
        return alertDialogBuilder.getAlertDialog();
    }

    private static String getTitle(Activity activity, int type){
        String title=null;
        if(type==0){
            title=activity.getString(R.string.gps_unavailable_dialog_title);
        }
        else if(type==1){
            title=activity.getString(R.string.network_unavailable_dialog_title);
        }
        return title;
    }

    private static String getPositiveButtonText(Activity activity, int type){
        String positiveButtonText=null;
        if(type==0){
            positiveButtonText=activity.getString(R.string.gps_unavailable_dialog_positive_button);
        }
        else if(type==1){
            positiveButtonText=activity.getString(R.string.network_unavailable_dialog_positive_button);
        }
        return positiveButtonText;
    }

    private static void setAlertDialogOnShowListener(final Activity activity,
                                                     final AlertDialog geolocalizationProviderUnavailableDialog){
        geolocalizationProviderUnavailableDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,geolocalizationProviderUnavailableDialog);
            }
        });
    }
}
