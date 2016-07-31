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

/**
 * Created by Pawcioch on 22.07.2016.
 */
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
        SharedPreferences sharedPreferences = UsefulFunctions.getSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        RadioGroup radioGroup=(RadioGroup)getActivity().findViewById(R.id.first_launch_language_fragment_radio_group);
        radioGroup.check(getActivity().findViewById(R.id.first_launch_language_fragment_english_radio_button).getId());
        editor.putInt(getString(R.string.shared_preferences_language_key), 1);
        editor.commit();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.first_launch_language_fragment_english_radio_button) {
                    editor.putInt(getString(R.string.shared_preferences_language_key), 1);
                    editor.commit();
                } else if (i == R.id.first_launch_language_fragment_polish_radio_button) {
                    editor.putInt(getString(R.string.shared_preferences_language_key), 2);
                    editor.commit();
                }
            }
        });
    }
}
