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
import android.view.View;
import android.widget.LinearLayout;
import paweltypiak.weatherial.R;

class ApplicationSourceSectionInitializer {

    ApplicationSourceSectionInitializer(Activity activity,
                                               InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        setRepositoryLayoutListeners(activity,nestedScrollViewInitializer);
        setLicenseLayoutListeners(activity,nestedScrollViewInitializer);
    }

    private void setRepositoryLayoutListeners(final Activity activity,
                                              final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        LinearLayout repositoryLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_application_source_repository_web_layout);
        String repositoryAddress=activity.getString(R.string.project_repository_address);
        setRepositoryLayoutOnClickListener(activity,nestedScrollViewInitializer,repositoryLayout,repositoryAddress);
        setRepositoryLayoutOnLongClickListener(activity,nestedScrollViewInitializer,repositoryLayout,repositoryAddress);
    }

    private void setRepositoryLayoutOnClickListener(final Activity activity,
                                                    final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer,
                                                    LinearLayout repositoryLayout,
                                                    final String repositoryAddress){
        repositoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollViewInitializer.initializeWebIntent(activity,repositoryAddress);
            }
        });
    }

    private void setRepositoryLayoutOnLongClickListener(final Activity activity,
                                                        final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer,
                                                        LinearLayout repositoryApiLayout,
                                                        final String repositoryAddress){
        repositoryApiLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                nestedScrollViewInitializer.copyAddressToClipboard(activity,repositoryAddress);
                return true;
            }
        });
    }

    private void setLicenseLayoutListeners(Activity activity,
                                           InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        LinearLayout licenseLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_application_source_license_web_layout);
        String apacheLicenceAddress=activity.getString(R.string.apache_license_address);
        setLicenseLayoutOnClickListener(activity,nestedScrollViewInitializer,licenseLayout,apacheLicenceAddress);
        setLicenseLayoutOnLongClickListener(activity,nestedScrollViewInitializer,licenseLayout,apacheLicenceAddress);
    }

    private void setLicenseLayoutOnClickListener(final Activity activity,
                                                 final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer,
                                                 LinearLayout licenseLayout,
                                                 final String apacheLicenceAddress){
        licenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollViewInitializer.initializeWebIntent(activity,apacheLicenceAddress);
            }
        });
    }

    private void setLicenseLayoutOnLongClickListener(final Activity activity,
                                                     final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer,
                                                     LinearLayout licenseLayout,
                                                     final String apacheLicenceAddress){
        licenseLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                nestedScrollViewInitializer.copyAddressToClipboard(activity,apacheLicenceAddress);
                return true;
            }
        });
    }
}
