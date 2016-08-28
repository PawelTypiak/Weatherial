package paweltypiak.matweather.firstLaunching;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import paweltypiak.matweather.R;

public class FirstLaunchStartFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_launch_start_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAppIcon();
    }

    private void setAppIcon(){
        ImageView appIconImageView;
        appIconImageView=(ImageView)getActivity().findViewById(R.id.first_launch_start_fragment_app_icon_image);
        Picasso.with(getActivity()).load(R.drawable.app_icon).fit().centerInside().into(appIconImageView);
    }
}
