package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.searchDialogInitializing;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.alertDialogTools.KeyboardVisibilitySetter;
import paweltypiak.weatherial.usefulClasses.UsefulFunctions;

class DifferentLocationDialogRunnable implements  Runnable{

    private RadioButton radioButton;
    private View dialogView;
    private Activity activity;

    DifferentLocationDialogRunnable(Activity activity,RadioButton radioButton, View dialogView) {
        this.activity=activity;
        this.radioButton = radioButton;
        this.dialogView=dialogView;
    }

    @Override
    public void run() {
        KeyboardVisibilitySetter.hideKeyboard(activity,null);
        EditText editText=(EditText)dialogView.findViewById(R.id.search_edit_text);
        String editTextString=editText.getText().toString();
        editTextString =UsefulFunctions.getFormattedString(editTextString);
        radioButton.setText(editTextString);
    }
}
