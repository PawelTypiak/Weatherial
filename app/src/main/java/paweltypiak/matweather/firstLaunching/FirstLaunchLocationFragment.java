package paweltypiak.matweather.firstLaunching;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import paweltypiak.matweather.R;

public class FirstLaunchLocationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_launch_location_fragment, parent, false);
    }

}
