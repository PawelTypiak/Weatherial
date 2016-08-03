package paweltypiak.matweather.firstLaunching;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import paweltypiak.matweather.R;
import paweltypiak.matweather.UsefulFunctions;

public class FirstLaunchLocalizationOptionsFragment extends Fragment{

    private int choosenLocalizationOption=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_launch_localization_options_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radioGroupListener();
    }

    private void radioGroupListener(){
        SharedPreferences sharedPreferences = UsefulFunctions.getSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        RadioGroup radioGroup=(RadioGroup)getActivity().findViewById(R.id.first_launch_localization_options_fragment_radio_group);
        radioGroup.check(getActivity().findViewById(R.id.first_launch_localization_options_fragment_gps_radio_button).getId());
        choosenLocalizationOption=1;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.first_launch_localization_options_fragment_gps_radio_button) {
                    Log.d("localization_option", "gps");
                    choosenLocalizationOption=1;
                } else if (i == R.id.first_launch_localization_options_fragment_network_radio_button) {
                    Log.d("localization_option", "network");
                    choosenLocalizationOption=2;
                }
            }
        });
    }
    public int getChoosenLocalizationOption() {
        return choosenLocalizationOption;
    }
}
