package paweltypiak.matweather.firstLaunching;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import paweltypiak.matweather.R;
import paweltypiak.matweather.UsefulFunctions;

public class FirstLaunchLanguageFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_launch_language_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radioGroupListener();
    }

    private void radioGroupListener(){
        RadioGroup radioGroup=(RadioGroup)getActivity().findViewById(R.id.first_launch_language_fragment_radio_group);
        radioGroup.check(getActivity().findViewById(R.id.first_launch_language_fragment_english_radio_button).getId());
        UsefulFunctions.setLanguage(getActivity(),1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.first_launch_language_fragment_english_radio_button) {
                    UsefulFunctions.setLanguage(getActivity(),1);
                } else if (i == R.id.first_launch_language_fragment_polish_radio_button) {
                    UsefulFunctions.setLanguage(getActivity(),2);
                }
            }
        });
    }
}
