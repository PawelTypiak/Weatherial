package paweltypiak.matweather.dialogsInitializing.dialogInitializers.searchDialogInitializing;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.alertDialogTools.KeyboardVisibilitySetter;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

class DifferentLocationDialogRunnable implements  Runnable{

    private RadioButton radioButton;
    private View dialogView;
    private EditText editText;
    private String editTextString;
    private Activity activity;

    public DifferentLocationDialogRunnable(Activity activity,RadioButton radioButton, View dialogView) {
        this.activity=activity;
        this.radioButton = radioButton;
        this.dialogView=dialogView;
    }

    @Override
    public void run() {
        KeyboardVisibilitySetter.hideKeyboard(activity,null);
        editText=(EditText)dialogView.findViewById(R.id.search_edit_text);
        editTextString=editText.getText().toString();
        editTextString =UsefulFunctions.getFormattedString(editTextString);
        radioButton.setText(editTextString);
    }
}
