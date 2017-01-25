package paweltypiak.matweather.dialogsInitializing.alertDialogTools;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class EditTextCustomizer {

    public static void customizeEditText(final Activity activity, final AlertDialog alertDialog, final EditText editText){
        updateLayoutOnAlertDialogShow(activity,alertDialog,editText);
        setEditTextOnFocusChangeListener(activity,editText);
        addEditTextTextChangedListener(activity,alertDialog,editText);
        setEditTextOnKeyListener(editText);
    }
    private static void updateLayoutOnAlertDialogShow(Activity activity,AlertDialog alertDialog,EditText editText){
        if(editText.getText().length()!=0) {
            editText.getBackground().mutate().setColorFilter(ContextCompat.getColor(activity, R.color.transparent), PorterDuff.Mode.SRC_ATOP);
            editText.setHint("");
        }
        else {
            AlertDialogButtonsCustomizer.setDialogButtonDisabled(alertDialog,activity);
        }
    }

    private static void setEditTextOnFocusChangeListener(final Activity activity,final EditText editText){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean bool) {
                if(editText.isFocused()) {
                    updateLayoutIfIsFocused(activity,editText);
                }
                else {
                    updateLayoutIfNotFocused(activity,editText);
                }
            }
        });
    }

    private static void updateLayoutIfIsFocused(Activity activity,EditText editText){
        editText.getBackground().mutate().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        KeyboardVisibilitySetter.showKeyboard(activity);
    }

    private static void updateLayoutIfNotFocused(Activity activity,EditText editText){
        String editTextString=editText.getText().toString();
        editTextString=UsefulFunctions.getFormattedString(editTextString);
        if(editTextString.length()==0) {
            editText.getBackground().mutate().setColorFilter(ContextCompat.getColor(activity,R.color.hintLightBackground), PorterDuff.Mode.SRC_ATOP);
        }
        else{
            editText.getBackground().mutate().setColorFilter(ContextCompat.getColor(activity,R.color.transparent), PorterDuff.Mode.SRC_ATOP);
            editText.setText(editTextString);
        }
        KeyboardVisibilitySetter.hideKeyboard(activity,editText);
    }

    private static void addEditTextTextChangedListener(final Activity activity,final AlertDialog alertDialog,final EditText editText){
        final String hint=editText.getHint().toString();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editText.getText().length()==0) {
                    editText.setHint(hint);
                    AlertDialogButtonsCustomizer.setDialogButtonDisabled(alertDialog,activity);
                }
                else {
                    editText.setHint("");
                    AlertDialogButtonsCustomizer.setDialogButtonEnabled(alertDialog,activity);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private static void setEditTextOnKeyListener(final EditText editText){
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    editText.clearFocus();
                    return false;
                }
                return false;
            }
        });
    }
}
