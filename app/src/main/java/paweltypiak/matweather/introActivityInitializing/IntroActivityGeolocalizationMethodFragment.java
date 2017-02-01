package paweltypiak.matweather.introActivityInitializing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import paweltypiak.matweather.R;

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
                    Log.d("geolocalization_method", "gps");
                    selectedGeolocalizationMethod =0;
                } else if (i == R.id.intro_activity_geolocalization_methods_fragment_network_radio_button) {
                    Log.d("geolocalization_method", "network");
                    selectedGeolocalizationMethod =1;
                }
            }
        });
    }

    public int getSelectedGeolocalizationMethod() {
        return selectedGeolocalizationMethod;
    }
}
