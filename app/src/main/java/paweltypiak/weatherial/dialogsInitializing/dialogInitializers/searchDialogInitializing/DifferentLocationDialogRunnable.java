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
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.alertDialogTools.KeyboardVisibilitySetter;
import paweltypiak.weatherial.utils.UsefulFunctions;

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
