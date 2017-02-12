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

public class NoWeatherResultsForLocationDialogInitializer {

    public static AlertDialog getNoWeatherResultsForLocationDialog(Activity activity,
                                                                    int type,
                                                                    Runnable positiveButtonRunnable,
                                                                    Runnable negativeButtonRunnable){
        return initializeNoWeatherResultsForLocationDialog(
                activity,
                type,
                positiveButtonRunnable,
                negativeButtonRunnable);
    }

    private static AlertDialog initializeNoWeatherResultsForLocationDialog(Activity activity,
                                                                          int type,
                                                                          Runnable positiveButtonRunnable,
                                                                          Runnable negativeButtonRunnable){
        View dialogView=initializeDialogView(activity);
        initializeMessageTextView(activity,dialogView);
        AlertDialog noWeatherResultsForLocationDialog =buildAlertDialog(
                activity,
                type,
                positiveButtonRunnable,
                negativeButtonRunnable,
                dialogView
        );
        setAlertDialogOnShowListener(activity,noWeatherResultsForLocationDialog);
        return noWeatherResultsForLocationDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        return dialogView;
    }

    private static void initializeMessageTextView(Activity activity,View dialogView){
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.no_weather_results_for_location_dialog_message));
    }

    private static AlertDialog buildAlertDialog(Activity activity,
                                                int type,
                                                Runnable positiveButtonRunnable,
                                                Runnable negativeButtonRunnable,
                                                View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.failure_dialog_title),
                R.drawable.error_icon,
                null,
                isUncancelable(type),
                getPositiveButtonString(activity,type),
                positiveButtonRunnable,
                null,
                null,
                getNegativeButtonString(activity,type),
                negativeButtonRunnable);
        return alertDialogBuilder.getAlertDialog();
    }

    private static boolean isUncancelable(int type){
        boolean isUncancelable=false;
        if(type==0){
            isUncancelable=true;
        }
        else if(type==1){
            isUncancelable=false;
        }
        else if(type==2){
            isUncancelable=false;
        }
        return isUncancelable;
    }

    private static String getPositiveButtonString(Activity activity, int type){
        String positiveButtonString=null;
        if(type==0){
            positiveButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_positive_button_type_0_1);
        }
        else if(type==1){
            positiveButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_positive_button_type_0_1);
        }
        else if(type==2){
            positiveButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_positive_button_type_2);
        }
        return positiveButtonString;
    }

    private static String getNegativeButtonString(Activity activity,int type){
        String negativeButtonString=null;
        if(type==0){
            negativeButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_negative_button_type_0);
        }
        else if(type==1){
            negativeButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_negative_button_type_1_2);
        }
        else if(type==2){
            negativeButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_negative_button_type_1_2);
        }
        return negativeButtonString;
    }

    private static void setAlertDialogOnShowListener(final Activity activity,
                                                     final AlertDialog noWeatherResultsForLocationDialog){
        noWeatherResultsForLocationDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,noWeatherResultsForLocationDialog);
            }
        });
    }
}
