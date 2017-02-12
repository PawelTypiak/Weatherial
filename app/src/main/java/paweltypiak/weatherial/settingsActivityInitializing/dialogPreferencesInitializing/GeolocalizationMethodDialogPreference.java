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
package paweltypiak.weatherial.settingsActivityInitializing.dialogPreferencesInitializing;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.utils.SharedPreferencesModifier;

public class GeolocalizationMethodDialogPreference extends CustomDialogPreference{

    private int selectedOption =-1;

    public GeolocalizationMethodDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setDialogTitle(getContext().getString(R.string.preferences_geolocalization_method_title));
        setPreferenceSummary();
    }

    protected void buildRadioGroup(RadioGroup radioGroup){
        String gpsRadioButtonText=getContext().getString(R.string.geolocalization_method_gps);
        int gpsRadioButtonId=R.id.geolocalization_method_dialog_gps_radio_button_id;
        int gpsRadioButtonBottomMargin=(int)getContext().getResources().getDimension(R.dimen.radio_button_bottom_margin);
        RadioButton gpsRadioButton
                =setRadioButtonLayout(gpsRadioButtonText,gpsRadioButtonId,gpsRadioButtonBottomMargin);
        radioGroup.addView(gpsRadioButton);
        String networkRadioButtonText=getContext().getString(R.string.geolocalization_method_network);
        int networkRadioButtonId=R.id.geolocalization_method_dialog_network_radio_button_id;
        int networkRadioButtonBottomMargin=0;
        RadioButton networkRadioButton
                =setRadioButtonLayout(networkRadioButtonText,networkRadioButtonId,networkRadioButtonBottomMargin);
        radioGroup.addView(networkRadioButton);
        int geolocalizationMethod=SharedPreferencesModifier.getGeolocalizationMethod(getContext());
        if(geolocalizationMethod==0){
            gpsRadioButton.setChecked(true);
            selectedOption =0;
        }
        else if(geolocalizationMethod==1) {
            networkRadioButton.setChecked(true);
            selectedOption =1;
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.geolocalization_method_dialog_gps_radio_button_id) {
                    selectedOption =0;
                }
                else if (i == R.id.geolocalization_method_dialog_network_radio_button_id) {
                    selectedOption =1;
                }
            }
        });
    }

    protected void onPositiveResult(){
        if(selectedOption ==0){
            SharedPreferencesModifier.setGeolocalizationMethod(getContext(),0);
            setSummary(getContext().getString(R.string.geolocalization_method_gps));
        }
        else if(selectedOption ==1){
            SharedPreferencesModifier.setGeolocalizationMethod(getContext(),1);
            setSummary(getContext().getString(R.string.geolocalization_method_network));
        }
    }

    protected void setPreferenceSummary(){
        int geolocalizationMethod=SharedPreferencesModifier.getGeolocalizationMethod(getContext());
        if(geolocalizationMethod==0){
            setSummary(getContext().getString(R.string.geolocalization_method_gps));
        }
        else if(geolocalizationMethod==1){
            setSummary(getContext().getString(R.string.geolocalization_method_network));
        }
        else  setSummary(getContext().getString(R.string.preferences_geolocalization_method_default_summary));
    }
}
