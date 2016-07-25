package paweltypiak.matweather.firstLaunching;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import paweltypiak.matweather.R;

public class FirstLaunchConfigurationFragment extends Fragment {

    private FragmentTransaction fragmentTransaction;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_launch_configuraion_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        insertNestedFragment(new FirstLaunchLanguageFragment(),"LanguageFragment");
        setAppIcon();
    }

    public void insertNestedFragment(android.support.v4.app.Fragment nestedFragment,String tag) {
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.first_launch_configuration_fragment_placeholder, nestedFragment,tag)
                .commit();
    }

    private void setAppIcon(){
        ImageView appIconImageView;
        appIconImageView=(ImageView)getActivity().findViewById(R.id.first_launch_configuration_fragment_app_icon_image);
        Picasso.with(getActivity()).load(R.drawable.app_icon).fit().centerInside().into(appIconImageView);
    }

}
