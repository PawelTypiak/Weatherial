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
package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.searchDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.weatherial.dialogsInitializing.alertDialogTools.AlertDialogButtonsCustomizer;
import paweltypiak.weatherial.dialogsInitializing.alertDialogTools.EditTextCustomizer;
import paweltypiak.weatherial.dialogsInitializing.alertDialogTools.KeyboardVisibilitySetter;

public class SearchDialogInitializer {

    private AlertDialog searchDialog;

    public SearchDialogInitializer(Activity activity, int type, RadioButton radioButton){
        searchDialog=initializeSearchDialog(activity,type,radioButton);
    }

    public AlertDialog getSearchDialog() {
        return searchDialog;
    }

    private AlertDialog initializeSearchDialog(Activity activity,int type,RadioButton radioButton){
        View dialogView=initializeDialogView(activity);
        setDialogViewClickableAndFocusable(dialogView);
        EditText locationEditText=initializeEditText(activity,dialogView,type,radioButton);
        searchDialog=buildAlertDialog(activity,type,radioButton,dialogView);
        setAlertDialogOnShowListener(
                activity,
                locationEditText);
        setAlertDialogOnDismissListener(
                activity,
                locationEditText,
                searchDialog);
        return searchDialog;
    }

    private View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_search,null);
        return dialogView;
    }

    private void setDialogViewClickableAndFocusable(View dialogView){
        dialogView.setFocusableInTouchMode(true);
        dialogView.setClickable(true);
    }

    private EditText initializeEditText(Activity activity,
                                        View dialogView,
                                        int type,
                                        RadioButton radioButton){
        EditText locationEditText=(EditText)dialogView.findViewById(R.id.search_edit_text);
        locationEditText.requestFocus();
        if(type==0){
            String radioButtonString=radioButton.getText().toString();
            if(!radioButtonString.equals(activity.getString(R.string.intro_activity_default_location_different))){
                locationEditText.setText(radioButtonString);
                locationEditText.setSelection(radioButtonString.length());
            }
        }
        return locationEditText;
    }

    private AlertDialog buildAlertDialog(Activity activity,
                                         int type,
                                         RadioButton radioButton,
                                         View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                getTitle(activity,type),
                getIconResourceId(type),
                null,
                false,
                getPositiveButtonText(activity,type),
                getPositiveButtonRunnable(activity,type,radioButton,dialogView) ,
                null,
                null,
                activity.getString(R.string.search_dialog_negative_button),
                null);
        return alertDialogBuilder.getAlertDialog();
    }

    private int getIconResourceId(int type){
        int iconResourceId=0;
        if(type==0){
            iconResourceId=R.drawable.location_icon;
        }
        else if(type==1){
            iconResourceId=R.drawable.search_icon;
        }
        return iconResourceId;
    }

    private String getTitle(Activity activity,int type){
        String title=null;
        if(type==0){
            title=activity.getString(R.string.search_dialog_title_type_0);
        }
        else if(type==1){
            title=activity.getString(R.string.search_dialog_title_type_1);
        }
        return title;
    }

    private String getPositiveButtonText(Activity activity, int type){
        String positiveButtonText=null;
        if(type==0){
            positiveButtonText=activity.getString(R.string.search_dialog_positive_button_type_0);
        }
        else if(type==1){
            positiveButtonText=activity.getString(R.string.search_dialog_positive_button_type_1);
        }
        return positiveButtonText;
    }

    private Runnable getPositiveButtonRunnable(Activity activity,
                                                      int type,
                                                      RadioButton radioButton,
                                                      View dialogView){
        Runnable positiveButtonRunnable=null;
        if(type==0){
            positiveButtonRunnable=new DifferentLocationDialogRunnable(activity,radioButton,dialogView);
        }
        else if(type==1){
            positiveButtonRunnable=new SearchRunnable(activity,dialogView);
        }
        return positiveButtonRunnable;
    }

    private void setAlertDialogOnShowListener(final Activity activity,
                                              final EditText locationEditText){
        searchDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,searchDialog);
                KeyboardVisibilitySetter.showKeyboard(activity);
                EditTextCustomizer.customizeEditText(activity,searchDialog,locationEditText);
            }
        });
    }

    private void setAlertDialogOnDismissListener(final Activity activity,
                                                 final EditText locationEditText,
                                                 AlertDialog searchDialog){
        searchDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                KeyboardVisibilitySetter.hideKeyboard(activity,null);
                locationEditText.getText().clear();
            }
        });
    }
}
