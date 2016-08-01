package paweltypiak.matweather.firstLaunching;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import paweltypiak.matweather.DialogInitializer;
import paweltypiak.matweather.R;
import paweltypiak.matweather.UsefulFunctions;

public class FirstLaunchLocationFragment extends Fragment {

    private AlertDialog differentLocationDialog;
    private AlertDialog emptyLocationNameDialog;
    private DialogInitializer dialogInitializer;
    private RadioButton differentLocationRadioButton;
    private int choosenLocationOption;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_launch_location_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialogInitializer=new DialogInitializer(getActivity());
        initializeRadioGroup();
    }


    private void initializeRadioGroup(){
        RadioGroup radioGroup=(RadioGroup)getActivity().findViewById(R.id.first_launch_location_fragment_radio_group);
        final RadioButton currentLocationRadioButton=(RadioButton)getActivity().findViewById(R.id.first_launch_location_fragment_current_location_radio_button);
        differentLocationRadioButton=(RadioButton)getActivity().findViewById(R.id.first_launch_location_fragment_different_location_radio_button);
        radioGroup.check(currentLocationRadioButton.getId());
        choosenLocationOption=1;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.first_launch_location_fragment_current_location_radio_button) {
                    choosenLocationOption=1;
                    Log.d("location_option", ""+choosenLocationOption);

                } else if (i == R.id.first_launch_location_fragment_different_location_radio_button) {
                    choosenLocationOption=2;
                    Log.d("location_option", ""+choosenLocationOption);

                }
            }
        });

        setDifferentLocationRadioButtonOnClickListener(differentLocationRadioButton);

    }

    private void setDifferentLocationRadioButtonOnClickListener(final RadioButton radioButton){
        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("location", "different on click");
                differentLocationDialog=dialogInitializer.initializeDifferentLocationDialog(radioButton);
                differentLocationDialog.show();
                UsefulFunctions.showKeyboard(getActivity());
            }
        });
    }

    public String getDifferentLocationName(){
        String differentLocationString=differentLocationRadioButton.getText().toString();
        Log.d("diffradio", differentLocationString);
        return differentLocationString;
    }

    public int getChoosenLocationOption() {
        return choosenLocationOption;
    }

    public void showEmptyLocationNameDialog(){
        if(emptyLocationNameDialog==null) emptyLocationNameDialog=dialogInitializer.initializeEmptyLocationNameDialog(1);
        emptyLocationNameDialog.show();
    }
}
