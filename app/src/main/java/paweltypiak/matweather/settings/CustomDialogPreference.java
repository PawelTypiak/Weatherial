package paweltypiak.matweather.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

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

    protected RadioButton setRadioButtonLayout(String radioButtonText,int radioButtonId,int bottomMargin){
        RadioButton radioButton=new RadioButton(getContext());
        radioButton.setText(UsefulFunctions.fromHtml(radioButtonText));
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,getContext().getResources().getDimensionPixelSize(R.dimen.dialog_text_size));
        radioButton.setTextColor(ContextCompat.getColor(getContext(),R.color.textSecondaryLightBackground));
        radioButton.setId(radioButtonId);
        UsefulFunctions.setRadioButtonMargins(radioButton,(Activity)getContext(),0,0,0,bottomMargin);
        return radioButton;
    }

    @Override
    protected View onCreateDialogView() {
        View view = View.inflate(getContext(),R.layout.dialog_radiogroup,null);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup_dialog_radiogroup);
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
