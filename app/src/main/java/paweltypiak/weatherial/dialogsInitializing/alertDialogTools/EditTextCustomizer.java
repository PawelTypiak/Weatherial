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
package paweltypiak.weatherial.dialogsInitializing.alertDialogTools;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.utils.UsefulFunctions;

public class EditTextCustomizer {

    public static void customizeEditText(final Activity activity, final AlertDialog alertDialog, final EditText editText){
        setEditTextOnFocusChangeListener(activity,editText);
        addEditTextTextChangedListener(activity,alertDialog,editText);
        setEditTextOnKeyListener(editText);
        updateLayoutOnAlertDialogShow(activity,alertDialog,editText);
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
        Log.d("hint", "hint: "+hint);
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
    private static void updateLayoutOnAlertDialogShow(Activity activity,AlertDialog alertDialog,EditText editText){
        if(editText.getText().length()!=0) {
            editText.getBackground().mutate().setColorFilter(ContextCompat.getColor(activity, R.color.transparent), PorterDuff.Mode.SRC_ATOP);
            editText.setHint("");
        }
        else {
            AlertDialogButtonsCustomizer.setDialogButtonDisabled(alertDialog,activity);
        }
    }
}
