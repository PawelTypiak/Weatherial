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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import paweltypiak.weatherial.R;

public class IntroActivityGeolocalizationMethodFragment extends Fragment{

    private int selectedGeolocalizationMethod =-1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.intro_activity_fragment_geolocalization_methods, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radioGroupListener();
    }

    private void radioGroupListener(){
        RadioGroup radioGroup=(RadioGroup)getActivity().findViewById(R.id.intro_activity_geolocalization_methods_fragment_radio_group);
        radioGroup.check(getActivity().findViewById(R.id.intro_activity_geolocalization_methods_fragment_gps_radio_button).getId());
        selectedGeolocalizationMethod =0;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.intro_activity_geolocalization_methods_fragment_gps_radio_button) {
                    selectedGeolocalizationMethod =0;
                } else if (i == R.id.intro_activity_geolocalization_methods_fragment_network_radio_button) {
                    selectedGeolocalizationMethod =1;
                }
            }
        });
    }

    public int getSelectedGeolocalizationMethod() {
        return selectedGeolocalizationMethod;
    }
}
