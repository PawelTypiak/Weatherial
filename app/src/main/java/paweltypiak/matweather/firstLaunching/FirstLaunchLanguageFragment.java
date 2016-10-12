package paweltypiak.matweather.firstLaunching;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class FirstLaunchLanguageFragment extends Fragment{

    private TextView languageVersionHeaderTextView;
    private RadioGroup languageVersionRadioGroup;
    private RadioButton englishRadioButton;
    private RadioButton polishRadioButton;
    private TextView nextButtonTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_launch_language_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLayoutResources();
        radioGroupListener();
    }

    private void refreshFragmentLayout(){
        languageVersionHeaderTextView.setText(getActivity().getText(R.string.first_launch_language_version_header));
        englishRadioButton.setText(getActivity().getText(R.string.language_version_english));
        polishRadioButton.setText(getActivity().getText(R.string.language_version_polish));
        nextButtonTextView.setText(getString(R.string.first_launch_button_continue_text));
    }

    private void getLayoutResources(){
        languageVersionHeaderTextView =(TextView)getActivity().findViewById(R.id.first_launch_language_fragment_header_text);
        languageVersionRadioGroup=(RadioGroup)getActivity().findViewById(R.id.first_launch_language_fragment_radio_group);
        englishRadioButton=(RadioButton)getActivity().findViewById(R.id.first_launch_language_fragment_english_radio_button);
        polishRadioButton=(RadioButton)getActivity().findViewById(R.id.first_launch_language_fragment_polish_radio_button);
        nextButtonTextView = (TextView)getActivity().findViewById(R.id.first_launch_button_text);
    }

    private void radioGroupListener(){
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
                if (i == R.id.first_launch_language_fragment_english_radio_button) {
                    Log.d("language", "english");
                    setSelectedLanguage(0);
                } else if (i == R.id.first_launch_language_fragment_polish_radio_button) {
                    Log.d("language", "polish");
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
        Log.d("sprawdzenie", "setSelectedLanguage: "+SharedPreferencesModifier.getLanguageVersion(getContext()));
    }
}
