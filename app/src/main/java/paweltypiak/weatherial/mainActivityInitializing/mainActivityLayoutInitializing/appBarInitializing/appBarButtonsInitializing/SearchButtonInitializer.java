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
import android.widget.ImageView;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.searchDialogInitializing.SearchDialogInitializer;

class SearchButtonInitializer {

    SearchButtonInitializer(Activity activity){
        ImageView searchImageView =(ImageView) activity.findViewById(R.id.toolbar_layout_search_button_image);
        setSearchButtonOnClickListener(activity, searchImageView);
    }

    private void setSearchButtonOnClickListener(final Activity activity,
                                                ImageView searchImageView){
        SearchDialogInitializer searchDialogInitializer
                =new SearchDialogInitializer(
                activity,
                1,
                null);
        final AlertDialog searchDialog=searchDialogInitializer.getSearchDialog();
        searchImageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchDialog.show();
                    }
                });
    }
}
