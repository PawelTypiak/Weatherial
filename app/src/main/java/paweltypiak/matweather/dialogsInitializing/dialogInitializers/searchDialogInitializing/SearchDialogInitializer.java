package paweltypiak.matweather.dialogsInitializing.dialogInitializers.searchDialogInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.matweather.dialogsInitializing.AlertDialogTools.EditTextCustomizer;
import paweltypiak.matweather.dialogsInitializing.AlertDialogTools.KeyboardVisibilitySetter;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.InternetFailureDialogInitializer;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.NoWeatherResultsForLocationDialogInitializer;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.ProgressDialogInitializer;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.WeatherResultsForLocationDialogInitializer;
import paweltypiak.matweather.jsonHandling.Channel;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataParser;
import paweltypiak.matweather.weatherDataDownloading.WeatherDownloadCallback;

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
                locationEditText,
                searchDialog);
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
            if(!radioButtonString.equals(activity.getString(R.string.first_launch_defeault_location_different))){
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
            iconResourceId=R.drawable.dialog_localization_icon;
        }
        else if(type==1){
            iconResourceId=R.drawable.dialog_search_icon;
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
                                              final EditText locationEditText,
                                              final AlertDialog searchDialog){
        searchDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Log.d("on_show", "search: ");


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
