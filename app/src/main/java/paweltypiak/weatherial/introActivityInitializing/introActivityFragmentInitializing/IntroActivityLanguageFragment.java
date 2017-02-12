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

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityConfigurationFragmentInitializing.OnSettingsFragmentViewCreatedListener;
import paweltypiak.weatherial.utils.SharedPreferencesModifier;
import paweltypiak.weatherial.utils.UsefulFunctions;

public class IntroActivityLanguageFragment extends Fragment{

    private OnSettingsFragmentViewCreatedListener onSettingsFragmentViewCreatedListener;
    private TextView languageVersionHeaderTextView;
    private RadioGroup languageVersionRadioGroup;
    private RadioButton englishRadioButton;
    private RadioButton polishRadioButton;
    private TextView nextButtonTextView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getOnSettingsFragmentViewCreatedListener(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getOnSettingsFragmentViewCreatedListener(activity);
        }
    }

    private void getOnSettingsFragmentViewCreatedListener(Context context){
        if (context instanceof OnSettingsFragmentViewCreatedListener) {
            onSettingsFragmentViewCreatedListener = (OnSettingsFragmentViewCreatedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement onSettingsFragmentViewCreatedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.intro_activity_fragment_language, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLayoutResources();
        setOnRadioGroupCheckedChangeListener();
        onSettingsFragmentViewCreatedListener.fadeInConfigurationFragmentLayout();
    }

    private void refreshFragmentLayout(){
        languageVersionHeaderTextView.setText(getActivity().getText(R.string.intro_activity_language_version_header));
        englishRadioButton.setText(getActivity().getText(R.string.language_version_english));
        polishRadioButton.setText(getActivity().getText(R.string.language_version_polish));
        nextButtonTextView.setText(getString(R.string.intro_activity_next_button_continue_text));
    }

    private void getLayoutResources(){
        languageVersionHeaderTextView =(TextView)getActivity().findViewById(R.id.intro_activity_language_fragment_header_text);
        languageVersionRadioGroup=(RadioGroup)getActivity().findViewById(R.id.intro_activity_language_fragment_radio_group);
        englishRadioButton=(RadioButton)getActivity().findViewById(R.id.intro_activity_language_fragment_english_radio_button);
        polishRadioButton=(RadioButton)getActivity().findViewById(R.id.intro_activity_language_fragment_polish_radio_button);
        nextButtonTextView = (TextView)getActivity().findViewById(R.id.intro_activity_button_text);
    }

    private void setOnRadioGroupCheckedChangeListener(){
        int currentLocale=UsefulFunctions.getLocale(getContext());
        if(currentLocale==0) {
            languageVersionRadioGroup.check(englishRadioButton.getId());
            setSelectedLanguage(0);
        }
        else if(currentLocale==1){
            languageVersionRadioGroup.check(polishRadioButton.getId());
            setSelectedLanguage(1);
        }
        languageVersionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.intro_activity_language_fragment_english_radio_button) {
                    setSelectedLanguage(0);
                } else if (i == R.id.intro_activity_language_fragment_polish_radio_button) {
                    setSelectedLanguage(1);
                }
                UsefulFunctions.getLocale(getContext());
                refreshFragmentLayout();
            }
        });
    }

    private void setSelectedLanguage(int type){
        SharedPreferencesModifier.setLanguage(getActivity(),type);
        UsefulFunctions.setLocale(getContext(),type);
    }
}
