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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.utils.SharedPreferencesModifier;

public class IntroActivityUnitsFragment extends Fragment{

    private int units[]={0,0,0,0};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.intro_activity_fragment_units, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeLayout();
    }

    private void initializeLayout() {
        for(int i=0;i<4;i++){
            final FirstLaunchSpinner spinner=initializeSpinner(i);
            setItemClickable(spinner,i);
            setSpinnerListener(spinner,i);
        }
    }

    private FirstLaunchSpinner initializeSpinner(int id){
        //initialize custom spinner
        FirstLaunchSpinner spinner
                = (FirstLaunchSpinner) getActivity().findViewById(
                getActivity().
                        getResources().
                        getIdentifier(
                                "intro_activity_units_fragment_spinner_"+id,
                                "id",
                                getActivity().getPackageName()
                        )
        );
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getActivity().getApplicationContext(),
                R.layout.spinner_item,
                getActivity().
                        getResources().
                        getStringArray(
                                getActivity().getResources().getIdentifier(
                                        "units_array_"+id,
                                        "array",
                                        getActivity().getPackageName()
                                )
                        )
        );
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        return spinner;
    }

    private void setItemClickable(final Spinner spinner, final int id) {
        //set layout visibility after all icons are loaded
        LinearLayout clickableView
                =(LinearLayout)getActivity().
                findViewById(getResources().
                        getIdentifier
                                ("intro_activity_units_fragment_clickable_layout_" + id,
                                        "id",
                                        getActivity().getPackageName()
                                )
                );
        clickableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.performClick();
            }
        });
    }

    private void setSpinnerListener(final FirstLaunchSpinner spinner, final int id){
        final TextView unitTextView
                =(TextView)getActivity().findViewById(getResources().
                getIdentifier
                        ("intro_activity_units_fragment_spinner_text_view_" + id,
                                "id",
                                getActivity().getPackageName()
                        ));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                units[id]=i;
                SharedPreferencesModifier.setUnits(getActivity(),units);
                unitTextView.setText(spinner.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public static class FirstLaunchSpinner extends Spinner {
        OnItemSelectedListener listener;
        public FirstLaunchSpinner(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        public FirstLaunchSpinner(Context context) {
            super(context);
        }
        @Override
        public void setSelection(int position) {
            super.setSelection(position);
            if (listener != null)
                listener.onItemSelected(null, null, position, 0);
        }
    }
}
