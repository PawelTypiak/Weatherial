package paweltypiak.matweather.firstLaunching;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import paweltypiak.matweather.R;

public class FirstLaunchLocationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_launch_location_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeLayout();
    }

    private void initializeLayout(){
        RadioGroup radioGroup=(RadioGroup)getActivity().findViewById(R.id.first_launch_location_fragment_radio_group);
        radioGroup.check(getActivity().findViewById(R.id.first_launch_location_fragment_current_location_radio_button).getId());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.first_launch_location_fragment_current_location_radio_button) {
                    Log.d("location", "current ");

                } else if (i == R.id.first_launch_location_fragment_different_location_radio_button) {
                    Log.d("location", "different ");

                }
            }
        });
    }
}
