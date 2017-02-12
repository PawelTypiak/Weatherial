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
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import paweltypiak.weatherial.R;

public class InfoActivityNestedScrollViewInitializer {

    public InfoActivityNestedScrollViewInitializer(Activity activity){
        initializeApplicationSourceSection(activity);
        initializeApiSection(activity);
        initializeExternalLibrariesSection(activity);
    }

    private void initializeApplicationSourceSection(Activity activity){
        new ApplicationSourceSectionInitializer(activity,this);
    }

    private void initializeApiSection(Activity activity){
        new ApiSectionInitializer(activity,this);
    }

    private void initializeExternalLibrariesSection(Activity activity){
        new ExternalLibrariesSectionInitializer(activity,this);
    }

    void initializeWebIntent(Activity activity,String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        activity.startActivity(intent);
    }

    public void copyAddressToClipboard(Activity activity,String copiedText){
        ClipboardManager myClipboard;
        ClipData myClip;
        myClipboard = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
        myClip = ClipData.newPlainText("text", copiedText);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(
                activity,
                activity.getString(R.string.info_activity_clipboard_toast_message),
                Toast.LENGTH_SHORT).show();
    }
}
