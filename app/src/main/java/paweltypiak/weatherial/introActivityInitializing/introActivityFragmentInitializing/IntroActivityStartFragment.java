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
package paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import paweltypiak.weatherial.R;

public class IntroActivityStartFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.intro_activity_fragment_start, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAppIcon();
    }

    private void setAppIcon(){
        ImageView appIconImageView;
        appIconImageView=(ImageView)getActivity().findViewById(R.id.intro_activity_start_fragment_app_icon_image);
        Picasso.with(getActivity()).load(R.drawable.logo_intro_activity).fit().centerInside().into(appIconImageView, new Callback() {
            @Override
            public void onSuccess() {
                fadeInMainLayout();
            }

            @Override
            public void onError() {

            }
        });
    }

    private void fadeInMainLayout(){
        LinearLayout mainFragmentLayout=(LinearLayout)getActivity().findViewById(R.id.intro_activity_layout);
        long transitionTime=200;
        mainFragmentLayout.setAlpha(0f);
        mainFragmentLayout.setVisibility(View.VISIBLE);
        mainFragmentLayout.animate()
                .alpha(1f)
                .setDuration(transitionTime)
                .setListener(null);
    }
}
