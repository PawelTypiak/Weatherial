package paweltypiak.matweather.dialogsInitializing.dialogInitializers.favouritesDialogInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.matweather.dialogsInitializing.AlertDialogTools.AlertDialogButtonsAvailabilitySetter;
import paweltypiak.matweather.usefulClasses.FavouritesEditor;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class FavouritesDialogInitializer {

    private AlertDialog favouritesDialog;

    public FavouritesDialogInitializer(Activity activity,
                                       int type,
                                       Runnable positiveButtonRunnable,
                                       Runnable negativeButtonRunnable){
        favouritesDialog=initializeFavouritesDialog(activity,type,positiveButtonRunnable,negativeButtonRunnable);
    }

    public AlertDialog getFavouritesDialog() {
        return favouritesDialog;
    }

    private AlertDialog initializeFavouritesDialog(Activity activity,
                                                   int type,
                                                   Runnable positiveButtonRunnable,
                                                   Runnable negativeButtonRunnable){
        View dialogView=initializeDialogView(activity);
        initializeRadioGroup(activity,type,dialogView);
        favouritesDialog = buildAlertDialog(
                activity,
                type,
                positiveButtonRunnable,
                negativeButtonRunnable,
                dialogView);
        setAlertDialogOnClickListener(activity,favouritesDialog);
        return favouritesDialog;
    }

    private View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_radiogroup,null);
        return dialogView;
    }

    private void initializeRadioGroup(final Activity activity, int type,View dialogView){
        final RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.radiogroup_dialog_radiogroup);
        initializeFavouriteLocationsList(activity,type,radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                AlertDialogButtonsAvailabilitySetter.setDialogButtonEnabled(favouritesDialog,activity);
                FavouritesEditor.setSelectedFavouriteLocationID(i);
                Log.d("wybrane id", ""+i);
            }
        });
    }

    private void initializeFavouriteLocationsList(Activity activity, int type, RadioGroup radioGroup){
        List<String> favouritesList = FavouritesEditor.getFavouriteLocationsNamesDialogList(activity);
        int size=favouritesList.size();
        if(type==0) {
            size=++size;
        }
        for(int i=0;i<size;i++){
            initializeRadioButton(activity,
                    type,
                    favouritesList,
                    radioGroup,
                    size,
                    i
                    );
        }
    }

    private void initializeRadioButton(Activity activity,
                                       int type,
                                       List<String> favouritesList,
                                       RadioGroup radioGroup,
                                       int size,
                                       int i
                                       ){
        RadioButton radioButton=new RadioButton(activity);
        radioButton.setId(i);
        radioButton.setText(UsefulFunctions.fromHtml(getLocationName(activity,type,favouritesList,size,i)));
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,activity.getResources().getDimensionPixelSize(R.dimen.locations_list_text_size));
        radioButton.setSingleLine();
        radioButton.setEllipsize(TextUtils.TruncateAt.END);
        radioButton.setTextColor(ContextCompat.getColor(activity,R.color.textSecondaryLightBackground));
        setRadioButtonMargins(activity,size,i,radioButton);
        radioGroup.addView(radioButton);
    }

    private String getLocationName(Activity activity,
                                   int type,
                                   List<String> favouritesList,
                                   int size,
                                   int i
    ){
        String locationName=null;
        if(type==0){
            if(i!=size-1){
                locationName=favouritesList.get(i);
            }
            else{
                locationName=activity.getString(R.string.favourites_dialog_geolocalization_option);
            }
        }
        else if(type==1) locationName=favouritesList.get(i);
        return locationName;
    }

    private void setRadioButtonMargins(Activity activity,
                                       int size,
                                       int i,
                                       RadioButton radioButton
                                       ){
        if(i!=size-1){
            LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            int radioButtonBottomMargin=(int)(activity.getResources().getDimension(R.dimen.radio_button_bottom_margin));
            layoutParams.bottomMargin=radioButtonBottomMargin;
            radioButton.setLayoutParams(layoutParams);
        }
    }

    private AlertDialog buildAlertDialog(Activity activity,
                                         int type,
                                         Runnable positiveButtonRunnable,
                                         Runnable negativeButtonRunnable,
                                         View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                getTitle(activity,type),
                getIconResourceId(type),
                null,
                isUncancelable(type),
                activity.getString(R.string.favourites_dialog_positive_button),
                getPositiveButtonRunnable(activity,positiveButtonRunnable),
                null,
                null,
                getNegativeButtonText(activity,type),
                negativeButtonRunnable);
        return alertDialogBuilder.getAlertDialog();
    }

    private String getTitle(Activity activity,int type){
        String title=null;
        if(type==0) {
            title=activity.getString(R.string.favourites_dialog_first_launch_title);
        }
        else if(type==1){
            title=activity.getString(R.string.favourites_dialog_title);
        }
        return  title;
    }

    private int getIconResourceId(int type){
        int iconResourceId=0;
        if(type==0) {
            iconResourceId=R.drawable.dialog_localization_icon;
        }
        else if(type==1){
            iconResourceId=R.drawable.dialog_favourites_icon;
        }
        return iconResourceId;
    }

    private boolean isUncancelable(int type){
        boolean isUncancelable=false;
        if(type==0) {
            isUncancelable=true;
        }
        else if(type==1){
            isUncancelable=false;
        }
        return isUncancelable;
    }

    private Runnable getPositiveButtonRunnable(Activity activity,Runnable positiveButtonRunnable){
        if(positiveButtonRunnable==null){
            return new FavouritesDialogRunnable(activity);
        }
        else{
            return positiveButtonRunnable;
        }
    }

    private String getNegativeButtonText(Activity activity, int type){
        String negativeButtonText=null;
        if(type==0) {
            negativeButtonText=activity.getString(R.string.favourites_dialog_negative_button_type_0);
        }
        else if(type==1){
            negativeButtonText=activity.getString(R.string.favourites_dialog_negative_button_type_1);
        }
        return negativeButtonText;
    }

    private void setAlertDialogOnClickListener(final Activity activity,
                                               final AlertDialog favouritesDialog){
        favouritesDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsAvailabilitySetter.setDialogButtonDisabled(favouritesDialog,activity);
            }
        });
    }
}
