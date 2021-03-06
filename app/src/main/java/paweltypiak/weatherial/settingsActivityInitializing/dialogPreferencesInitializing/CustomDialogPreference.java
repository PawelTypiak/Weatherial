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
package paweltypiak.weatherial.settingsActivityInitializing.dialogPreferencesInitializing;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.utils.UsefulFunctions;

public abstract class CustomDialogPreference extends DialogPreference{
    //dialog preference with custom layout
    private String dialogTitle;

    public CustomDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setPositiveButtonText(getContext().getString(R.string.preferences_dialog_positive_button));
        setNegativeButtonText(getContext().getString(R.string.preferences_dialog_negative_button));
    }

    protected void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    protected RadioButton setRadioButtonLayout(String radioButtonText,int radioButtonId,int radioButtonBottomMargin){
        AppCompatRadioButton radioButton=new AppCompatRadioButton(getContext());
        radioButton.setText(UsefulFunctions.fromHtml(radioButtonText));
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,getContext().getResources().getDimensionPixelSize(R.dimen.dialog_text_size));
        radioButton.setTextColor(ContextCompat.getColor(getContext(),R.color.textSecondaryLightBackground));
        radioButton.setId(radioButtonId);
        setRadioButtonMargins(radioButton,radioButtonBottomMargin);
        return radioButton;
    }

    private void setRadioButtonMargins(RadioButton radioButton,int radioButtonBottomMargin){
        LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,0,0, radioButtonBottomMargin);
        radioButton.setLayoutParams(layoutParams);
    }

    @Override
    protected View onCreateDialogView() {
        View view = View.inflate(getContext(),R.layout.dialog_radiogroup,null);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group_dialog_radiogroup);
        buildRadioGroup(radioGroup);
        return view;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setTitle(dialogTitle);
        super.onPrepareDialogBuilder(builder);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);
        AlertDialog dialog=(AlertDialog)getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult){
            onPositiveResult();
        }
    }
    protected abstract void buildRadioGroup(RadioGroup radioGroup);

    protected abstract void onPositiveResult();

    protected abstract void setPreferenceSummary();
}
