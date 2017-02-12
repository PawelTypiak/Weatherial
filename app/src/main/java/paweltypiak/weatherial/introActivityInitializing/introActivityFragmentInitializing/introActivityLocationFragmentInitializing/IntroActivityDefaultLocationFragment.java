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
package paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLocationFragmentInitializing;

import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.noDifferentLocationSelectedDialogInitializing.NoDifferentLocationSelectedDialogInitializer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.searchDialogInitializing.SearchDialogInitializer;
import paweltypiak.weatherial.R;

public class IntroActivityDefaultLocationFragment extends Fragment {

    private AlertDialog differentLocationDialog;
    private AlertDialog emptyLocationNameDialog;
    private RadioButton differentLocationRadioButton;
    private int selectedDefaultLocationOption;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.intro_activity_fragment_default_location, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeRadioGroup();
    }

    private void initializeRadioGroup(){
        RadioGroup radioGroup=(RadioGroup)getActivity().findViewById(R.id.intro_activity_default_location_fragment_radio_group);
        final RadioButton currentLocationRadioButton=(RadioButton)getActivity().findViewById(R.id.intro_activity_default_location_fragment_current_location_radio_button);
        differentLocationRadioButton=(RadioButton)getActivity().findViewById(R.id.intro_activity_default_location_fragment_different_location_radio_button);
        radioGroup.check(currentLocationRadioButton.getId());
        selectedDefaultLocationOption =0;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.intro_activity_default_location_fragment_current_location_radio_button) {
                    selectedDefaultLocationOption =0;

                } else if (i == R.id.intro_activity_default_location_fragment_different_location_radio_button) {
                    selectedDefaultLocationOption =1;
                }
            }
        });
        setDifferentLocationRadioButtonOnClickListener(differentLocationRadioButton);
    }

    private void setDifferentLocationRadioButtonOnClickListener(final RadioButton radioButton){
        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SearchDialogInitializer searchDialogInitializer
                        =new SearchDialogInitializer(
                        getActivity(),
                        0,
                        radioButton);
                differentLocationDialog=searchDialogInitializer.getSearchDialog();
                differentLocationDialog.show();
            }
        });
    }

    public String getDifferentLocationName(){
        String differentLocationString=differentLocationRadioButton.getText().toString();
        return differentLocationString;
    }

    public int getSelectedDefaultLocationOption() {return selectedDefaultLocationOption;}

    public void showNoDifferentLocationSelectedDialog(){
        if(emptyLocationNameDialog==null) {
            emptyLocationNameDialog
                    = NoDifferentLocationSelectedDialogInitializer.
                    getNoDifferentLocationSelectedDialog(getActivity(),differentLocationDialog);
        }
        emptyLocationNameDialog.show();
    }
}
