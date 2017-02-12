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
package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.mapsDialogInitializing.MapsDialogInitializer;

class ToolbarTitleButtonInitializer {

    ToolbarTitleButtonInitializer(Activity activity){
        setToolbarTitleButtonOnClickListener(activity);
    }

    private void setToolbarTitleButtonOnClickListener(final Activity activity){
        View clickableView= activity.findViewById(R.id.toolbar_layout_title_clickable_view);
        clickableView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog mapsDialog= MapsDialogInitializer.getMapsDialog(activity);
                        mapsDialog.show();
                    }
                });
    }
}
