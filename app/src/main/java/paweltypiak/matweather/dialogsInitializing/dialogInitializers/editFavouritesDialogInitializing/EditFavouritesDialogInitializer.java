package paweltypiak.matweather.dialogsInitializing.dialogInitializers.editFavouritesDialogInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.matweather.dialogsInitializing.AlertDialogTools.EditTextCustomizer;
import paweltypiak.matweather.dialogsInitializing.AlertDialogTools.KeyboardVisibilitySetter;
import paweltypiak.matweather.usefulClasses.FavouritesEditor;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class EditFavouritesDialogInitializer {

    public static AlertDialog getEditFavouritesDialog(Activity activity){
        return initializeEditFavouritesDialog(activity);
    }

    private static  AlertDialog initializeEditFavouritesDialog(Activity activity){
        View dialogView=initializeDialogView(activity);
        setDialogViewClickableAndFocusable(dialogView);
        EditText[] editTextArray=initializeEditTexts(activity,dialogView);
        initializeCheckbox(activity,dialogView);
        AlertDialog editFavouritesDialog = buildAlertDialog(activity,dialogView);
        setDialogOnShowListener(activity,editTextArray,editFavouritesDialog);
        setDialogOnDismissListener(activity,editFavouritesDialog);
        return editFavouritesDialog;
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
        EditText subtitleEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_subtitle_edit_text);
        subtitleEditText.setText(location[1]);
        EditText[] editTextArray={titleEditText,subtitleEditText};
        return editTextArray;
    }

    private static void initializeCheckbox(Activity activity,View dialogView){
        CheckBox checkBox=(CheckBox)dialogView.findViewById(R.id.edit_location_dialog_checkbox);
        checkBox.setChecked(FavouritesEditor.isDefeaultLocationEqual(activity,null));
    }

    private static AlertDialog buildAlertDialog(Activity activity,View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.edit_location_dialog_title),
                R.drawable.dialog_edit_icon,
                null,
                false,
                activity.getString(R.string.edit_location_dialog_positive_button),
                new SaveChangesRunnable(activity,dialogView),
                activity.getString(R.string.edit_location_dialog_neutral_button),
                new DeleteFromFavouritesRunnable(activity),
                activity.getString(R.string.edit_location_dialog_negative_button),
                null);
        return alertDialogBuilder.getAlertDialog();
    }

    private static void setDialogOnShowListener(final Activity activity,
                                                final EditText[] editTextArray,
                                                final AlertDialog editFavouritesDialog){
        editFavouritesDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                EditTextCustomizer.customizeEditText(activity,editFavouritesDialog,editTextArray[0]);
                EditTextCustomizer.customizeEditText(activity,editFavouritesDialog,editTextArray[1]);
            }
        });
    }

    private static void setDialogOnDismissListener(final Activity activity,
                                                   AlertDialog editFavouritesDialog){
        editFavouritesDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                KeyboardVisibilitySetter.hideKeyboard(activity,null);
            }
        });
    }
}
