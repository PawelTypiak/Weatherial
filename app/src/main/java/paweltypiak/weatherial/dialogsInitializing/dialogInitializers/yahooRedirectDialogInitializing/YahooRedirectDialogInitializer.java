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
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.weatherial.dialogsInitializing.alertDialogTools.AlertDialogButtonsCustomizer;

public class YahooRedirectDialogInitializer {

    public static AlertDialog getYahooRedirectDialog(Activity activity,
                                                      int type,
                                                      String link){
        return initializeYahooRedirectDialog(
                activity,
                type,
                link);
    }

    private static AlertDialog initializeYahooRedirectDialog(Activity activity,
                                                     int type,
                                                     String link){
        View dialogView=initializeDialogView(activity);
        initializeMessageTextView(activity,dialogView);
        initializeLinkTextView(activity,type,dialogView);
        AlertDialog yahooRedirectDialog = buildAlertDialog(activity,type,link,dialogView);
        setAlertDialogOnShowListener(activity,yahooRedirectDialog);
        return  yahooRedirectDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_two_line_text,null);
        return dialogView;
    }

    private static void initializeMessageTextView(Activity activity,View dialogView){
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.two_line_text_dialog_message_text_1);
        messageTextView.setText(activity.getString(R.string.yahoo_redirect_dialog_message));
    }

    private static void initializeLinkTextView(Activity activity,
                                               int type,
                                               View dialogView){
        String message= getMessage(activity,type);
        TextView linkTextView=(TextView)dialogView.findViewById(R.id.two_line_text_dialog_message_text_2);
        linkTextView.setText(message);
    }

    private static String getMessage(Activity activity,
                                     int type){
        String message=null;
        if(type==0){
            message=activity.getString(R.string.yahoo_main_redirect_dialog_message_service_name);
        }
        else if(type==1){
            message=activity.getString(R.string.yahoo_weather_redirect_dialog_message_service_name);
        }
        return message;
    }

    private static AlertDialog buildAlertDialog(Activity activity,
                                                int type,
                                                String link,
                                                View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.yahoo_redirect_dialog_title),
                R.drawable.info_icon,
                null,
                false,
                activity.getString(R.string.yahoo_redirect_dialog_positive_button),
                getPositiveButtonRunnable(activity,type,link),
                null,
                null,
                activity.getString(R.string.yahoo_redirect_dialog_negative_button),
                null);
        return alertDialogBuilder.getAlertDialog();
    }

    private static Runnable getPositiveButtonRunnable(Activity activity, int type,String link){
        Runnable positiveButtonRunnable=null;
        if(type==0){
            positiveButtonRunnable=new WebIntentRunnable(activity,activity.getString(R.string.yahoo_address));
        }
        else if(type==1){
            positiveButtonRunnable=new WebIntentRunnable(activity,link);
        }
        return positiveButtonRunnable;
    }

    private static void setAlertDialogOnShowListener(final Activity activity, final AlertDialog yahooRedirectDialog){
        yahooRedirectDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,yahooRedirectDialog);
            }
        });
    }
}
