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
package paweltypiak.weatherial.infoActivityInitializing.infoActivityNestedScrollViewInitializing;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import paweltypiak.weatherial.R;

class ExternalLibrariesSectionInitializer {

    ExternalLibrariesSectionInitializer(Activity activity,
                                               InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        initializeExternalLibrariesLicenseHiperlink(activity,nestedScrollViewInitializer);
        initializeExternalLibrariesHiperlinks(activity,nestedScrollViewInitializer);
    }

    private void initializeExternalLibrariesLicenseHiperlink(final Activity activity,
                                                             InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        TextView licenseTextView
                =(TextView)activity.findViewById(R.id.info_activity_external_libraries_license_text);
        setExternalLibrariesLicenseTextViewOnClickListener(activity,nestedScrollViewInitializer,licenseTextView);
        setExternalLibrariesLicenseTextViewHighlight(activity,licenseTextView);
    }

    private void setExternalLibrariesLicenseTextViewOnClickListener(final Activity activity,
                                                                    final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer,
                                                                    TextView licenseTextView){
        licenseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String webAddress=activity.getString(R.string.apache_license_address);
                nestedScrollViewInitializer.initializeWebIntent(activity,webAddress);
            }
        });
    }

    private void setExternalLibrariesLicenseTextViewHighlight(Activity activity,
                                                              TextView licenseTextView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setRippleHighlight(activity,licenseTextView);
        }
        else{
            setStaticHighlight(activity,licenseTextView);
        }
    }

    private void setRippleHighlight(Activity activity,TextView textView){
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = activity.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        textView.setBackgroundResource(backgroundResource);
        typedArray.recycle();
    }

    private void setStaticHighlight(Activity activity,TextView textView){
        StateListDrawable states = new StateListDrawable();
        states.addState(
                new int[] {android.R.attr.state_pressed},
                ContextCompat.getDrawable(activity,R.drawable.rounded_button_background_pressed));
        states.addState(
                new int[] { },
                ContextCompat.getDrawable(activity,R.drawable.rounded_button_background_normal));
        textView.setBackground(states);
    }

    private void initializeExternalLibrariesHiperlinks(final Activity activity,
                                                       InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        setPicassoLibraryLayoutOnClickListener(activity,nestedScrollViewInitializer);
        setSmoothAppBarLayoutLibraryLayoutOnClickListener(activity,nestedScrollViewInitializer);
        setCollapsingToolbarLibraryLayoutOnClickListener(activity,nestedScrollViewInitializer);
    }

    private void setPicassoLibraryLayoutOnClickListener(final Activity activity,
                                                        final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        LinearLayout picassoLibraryLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_external_libraries_picasso_library_layout);
        picassoLibraryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollViewInitializer.initializeWebIntent(
                        activity,
                        activity.getString(R.string.picasso_library_address));
            }
        });
    }

    private void setSmoothAppBarLayoutLibraryLayoutOnClickListener(final Activity activity,
                                                                   final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        LinearLayout smoothAppBarLayoutLibraryLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_external_libraries_smooth_app_bar_layout_library_layout);
        smoothAppBarLayoutLibraryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollViewInitializer.initializeWebIntent(
                        activity,
                        activity.getString(R.string.smooth_app_bar_layout_library_address));
            }
        });
    }

    private void setCollapsingToolbarLibraryLayoutOnClickListener(final Activity activity,
                                                                  final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        LinearLayout collapsingToolbarLibraryLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_external_libraries_multiline_collapsingtoolbar_library_layout);
        collapsingToolbarLibraryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollViewInitializer.initializeWebIntent(
                        activity,
                        activity.getString(R.string.multiline_collapsingtoolbar_library_address));
            }
        });
    }
}
